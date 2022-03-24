package top.anets.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/*****************************************************
 * @fileName: Aspect
 * @program: ctrs
 * @module: aspect
 * @description: aspect
 * @author: tanyangbo
 * @create: 2022-03-23 08:50
 * @checker:
 * @document:
 * @editLog:
 * editDate  editBy  description 
 *****************************************************/
@Aspect
@Component
public class Aspects {

    @Pointcut("@annotation(top.anets.aop.MyAround)")
    public void myPoinitCut(){}


    @Around("myPoinitCut()")
    public Object around(ProceedingJoinPoint jp) throws  Throwable,Exception{
        System.out.println("around before==================");
        MethodSignature ms = (MethodSignature) jp.getSignature();
        //目标字节码对象
        Class<?> targetClass = jp.getTarget().getClass();
        //目标方法对象
        Method targetMethod = targetClass.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        //获取传参
        Object[] args = jp.getArgs();
        String name = targetClass.getName();
        System.out.println("className:"+name);
        String msName = ms.getName();
        System.out.println("msName:"+msName);
        Object proceed = jp.proceed();
        String params = new ObjectMapper().writeValueAsString(args);
        System.out.println("params:"+params);


        Class[] parameterTypes = ms.getParameterTypes();
        for (Class item:parameterTypes ) {
            System.out.println("参数类型:"+item.getName());
        }
        System.out.println("around after ==================");
        return  proceed;
    }
}
