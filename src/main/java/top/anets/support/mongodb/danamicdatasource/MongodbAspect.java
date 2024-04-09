package top.anets.support.mongodb.danamicdatasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.MongoDatabaseFactory;
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
        // 获取方法签名
        MongoDB ds = getDSAnnocation(joinPoint).value();
        _logger.debug("MongodbAspect : getMongodbDynamicDS -> ds_key={}", ds.getValue());
//       获取当前线程的db
        MongoDatabaseFactory origin = MongodbContextHolder.getMongoDbFactory();
        MongodbContextHolder.setMongoDbFactory(ds.getValue());
        try{
            Object proceed = joinPoint.proceed();
            return proceed;
        }finally {
            MongoDatabaseFactory now = MongodbContextHolder.getMongoDbFactory();
            if(origin != null && origin!= now){
//              说明在内部被移除，移除的可能性只有内部方法调用了其他方法进入了切面
                MongodbContextHolder.setMongoDbFactory(origin);
                _logger.debug("MongodbAspect : getMongodbDynamicDS -> ThreadLocal has recovered");
            }else if(origin == now){
//              说明在内部方法中还是使用了同一个库，继续使用
                _logger.debug("MongodbAspect : getMongodbDynamicDS -> ThreadLocal continue use ds_key={}", ds.getValue());
            }else{
//              如果origin为null的话说明是一个新线程哈
                MongodbContextHolder.removeMongoDbFactory();
                _logger.debug("MongodbAspect : getMongodbDynamicDS -> ThreadLocal has deleted ds_key={}", ds.getValue());
            }
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