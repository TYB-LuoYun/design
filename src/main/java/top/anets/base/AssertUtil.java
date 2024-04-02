//package top.anets.base;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.ReflectUtil;
//import io.swagger.annotations.ApiModelProperty;
//import org.apache.commons.lang3.StringUtils;
//import org.hibernate.validator.HibernateValidator;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import java.lang.reflect.Field;
//import java.util.*;
//
///**
// * 校验工具类
// */
//public class AssertUtil {
//
//    /**
//     * 校验到失败就结束
//     */
//    private static Validator failFastValidator = Validation.byProvider(HibernateValidator.class)
//            .configure()
//            .failFast(true)
//            .buildValidatorFactory().getValidator();
//
//    /**
//     * 全部校验
//     */
//    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//
//
//    /**
//     * 注解验证参数(校验到失败就结束)
//     *
//     * @param obj
//     */
//    public static <T> void fastFailValidate(T obj) {
//        Set<ConstraintViolation<T>> constraintViolations = failFastValidator.validate(obj);
//        if (constraintViolations.size() > 0) {
//            throwException("参数校验失败:"+ constraintViolations.iterator().next().getMessage());
//        }
//    }
//
//    /**
//     * 注解验证参数(全部校验,抛出异常)
//     *
//     * @param obj
//     */
//    public static <T> void allCheckValidateThrow(T obj) {
//        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
//        if (constraintViolations.size() > 0) {
//            StringBuilder errorMsg = new StringBuilder();
//            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
//            while (iterator.hasNext()) {
//                ConstraintViolation<T> violation = iterator.next();
//                //拼接异常信息
//                errorMsg.append(violation.getPropertyPath().toString()).append(":").append(violation.getMessage()).append(",");
//            }
//            //去掉最后一个逗号
//            throwException("参数校验失败:"+ errorMsg.toString().substring(0, errorMsg.length() - 1));
//        }
//    }
//
//
//    /**
//     * 注解验证参数(全部校验,返回异常信息集合)
//     *
//     * @param obj
//     */
//    public static <T> Map<String, String> allCheckValidate(T obj) {
//        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
//        if (constraintViolations.size() > 0) {
//            Map<String, String> errorMessages = new HashMap<>();
//            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
//            while (iterator.hasNext()) {
//                ConstraintViolation<T> violation = iterator.next();
//                errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
//            }
//            return errorMessages;
//        }
//        return new HashMap<>();
//    }
//
//    //如果不是true，则抛异常
//    public static void isTrue(boolean expression, String msg) {
//        if (!expression) {
//            throwException(msg);
//        }
//    }
//
//
//
//    //如果是true，则抛异常
//    public static void isFalse(boolean expression, String msg) {
//        if (expression) {
//            throwException(msg);
//        }
//    }
//
//
//    //如果不是非空对象，则抛异常
//    public static void isNotEmpty(Object obj, String msg) {
//        if (isEmpty(obj)) {
//            throwException(msg);
//        }
//    }
//
//
//
//    public static <T>  void isNotEmpty(Object obj, Fields.SFunction<T, ?>... field) {
////      获取对象的字段值
//        Arrays.stream(field).forEach(item->{
//            Field fieldz = Fields.getField(item);
//            Object fieldValue = ReflectUtil.getFieldValue(obj,fieldz);
//            if (isEmpty(fieldValue)) {
//                ApiModelProperty annotation = fieldz.getAnnotation(ApiModelProperty.class);
//                if(annotation!=null&&StringUtils.isNotBlank(annotation.value())){
//                    throwException(fieldz.getName()+"("+annotation.value()+")不能为空");
//                }else{
//                    throwException(fieldz.getName()+"不能为空");
//                }
//            }
//        });
//
//    }
//
//
//
//    //如果不是非空对象，则抛异常
//    public static void isNotBlank(String str, String msg) {
//        if (StringUtils.isBlank(str)) {
//            throwException(msg);
//        }
//    }
//
//
//
//    //如果不是非空对象，则抛异常
//    public static void isEmpty(Object obj, String msg) {
//        if (!isEmpty(obj)) {
//            throwException(msg);
//        }
//    }
//
//    public static void equal(Object o1, Object o2, String msg) {
//        if (!ObjectUtil.equal(o1, o2)) {
//            throwException(msg);
//        }
//    }
//
//    public static void notEqual(Object o1, Object o2, String msg) {
//        if (ObjectUtil.equal(o1, o2)) {
//            throwException(msg);
//        }
//    }
//
//    private static boolean isEmpty(Object obj) {
//        return ObjectUtil.isEmpty(obj);
//    }
//
//    private static void throwException(String msg) {
//        throw new IllegalArgumentException(msg);
//    }
//
//
//}
