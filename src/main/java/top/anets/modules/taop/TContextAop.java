//package top.anets.modules.taop;
//
//import com.citic.bizframe.context.IContext;
//import com.citic.bizframe.context.impl.BaseContext;
//import com.citic.bizframe.dataset.DatasetService;
//import com.citic.bizframe.interfaces.dataset.IDataset;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.ext.ArrayList;
//import java.ext.Arrays;
//import java.ext.List;
//
///*****************************************************
// * @fileName: TcontextAop
// * @program: CTRS
// * @module:
// * @description:
// * @author: tanyangbo
// * @create: 2022-03-31 13:52
// * @checker:
// * @document:
// * @editLog:
// * editDate  editBy  description
// *****************************************************/
//@Aspect
//@Component
//public class TContextAop {
//    @Pointcut("@within(TAop)")
//    public void myPoinitCut(){
//    }
//
//
//
//    @Around("myPoinitCut()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object[] params = joinPoint.getArgs();
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Annotation[][] annotations = method.getParameterAnnotations();
//        for (int i = 0; i < annotations.length; i++) {
//            Object param = params[i];
//            Annotation[] paramAnn = annotations[i];
//            if(param == null || paramAnn.length == 0){
//                continue;
//            }
//            for (Annotation annotation : paramAnn) {
//                if(annotation.annotationType().equals(TContext.class)){
//                    Object target = joinPoint.getTarget();
//                    Class<?> targetClass = target.getClass();
//                    Field[] fields = getAllDeclaredFields(targetClass);
//                    Object contextValue = null;
//                    for ( int j = 0 ; j < fields. length ; j++){
//                        Field f = fields[j];
//                        f.setAccessible( true );
//                        String type = f.getType().toString();
//                        if ("request".equals(f.getName())) {
//                            Object value = null;
//                            try {
//                                value = f.get(target);
//                            } catch (IllegalAccessException e) {
//                                e.printStackTrace();
//                            }
//                            contextValue = value;
//                        }
//                    }
//                    if(contextValue == null){
//                        throw new RuntimeException("controller layer can not extends BaseController");
//                    }
//                    IDataset dataset = DatasetService.getDefaultInstance().getDataset((HttpServletRequest) contextValue);
//                    IContext context = new BaseContext(dataset);
//                    params[i] = context;
//                    break;
//                }
//            }
//        }
//        Object p = null;
//        try {
//            p = joinPoint.proceed(params);
//        }catch (Throwable e){
//            System.out.println(e);
//            throw e;
//        }
//        return p;
//
//    }
//
//
//    /**x
//     * 获取本类及其父类的属性的方法
//     * @param clazz 当前类对象
//     * @return 字段数组
//     */
//    private static Field[] getAllDeclaredFields(Class<?> clazz) {
//        List<Field> fieldList = new ArrayList<>();
//        while (clazz != null){
//            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
//            clazz = clazz.getSuperclass();
//        }
//        Field[] fields = new Field[fieldList.size()];
//        return fieldList.toArray(fields);
//    }
//}
