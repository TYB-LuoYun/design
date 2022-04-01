package top.anets.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*****************************************************
 * @fileName: PageHelper
 * @program: design
 * @module: pagehelper
 * @description: pagehelper
 * @author: tanyangbo
 * @create: 2022-03-24 14:13
 * @checker:
 * @document:
 * @editLog:
 * editDate  editBy  description 
 *****************************************************/
@Aspect
@Component
public class ReflectTestAop {
    @Pointcut("@annotation(top.anets.annotation.ReflectTest)")
    public void myPoinitCut(){}

    //在事件通知类型中申明returning即可获取返回值
    @AfterReturning(value = "myPoinitCut()", returning="returnValue")
    public void logMethodCall(JoinPoint jp, Object returnValue) throws Throwable {
        System.out.println("方法返回值为：" + returnValue);
    }


    @Around(value = "myPoinitCut()")
    public Object around(ProceedingJoinPoint jp) throws  Throwable,Exception{
        System.out.println("around before==================");
//      获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息
        MethodSignature ms = (MethodSignature) jp.getSignature();

//        获取被代理的对象
        Object target = jp.getTarget();

        //目标字节码对象
        Class<?> targetClass = jp.getTarget().getClass();


        //目标方法对象
        Method targetMethod = targetClass.getDeclaredMethod(ms.getName(), ms.getParameterTypes());

        //获取传参
        Object[] args = jp.getArgs();

        String name = targetClass.getName();
        System.out.println("className:"+name);

        Field[] fields = targetClass.getDeclaredFields();
        for ( int i = 0 ; i < fields. length ; i++){
            Field f = fields[i]; 
            f.setAccessible( true ); // 设置些属性是可以访问的
            Object value = f.get(target);
            String type = f.getType().toString(); // 得到此属性的类型
            System.out.println("field-"+i+":"+type+"/"+f.getName()+"/"+value);

            if (type.endsWith( "String" )) {//          或者通过  f.getName() 字段名+字段类型比较

                f.set(target, "15" ) ;        // 给属性设值
                System.out.println("field-change-"+i+":"+type+"/"+f.getName()+"/"+f.get(target));
            }
        }


        String msName = ms.getName();
        System.out.println("methodName:"+msName);
        //获取每个参数
        Class[] parameterTypes = ms.getParameterTypes();

        for (int i = 0;i < parameterTypes.length ; i++ ) {
            Class item = parameterTypes[i];
            System.out.println("params-"+i+":"+item.getName()+"/"+item.getSimpleName()+"/"+args[i]);
        }

        Object proceed = jp.proceed();
        System.out.println("around after ==================");
        return  proceed;
    }
}
