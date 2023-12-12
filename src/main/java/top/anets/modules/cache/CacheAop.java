package top.anets.modules.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import top.anets.exception.ServiceException;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author ftm
 * @date 2023/3/17 0017 13:54
 * 缓存击穿：单Key突然过期+高并发               ----------      加锁只放行 + 自动刷新 + 预加载热点数据是实时调整
 * 缓存雪崩: 多热点key 同时过期 + 高并发  ；     ----------      过期时间错开 + 多级缓存() + 限流（同一个key限制流量）   + 自动刷新 +多级缓存（一级缓存的淘汰策略 和 访问后多久过期 不至于大片key同时失效）
 * 缓存穿透：单key 数据库无数据，不缓存 + 高并发  ----------     (存空值 + 布隆过滤)
 */
@Aspect
@Component
public class CacheAop {

    @Pointcut("@annotation(top.anets.modules.cache.Cache)")
    public void pointCut() {}
    private final RateLimiter limiter = RateLimiter.create(139); // 10 requests per second

    @Autowired
    private RedisTemplate redisTemplate;

    private final ExpressionParser parser = new SpelExpressionParser();
    private LoadingCache<String,Object> localCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Object>() {
                @Override
                public Semaphore load(String parameter) {
                    return null;
                }
            });

    private LoadingCache<String, Semaphore> parameterLocks = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Semaphore>() {
                @Override
                public Semaphore load(String parameter) {
                    return new Semaphore(1);
                }
            });

    /**
     * 2种方式修改参数值
     * @param joinPoint
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] params = joinPoint.getArgs();
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取目标方法的参数名数组
        String[] parameterNames = signature.getParameterNames();
//      获取请求参数值
        Cache cache = method.getAnnotation(Cache.class);
        String prefix = null;
        if(StringUtils.isBlank(cache.name())){
            prefix = joinPoint.getTarget().getClass().getSimpleName()+method.getName();
        }else{
            prefix = cache.name();
        }
        String unikey  =null;
        if(StringUtils.isBlank(cache.key())){
            //      获取唯一参数签名
            unikey  = getParamUniKey(params,parameterNames);
        }else{
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < params.length; i++) {
                context.setVariable(parameterNames[i], params[i]);
            }
            // 解析SpEL表达式
            String keyExpression =cache.key();
            String value = parser.parseExpression(keyExpression).getValue(context, String.class);
            unikey =  value;
        }

        String key = prefix +"-"+ unikey;
//      从缓存中取值
        Object cacheRes = redisTemplate.opsForValue().get(key);
        if(cacheRes!=null){

            if(cache.async()){
//              异步更新缓存
                String updateKey = "UpdateKeyPrefix" + key;
                Long ttl = redisTemplate.getExpire(updateKey);
                boolean lock = false;
                if (ttl == null || ttl < 0) {
                    Semaphore parameterLock = parameterLocks.getUnchecked(key);
                    lock = this.tryLock(updateKey, "", 2*60);
                }
                if(lock){
                    ThreadPoolUtils.execute(new Runnable() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            Object rs = joinPoint.proceed(params);//更新对象
                            redisTemplate.opsForValue().set(key,rs);
                        }
                    });
                }
            }
            return cacheRes;
        }
        if(redisTemplate.hasKey(key)){
            return null;
        }





//      确保同一时刻只有一个线程加载数据,防止缓存失效击穿访问数据库-----------------------------------------------------------------------（缓存击穿保护）
        Semaphore parameterLock = parameterLocks.getUnchecked(key);
        parameterLock.acquire();
        try {
            Object cacheNow  = redisTemplate.opsForValue().get(key);
            if (cacheNow == null) {
                if(limiter.tryAcquire()){ //防止1s内大片请求数据库--------------------------------------------------------------------(缓存雪崩保护)
                    cacheNow = joinPoint.proceed(params);//更新对象
                }else{
                    throw new ServiceException("缓存更新限流，请稍后再试");
                }
//                  缓存
                if(cacheNow!=null){
                    redisTemplate.opsForValue().set(key,cacheNow,cache.expire(), TimeUnit.SECONDS);
                }else{
//                  存储空值防止缓存穿透，空值的过期时间尽量短
                    redisTemplate.opsForValue().set(key,null, (int) (Math.random() * 10 + 5), TimeUnit.SECONDS);//---------------(缓存穿透保护)
                }
            }else{
                System.out.println("cache hit");
            }
            return cacheNow;

        }catch (Exception e){

        }finally {
            // 释放锁
            parameterLock.release(1);
        }


        return null;
    }

    public  boolean tryLock(String key,Object value, long expireTime) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
        return result != null && result;
    }

    private String getParamUniKey(Object[] params, String[] names) {
        if(params == null || params.length<=0){
            return "";
        }
        String uniKey = "";
        for (int i = 0; i < params.length; i++) {
            Object item = params[i];
            if(item == null){
                item="";
            }
            String name = names[i];
            if(item.getClass().isAssignableFrom(String.class)|| ClassUtils.isPrimitiveOrWrapper(item.getClass()) || item.getClass().isAssignableFrom(Date.class)){
                uniKey += (name+item);
            }else if(Collection.class.isAssignableFrom(item.getClass())){
                uniKey += (name+item);
            }else{
                throw new RuntimeException("请指定key值");
            }

        }
        return uniKey;
    }


    public static void main(String[] args){
        ArrayList<String> list = new ArrayList<>();
        list.add("233");
        Object we =list;
        System.out.println(we);
    }
}
