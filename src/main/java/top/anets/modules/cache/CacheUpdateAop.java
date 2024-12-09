package top.anets.modules.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.jna.platform.win32.WinDef;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import top.anets.exception.ServiceException;
import top.anets.modules.cache.service.RedisCaffeineCache;
import top.anets.modules.cache.util.NullValueUtil;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;
import top.anets.utils.SpelUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author ftm
 * @date 2023/3/17 0017 13:54
 * 缓存击穿：单Key突然过期+高并发               ----------      加锁只放行 + 自动刷新 + 预加载热点数据是实时调整
 * 缓存雪崩: 多热点key 同时过期 + 高并发  ；     ----------      过期时间错开 + 多级缓存() + 限流（同一个key限制流量）   + 自动刷新
 * 缓存穿透：单key 数据库无数据，不缓存 + 高并发  ----------     (存空值 + 布隆过滤)
 */
@Aspect
@Component
@ConditionalOnBean(RedisTemplate.class)
public class CacheUpdateAop {

    @Pointcut("@annotation(top.anets.modules.cache.CacheUpdate)")
    public void pointCut() {}
    private final RateLimiter limiter = RateLimiter.create(139); // 10 requests per second

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisCaffeineCache redisCaffeineCache;

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
        CacheUpdate cache = method.getAnnotation(CacheUpdate.class);
        String prefix = null;
        if(StringUtils.isBlank(cache.name())){
            throw new ServiceException("name缓存名不能为空");
        }else{
            prefix = cache.name();
        }
        String unikey  =null;
        if(StringUtils.isBlank(cache.key())){
            //      获取唯一参数签名
            unikey  = getParamUniKey(params,parameterNames);
        }else{
            // 解析SpEL表达式
            String keyExpression =cache.key();
            String value = SpelUtils.parseSmart(method, params, keyExpression);
            unikey =  value;
        }

        String key = prefix +"-"+ unikey;
        Object cacheNow  = joinPoint.proceed(params);//更新对象
        if(cache.delete() == true){
            redisCaffeineCache.evict(key );
           return cacheNow;
        }
        redisCaffeineCache.set(key, cacheNow);
        return cacheNow;
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
