package top.anets.support.mongodb.danamicdatasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ftm
 * @date 2023/3/9 0009 12:39
 */
@Aspect
@Component
public class MongodbAspect {

    private final static Logger _logger = LoggerFactory.getLogger(MongodbAspect.class);


    @Pointcut("@within(top.anets.support.mongodb.danamicdatasource.MongoDataSource)")
    public void mongodbDSourcePointCut() {}

    @Pointcut("@annotation(top.anets.support.mongodb.danamicdatasource.MongoDataSource)")
    public void mongodbDSourcePointCut2() {}



    @Around("mongodbDSourcePointCut() || mongodbDSourcePointCut2()")
    public Object getMongodbDynamicDS(ProceedingJoinPoint joinPoint) throws Throwable {
        MongoDB ds = getDSAnnocation(joinPoint).value();
        _logger.debug("MongodbAspect : getMongodbDynamicDS -> ds_key={}", ds.getValue());
        MongodbContextHolder.setMongoDbFactory(ds.getValue());
        try{
            return joinPoint.proceed();
        }finally {
            MongodbContextHolder.removeMongoDbFactory();
            _logger.debug("MongodbAspect : getMongodbDynamicDS -> ThreadLocal has deleted ds_key={}", ds.getValue());
        }
    }


    private MongoDataSource getDSAnnocation(ProceedingJoinPoint joinPoint) {
        Class<?> targetClazz = joinPoint.getTarget().getClass();
        MongoDataSource ds = targetClazz.getAnnotation(MongoDataSource.class);
        //	先判断类的注解，再判断方法注解
        if( ds!=null) {
            return ds;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(MongoDataSource.class);
    }

}