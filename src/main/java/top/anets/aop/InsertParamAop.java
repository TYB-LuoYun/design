package top.anets.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.anets.annotation.InsertParam;
import top.anets.controller.Vo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*****************************************************
 * @fileName: InsertParamAop
 * @program: design
 * @module:
 * @description:
 * @author: tanyangbo
 * @create: 2022-03-30 15:15
 * @checker:
 * @document:
 * @editLog:
 * editDate  editBy  description 
 *****************************************************/
@Aspect
@Component
public class InsertParamAop {
    @Pointcut("@annotation(top.anets.annotation.ReflectTest)")
    public void myPoinitCut(){
    }

    /**
     * 2种方式修改参数值
     * @param joinPoint
     */
    @Around("myPoinitCut()")
    public Object around(ProceedingJoinPoint joinPoint){

        Object[] params = joinPoint.getArgs();

        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取每个参数
        Class[] parameterTypes = signature.getParameterTypes();

        //参数注解，1维是参数，2维是注解
        Annotation[][] annotations = method.getParameterAnnotations();
        Object proceed = null;
        for (int i = 0; i < annotations.length; i++) {
            Object param = params[i];

            Annotation[] paramAnn = annotations[i];
            //参数为空，直接下一个参数
            if(param == null || paramAnn.length == 0){
                continue;
            }
            for (Annotation annotation : paramAnn) {
                //这里判断当前注解是否为Test.class
                if(annotation.annotationType().equals(InsertParam.class)){
                    Class paramClass = parameterTypes[i];
                    System.out.println("注解上的参数："+paramClass+"/"+param);
                    System.out.println("修改注解上的参数");
                    System.out.println("change value");
                    //效验改参数 验证一次并退出改注解
//                    Field field = String.class.getDeclaredField("value"); //value是固定值 就是这样写
                    Field field = null;
                    try {
                        field = paramClass.getDeclaredField("name");
                        field.setAccessible(true);
                        field.set(param,"sddd"); //第一种方式修改对象里面的属性
                        System.out.println("注解上的参数："+paramClass+"/"+params[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            Vo vo = new Vo();
            vo.setAge(56);
            params[i] = vo; //第二种方式直接修改对象

            try {
                 proceed = joinPoint.proceed(params);//更新对象
                System.out.println("注解上的参数："+params[i].getClass()+"/"+params[i]);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
        return proceed;

    }

    /**
     * 获取本类及其父类的属性的方法
     * @param clazz 当前类对象
     * @return 字段数组
     */
    private static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }



}
