package top.anets.modules.limit;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author LuoYun
 * @since 2022/7/6 11:15
 */
@Slf4j
@Aspect
@Component
public class RequestLimitAop {
//    @Before("within(@org.springframework.web.bind.annotation.RequestMapping * || @javax.ws.rs.Path *) && @annotation(limit)")
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Around("within(@top.anets.modules.limit.TAop *) && @annotation(limit)")
    public Object requestLimit(ProceedingJoinPoint joinPoint, RequestLimit limit)  throws Throwable {
        Object p = null;
        try {
            /**
             * 获取request
             */
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String ip = HttpUtil.getIpByRequest(request);
            String url = request.getRequestURL().toString();
            String key = "ifOvertimes".concat(url).concat(ip);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            //访问次数加一
            int count = redisTemplate.opsForValue().increment(key, 1).intValue();//increment(K key, double delta),以增量的方式将double值存储在变量中。
            //如果是第一次，则设置过期时间
            if (count == 1) {
                redisTemplate.expire(key, limit.time(), TimeUnit.SECONDS);
            }
            if (count > limit.maxCount()) {
                request.setAttribute("ifOvertimes", "true");
                log.error(df.format(new Date()) +", ip :" +ip+",每 "+limit.time()  +" 秒访问接口:"+url+", "+count+" 次,拦截请求。");
                throw new RuntimeException("被限流");
            }
            p = joinPoint.proceed();

        }  catch (Throwable e) {
            e.printStackTrace();
            throw  e;
        }
        return p;
    }
}
