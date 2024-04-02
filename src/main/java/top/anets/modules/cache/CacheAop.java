package top.anets.modules.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
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
import org.ehcache.config.Eviction;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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
import top.anets.utils.SpElUtils;

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
 *
 * 前言:
 * SpringCache存在很多缺点
 * @Cacheable中 sync属性 用于解决缓存击穿问题，在去调用doInRedis方法时，会先获取锁，但是这个锁没有设定过期时间，当这个锁没有成功unlock时，后面的线程都将会阻塞等待这个锁，从而导致死锁
 * 只能设定统一的过期时间，可能会出现缓存雪崩问题
 * 而且不能自定义一些东西
 *
 *
 * 缓存击穿：单Key突然过期+高并发数据库               ----------      加锁只放行 + 自动刷新 + 预加载热点数据是实时调整
 * 缓存雪崩: 多热点key 同时过期 + 高并发  ；     ----------      过期时间错开 + 多级缓存() + 限流（同一个key限制流量）   + 自动刷新 +多级缓存（一级缓存的淘汰策略 和 访问后多久过期 不至于大片key同时失效）
 * 缓存穿透：单key 数据库无数据，不缓存 + 高并发  ----------     (存空值 + 布隆过滤)
 *
 *
 *
 *
 * 说下二级缓存:将频繁访问的数据将存入低延迟、高效的本地缓存中，避免大量的RPC（远程过程调用）和数据库查询
 * 引入本地缓存也可以进一步提高接口性能(比如循环调用缓存)，高流量时减轻对Redis的压力
 */
@Aspect
@Order(0)//确保比事务注解先执行，分布式锁在事务外
@Component
public class CacheAop {

    @Pointcut("@annotation(top.anets.modules.cache.Cache)")
    public void pointCut() {}
    private final RateLimiter limiter = RateLimiter.create(139); // 10 requests per second

    @Autowired
    private RedisTemplate redisTemplate;

    private final ExpressionParser parser = new SpelExpressionParser();
    com.github.benmanes.caffeine.cache.Cache<Object, Object> localCache = Caffeine.newBuilder()
            .maximumSize(100)
//           最后访问后间隔多久60s淘汰
            .expireAfterWrite(60, TimeUnit.SECONDS).build();
    @Autowired
    private RedissonClient redissonClient;

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
        Object cacheRes = getCacheFromStore(key);
        if(cacheRes!=null){
            if(cache.async()){
//              异步更新缓存
                String updateKey = "UpdateKeyPrefix" + key;
                Long ttl = redisTemplate.getExpire(updateKey);
                boolean lock = false;
                if (ttl == null || ttl < 0) {
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
//      其他线程等待，最好给一个等待时间，不然防止等待的线程太多而崩溃,最长等待5s，用户的接受时间
        RLock parameterLocks = redissonClient.getLock(key);
        boolean lock = parameterLocks.tryLock(5, TimeUnit.SECONDS);
        if(!lock){
            throw new ServiceException("缓存更新等待超时");
        }
        try {
            Object cacheNow  = getCacheFromStore(key);
            if (cacheNow == null) {
//              限流，防止突发流量对数据库的冲击，采用令牌桶吧
                if(limiter.tryAcquire()){ //防止1s内大片请求数据库--------------------------------------------------------------------(缓存雪崩保护)
                    cacheNow = joinPoint.proceed(params);//更新对象
                }else{
                    throw new ServiceException("缓存更新限流，请稍后再试");
                }
//                  缓存
                if(cacheNow!=null){
//                  在设定的过期时间基础上，补充随机时间，防止缓存雪崩
                    redisTemplate.opsForValue().set(key,cacheNow,cache.expire()+(int) (Math.random() * 10 + 5), TimeUnit.SECONDS);
                    localCache.put(key,cacheNow );
                }else{
//                  存储空值防止缓存穿透，空值的过期时间尽量短
                    redisTemplate.opsForValue().set(key,null, (int) (Math.random() * 10 + 5), TimeUnit.SECONDS);//---------------(缓存穿透保护)
                    localCache.put(key,cacheNow );
                }
            }else{
                System.out.println("cache hit");
            }
            return cacheNow;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 释放锁
            parameterLocks.unlock();
        }

        return null;
    }

    private Object getCacheFromStore(String key) {
        Object ifPresent = localCache.getIfPresent(key);
        if(ifPresent!=null){
            return ifPresent;
        }
        Object o = redisTemplate.opsForValue().get(key);
        return o;
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
