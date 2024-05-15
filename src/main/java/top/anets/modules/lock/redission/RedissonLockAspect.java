package top.anets.modules.lock.redission;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.anets.utils.SpelUtils;

import java.lang.reflect.Method;

/**
 * @author ftm
 * @date 2024-03-28 13:33
 */
@Slf4j
@Aspect
@Component
@Order(0)//确保比事务注解先执行，分布式锁在事务外
public class RedissonLockAspect {
    @Autowired
    private RedissonClient redissonClient;


    @Around("@annotation(top.anets.modules.lock.redission.RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        long start = System.currentTimeMillis();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        String prefix = StrUtil.isBlank(redissonLock.prefixKey()) ? SpelUtils.getMethodKey(method) : redissonLock.prefixKey();//默认方法限定名+注解排名（可能多个）
        String key = SpelUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());

        RLock lock = redissonClient.getLock(prefix + ":" + key);
        boolean lockSuccess = lock.tryLock(redissonLock.waitTime(), redissonLock.unit());
//        System.out.println("获取锁等待时间:"+(System.currentTimeMillis()-start));
        if (!lockSuccess) {
            throw new RuntimeException("请求太频繁了，请稍后再试哦~~");
        }
        try {
            return joinPoint.proceed();//执行锁内的代码逻辑
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("释放锁"+  key);
            }
        }
    }

}
