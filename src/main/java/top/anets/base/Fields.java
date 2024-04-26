package top.anets.base;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author ftm
 * @Date 2023-01-30 10:25:18
 * @Description 字段名提取器
 */
public class Fields {
    /**
     * 使Function获取序列化能力
     */
    @FunctionalInterface
    public interface SFunction<T, R> extends Function<T, R>, Serializable {
    }

    /**
     * 字段名注解,声明表字段
     */
//    @Target(ElementType.FIELD)
//    @Retention(RetentionPolicy.RUNTIME)
//    public @interface TableField {
//        String value() default "";
//    }

    //默认配置
    static String defaultSplit = "";
    static Integer defaultToType = 0;

    /**
     * 获取实体类的字段名称(实体声明的字段名称)
     */
    public static <T> String getFieldName(SFunction<T, ?> fn) {
    return getFieldName(fn, defaultSplit);
    }

    /**
    * 获取实体类的字段名称(实体声明的字段名称)
    */
    public static <T> String name(SFunction<T, ?> fn) {
        return getFieldName(fn, defaultSplit);
    }


    public static <T> Field getField(SFunction<T, ?> fn  ) {
        String split = defaultSplit;
        Integer toType = defaultToType;
        SerializedLambda serializedLambda = getSerializedLambda(fn);
        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        Field field;
        Class<?> aClass = null;
        try {
            aClass = Class.forName(serializedLambda.getImplClass().replace("/", "."));
            field = aClass.getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            fieldName = capital(fieldName);
            try {
                field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
            } catch (NoSuchFieldException e1) {
                throw new RuntimeException(e1);
            } catch (ClassNotFoundException e1) {
                throw new RuntimeException(e1);
            }
        }

        return field;
    }
    /**
    * 获取实体类的字段名称
    *
    * @param split 分隔符，多个字母自定义分隔符
    */
    public static <T> String getFieldName(SFunction<T, ?> fn, String split) {
        return getFieldName(fn, split, defaultToType);
        }

        /**
        * 获取实体类的字段名称
        *
        * @param split  分隔符，多个字母自定义分隔符
        * @param toType 转换方式，多个字母以大小写方式返回 0.不做转换 1.大写 2.小写
        */
        public static <T> String getFieldName(SFunction<T, ?> fn, String split, Integer toType) {
            SerializedLambda serializedLambda = getSerializedLambda(fn);

            // 从lambda信息取出method、field、class等
            String fieldName = serializedLambda.getImplMethodName().substring("get".length());
            fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
            Field field;
            Class<?> aClass = null;
            try {
             aClass = Class.forName(serializedLambda.getImplClass().replace("/", "."));
            field = aClass.getDeclaredField(fieldName);
            } catch (ClassNotFoundException | NoSuchFieldException e) {
            fieldName = capital(fieldName);
            try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
            } catch (NoSuchFieldException e1) {
            throw new RuntimeException(e1);
            } catch (ClassNotFoundException e1) {
            throw new RuntimeException(e1);
            }
            }

            // 从field取出字段名，可以根据实际情况调整
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && tableField.value().length() > 0  ) {
                return tableField.value();
            }
//            else if(aClass!=null && aClass.getAnnotation(Document.class)!=null){
//                return fieldName;
//            }
            else{
                return upperCharToUnderLine(fieldName);
//            //0.不做转换 1.大写 2.小写
//            switch (toType) {
//            case 1:
//            return fieldName.replaceAll("[A-Z]", split + "$0").toUpperCase();
//            case 2:
//            return fieldName.replaceAll("[A-Z]", split + "$0").toLowerCase();
//            default:
//            return fieldName.replaceAll("[A-Z]", split + "$0");
//            }

            }

            }


    public static String upperCharToUnderLine(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

            private static <T> SerializedLambda getSerializedLambda(SFunction<T, ?> fn) {
                // 从function取出序列化方法
                Method writeReplaceMethod;
                try {
                writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
                } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
                }

                // 从序列化方法取出序列化的lambda信息
                boolean isAccessible = writeReplaceMethod.isAccessible();
                writeReplaceMethod.setAccessible(true);
                SerializedLambda serializedLambda;
                try {
                serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
                } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
                }
                writeReplaceMethod.setAccessible(isAccessible);
                return serializedLambda;
                }


                //定义首部大写字母方法
                public static String capital(String s) {
                    if(Character.isUpperCase(s.charAt(0)))
                        return s;
                    else
                        return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
                    }
                }
