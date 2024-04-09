package top.anets.modules.log.appender;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.anets.exception.ServiceException;
import top.anets.log.MyRequestWrapper;
import top.anets.modules.log.enums.BusinessEnum;
import top.anets.modules.log.enums.ServiceEnum;
import top.anets.utils.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * @author ftm
 * @date 2024-04-08 17:43
 */
@Order(2)
@Aspect
@Slf4j
@Component
public class LogDbAop {
    @Value("${spring.application.name:DEFAULT}")
    private String applicationName;
    public static final ThreadLocal<NeedLog> NEED_LOG_THREAD_LOCAL = new ThreadLocal<>();

    @Pointcut("@annotation(top.anets.modules.log.appender.LogDb) ||  within(@top.anets.modules.log.appender.LogDb *)")
    public void myPoinitCut() {
    }

    @Around("myPoinitCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogDb cache = method.getAnnotation(LogDb.class);
        Class<?> aClass = joinPoint.getTarget().getClass();
        if(cache == null){
            cache = aClass.getAnnotation(LogDb.class);
        }
        /**
         * 获取到方法名
         */
        Object p = null;
        NeedLog needLog = new NeedLog();
        if(cache.business() != BusinessEnum.Custom){
            needLog.setBusiness(cache.business().name());
            needLog.setService(cache.business().getServiceEnum().name());
        }else if(StringUtils.isBlank(cache.name())){
            needLog.setBusiness(aClass.getSimpleName()+"-"+method.getName());
            needLog.setService(applicationName);
        }else{
            needLog.setBusiness(cache.name());
            needLog.setService(applicationName);
        }
        needLog.setMethod(method.getName());
        NEED_LOG_THREAD_LOCAL.set(needLog);
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attributes!=null&&attributes.getRequest()!=null){
                HttpServletRequest request = attributes.getRequest();
                if("GET".equals(request.getMethod())){
                    log.info("get url -> {} ({})", ServletUtil.getFullUrl(request),ServletUtil.getRemoteHost(request));
                }else{
                    String paramsStr = JSON.toJSONString(ServletUtil.getParams(request));
                    log.info("post url -> {} ({}), params -> {}", request.getRequestURL(),ServletUtil.getRemoteHost(request),paramsStr);
                }
                p = joinPoint.proceed(joinPoint.getArgs());
                if(p!=null){
                    log.info("out -> {}",JSON.toJSONString(p));
                }
            }else{
                p = joinPoint.proceed(joinPoint.getArgs());
            }


        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            log.error(sw.toString());
            throw new ServiceException(e.getMessage());
        }finally {
            NEED_LOG_THREAD_LOCAL.remove();
        }
        return p;

    }




}
