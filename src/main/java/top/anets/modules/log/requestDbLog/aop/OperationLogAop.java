package top.anets.modules.log.requestDbLog.aop;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.anets.exception.ServiceException;
import top.anets.modules.log.requestDbLog.entity.OperationRecord;
import top.anets.modules.log.requestDbLog.enums.BusinessEnum;
import top.anets.modules.log.requestDbLog.enums.OperationStatus;
import top.anets.modules.log.requestDbLog.service.OperationRecordService;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;
import top.anets.utils.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author ftm
 * @date 2023/1/31 0031 17:30
 */
@Order(2)
@Aspect
@Component
public class OperationLogAop {


    @Autowired
    private OperationRecordService operationRecordService;
    @Pointcut("@annotation(top.anets.modules.log.requestDbLog.aop.OperationLog)")
    public void myPoinitCut(){
    }
    private final ExpressionParser parser = new SpelExpressionParser();



    @Around("myPoinitCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Date beginTime = new Date();
        OperationRecord record = new OperationRecord();
        record.setOperationTime(beginTime);


        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog cache = method.getAnnotation(OperationLog.class);
        if(StringUtils.isNotBlank(cache.name())){
            record.setBusinessName(cache.name());
        }else if(cache.business() == null){
            throw new ServiceException("请填写具体的业务name或business");
        }else{
            record.setBusinessName(cache.business().getName());
        }
        record.setBusinessCode(cache.business().name());
        if(StringUtils.isNotBlank(cache.businessCode()) && cache.business() == BusinessEnum.Custom){
            record.setBusinessCode(cache.businessCode());
        }


        Object[] params = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        record.setRequestMethod(request.getMethod());
        record.setRequestUrl(request.getRequestURL().toString());
        record.setIp(ServletUtil.getCleintIp());
        if("GET".equals(request.getMethod())){
            record.setRequestParam(request.getQueryString());
        }else{
            String paramsStr = JSON.toJSONString(ServletUtil.getParams(request));
            record.setRequestParam(paramsStr);
        }
//        LoginUser loginUser = null;
//        try {
//            loginUser = SecurityUtils.getLoginUser();
//        }catch (Exception e){
//        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(parameterNames[i], params[i]);
        }
//        if(loginUser!=null){
//            record.setUserCode(loginUser.getUserId()+"");
//            record.setUserName(loginUser.getUsername());
//        }
        if(StringUtils.isNotBlank(cache.userCode())){
            // 解析SpEL表达式
            record.setUserCode(parser.parseExpression(cache.userCode()).getValue(context, String.class));
        }
        if(StringUtils.isNotBlank(cache.userName())){
            // 解析SpEL表达式
            record.setUserName(parser.parseExpression(cache.userName()).getValue(context, String.class));
        }
        if(StringUtils.isNotBlank(cache.organCode())){
            // 解析SpEL表达式
            record.setOrganCode(parser.parseExpression(cache.organCode()).getValue(context, String.class));
        }

        Object p = null;
        try{
           p = joinPoint.proceed(params);
           record.setResultRecord((String) request.getAttribute("LOG-RDATA"));
           record.setResultDesc((String) request.getAttribute("LOG-RDESC"));
           record.setStatus(OperationStatus.SUCCESS.name());
           record.setKillTime(System.currentTimeMillis() - beginTime.getTime());
           save(record);
        }catch (Exception e){
            e.printStackTrace();
            record.setStatus(OperationStatus.FAIL.name());
            record.setMsg(e.getMessage());
            record.setKillTime(System.currentTimeMillis() - beginTime.getTime());
            save(record);
            throw e;
        }


        return p;

    }

    private void save(OperationRecord record) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                operationRecordService.record(record);
            }
        });
    }


    public static void main(String [] args){



    }



}
