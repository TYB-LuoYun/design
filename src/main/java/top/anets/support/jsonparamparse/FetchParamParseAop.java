//package top.anets.support.jsonparamparse;
//
//import org.apache.commons.lang.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import top.anets.utils.ServletUtil;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.util.Map;
//
///**
// * @author ftm
// * @date 2023/3/17 0017 13:54
// */
//@Aspect
//@Component
//public class FetchParamParseAop {
//    @Pointcut("(@annotation(org.springframework.web.bind.annotation.PostMapping)||@annotation(org.springframework.web.bind.annotation.RequestMapping))"
//            + "&& (execution(* *(.., @com.dicomclub.cdr.support.aops.jsonparamparse.FetchParam (*), ..))||@annotation(com.dicomclub.cdr.support.aops.jsonparamparse.FetchParam))")
//    public void pointCut() {}
//
//
//    /**
//     * 2种方式修改参数值
//     * @param joinPoint
//     */
//    @Around("pointCut()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object[] params = joinPoint.getArgs();
//        //获取方法，此处可将signature强转为MethodSignature
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        //获取每个参数
//        Class[] parameterTypes = signature.getParameterTypes();
//        // 获取目标方法的参数名数组
//        String[] parameterNames = signature.getParameterNames();
//        //参数注解，1维是参数，2维是注解
//        Annotation[][] annotations = method.getParameterAnnotations();
////      获取请求参数值
//        Map<String, Object> map = ServletUtil.getParams();
//        Object proceed = null;
//        for (int i = 0; i < annotations.length; i++) {
//            Annotation[] paramAnn = annotations[i];
//            Class paramClass = parameterTypes[i];
//            String paramName = parameterNames[i];
//            for (Annotation annotation : paramAnn) {
//                if(annotation.annotationType().equals(FetchParam.class)){
//                    FetchParam jsonParam = (FetchParam) annotation;
//                    if(StringUtils.isNotBlank(jsonParam.value())){
//                        paramName = jsonParam.value();
//                    }
//                }
//            }
//            try {
//                if(map.get(paramName)!=null){
//                    params[i] =map.get(paramName) ; //第二种方式直接修改对象
//                }
//            }catch (Exception e){
//
//            }
//        }
//         proceed = joinPoint.proceed(params);//更新对象
//        return proceed;
//
//    }
//}
