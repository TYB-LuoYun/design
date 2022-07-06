package top.anets.modules.limit2;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author LuoYun
 * @since 2022/7/6 14:04
 * 漏桶算法和令牌桶算法最明显的区别是令牌桶算法允许流量一定程度的突发。因为默认的令牌桶算法，取走token是不需要耗费时间的，也就是说，假设桶内有100个token时，那么可以瞬间允许100个请求通过。
令牌桶算法的原理也比较简单，我们可以理解成医院的挂号看病，只有拿到号以后才可以进行诊病。
漏桶算法思路很简单，我们把水比作是 请求，漏桶比作是 系统处理能力极限，水先进入到漏桶里，漏桶里的水按一定速率流出，当流出的速率小于流入的速率时，由于漏桶容量有限，后续进入的水直接溢出（拒绝请求），以此实现限流。
 */
@Slf4j
@Aspect
@Configuration
public class RedisLimitAspect {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisLimitAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(top.anets.modules.limit2.RedisLimit)")
    public Object around(ProceedingJoinPoint pjp){
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();
        RedisLimit annotation = method.getAnnotation(RedisLimit.class);
        LimitType limitType = annotation.limitType();

        String name = annotation.name();
        String key;

        int period = annotation.period();
        int count = annotation.count();



        switch (limitType){
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                if(StringUtils.isNotBlank(annotation.key())){
                    key = annotation.key();
                }else{
                    key = "limit-token-"+pjp.getTarget().getClass().getSimpleName()+"-"+method.getName()+"-"+pjp.getArgs().length;
                }
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(annotation.prefix(), key));
        try {
            String luaScript = buildLuaScript();
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
            Long number = redisTemplate.execute(redisScript, keys,  count,  period);
            log.info("Access try count is {} for name = {} and key = {}", number, name, key);
            if(number != null && number.intValue() == 1){
                return pjp.proceed();
            }
            throw new RuntimeException(annotation.msg());
        }catch (Throwable e){
//            if(e instanceof LimitException){
//                log.debug("令牌桶={}，获取令牌失败",key);
//                throw new LimitException(e.getLocalizedMessage());
//            }
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public String buildLuaScript(){
        return "redis.replicate_commands(); local listLen,time" +
                "\nlistLen = redis.call('LLEN', KEYS[1])" +
                // 不超过最大值，则直接写入时间
                "\nif listLen and tonumber(listLen) < tonumber(ARGV[1]) then" +
                "\nlocal a = redis.call('TIME');" +
                "\nredis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])" +
                "\nelse" +
                // 取出现存的最早的那个时间，和当前时间比较，看是小于时间间隔
                "\ntime = redis.call('LINDEX', KEYS[1], -1)" +
                "\nlocal a = redis.call('TIME');" +
                "\nif a[1]*1000000+a[2] - time < tonumber(ARGV[2])*1000000 then" +
                // 访问频率超过了限制，返回0表示失败
                "\nreturn 0;" +
                "\nelse" +
                "\nredis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])" +
                "\nredis.call('LTRIM', KEYS[1], 0, tonumber(ARGV[1])-1)" +
                "\nend" +
                "\nend" +
                "\nreturn 1;";
    }

    public String getIpAddress(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
