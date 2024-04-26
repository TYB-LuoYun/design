package top.anets.modules.log.appender;

import com.alibaba.fastjson.JSON;
import top.anets.exception.ServiceException;
import top.anets.modules.log.requestDbLog.enums.BusinessEnum;
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
import top.anets.utils.ServletUtil;
import top.anets.utils.SpelUtils;

import javax.servlet.http.HttpServletRequest;
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
    public static  final ThreadLocal<NeedLog> NEED_LOG_THREAD_LOCAL = new ThreadLocal<>();

    @Pointcut("@annotation(top.anets.modules.log.appender.LogDb) ||  within(@top.anets.modules.log.appender.LogDb *)")
    public void myPoinitCut() {
    }

    @Around("myPoinitCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] params = joinPoint.getArgs();
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
        // 解析SpEL表达式
        String keyExpression =cache.classify();
        String value = SpelUtils.parseSmart(method, params, keyExpression);
        NeedLog needLog = new NeedLog();
        if(cache.businessEnum() != BusinessEnum.Custom){
            needLog.setBusiness(cache.businessEnum().name());
        }else if(StringUtils.isBlank(cache.business())){
            needLog.setBusiness(aClass.getSimpleName()+"-"+method.getName());
        }else{
            needLog.setBusiness(cache.business());
        }
        needLog.setClassify(value);
        needLog.setMethod(method.getName());
        NEED_LOG_THREAD_LOCAL.set(needLog);
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attributes!=null&&attributes.getRequest()!=null){
                HttpServletRequest request = attributes.getRequest();
                if("GET".equals(request.getMethod())){
                    log.info("Get请求 -> {} ({})", ServletUtil.getFullUrl(request),ServletUtil.getRemoteHost(request));
                }else{
                    String paramsStr = JSON.toJSONString(ServletUtil.getParams(request));
                    log.info("Post请求 -> {} ({}), 参数 -> {}", request.getRequestURL(),ServletUtil.getRemoteHost(request),paramsStr);
                }
                p = joinPoint.proceed(params);
                if(p!=null){
                    log.info("响应 -> {}",simpleText(JSON.toJSONString(p)));
                }
            }else{
                p = joinPoint.proceed(params);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(null,e);
            throw new ServiceException(e.getMessage());
        }finally {
            NEED_LOG_THREAD_LOCAL.remove();
        }
        return p;
    }

    private String simpleText(String inputString) {
        return inputString.length() <= 139 ? inputString : inputString.substring(0, 139) + "...";
    }

    public static void main(String[] args){
        String inputString = "yourStringHere";
        String result = inputString.length() <= 16 ? inputString : inputString.substring(0, 13) + "...";

    }



}
