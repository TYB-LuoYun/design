package top.anets.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.swagger.models.auth.In;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.modules.system.entity.Dict;
import top.anets.modules.system.entity.SysMenu;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ftm
 * @date 2022/10/21 0021 18:09
 */
public class ReflectUtil {
    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getInterfaceT(Object o, int index) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);

    }


    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getFunctionInterfaceT(Object o, int index) {
        Type r1Type = getLambdaParameterType(o, index);
        return  (Class<?>)(r1Type);

    }


    /**
     *
     * @param object
     * @param index
     * @return
     */
    public static Type  getLambdaParameterType(Object object,int index) {
        Type type = object.getClass().getGenericInterfaces()[0];
        Type r1Clazz;
        if(type instanceof ParameterizedType) {
            return  ((ParameterizedType) type).getActualTypeArguments()[1];
        } else if (object.getClass().isSynthetic())
        {
            try {
                return getConsumerLambdaParameterType(object, index);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("not ParameterizedType");
        }
        return null;
    }


    public static Class<?> getConsumerLambdaParameterType(Object consumer,int index) throws ReflectiveOperationException
    {
        Class<?> consumerClass = consumer.getClass();
        Object constantPool = invoke(consumerClass, "getConstantPool");
        for (int i = (int) invoke(constantPool, "getSize") - 1; i >= 0; --i)
        {
            try
            {
                Member member = (Member) invoke(constantPool, "getMethodAt", i);
                if (member instanceof Method && member.getDeclaringClass() != Object.class)
                {
                    return ((Method) member).getParameterTypes()[index];
                }
            }
            catch (Exception ignored)
            {
                // ignored
            }
        }
        throw new NoSuchMethodException();
    }


    public static Object invoke(Object obj, String methodName, Object... args) throws ReflectiveOperationException
    {
        Field overrideField = AccessibleObject.class.getDeclaredField("override");
        overrideField.setAccessible(true);
        Method targetMethod = getMethod(obj.getClass(), methodName);
        overrideField.set(targetMethod, true);
        return targetMethod.invoke(obj, args);
    }


    public static Method getMethod(Class<?> objClass, String methodName) throws NoSuchMethodException
    {
        for (Method method : objClass.getDeclaredMethods())
        {
            if (methodName.equals(method.getName()))
            {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }


    /**
     * 获取类上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getClassT(Object o, int index) {
        Type type = o.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actType = parameterizedType.getActualTypeArguments()[index];
            return checkType(actType, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }


    /**
     * 根据类路径获取类
     * @param args
     */
    public static Class<?> getClazz(String clazzName){
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取继承父类的泛型Class对象
     * @Description: abstTypeTest
     */
    public static Class getAbstActualType(Class objClass)
    {
        // 获取实例对象的抽象父类
        Type superclass = objClass.getGenericSuperclass();
        // 转化抽象父类为参数类
        ParameterizedType pType=(ParameterizedType) superclass;
        // 获取参数类的泛型类型数组
        Type[] types = pType.getActualTypeArguments();
        // 因为我们抽象类中泛型参数只有一个,所以泛型类型数组第一个就是我们的泛型类型,Class是Type的子类
        Class clazz=(Class) types[0];
        return clazz;
    }

    /**
     * 获取接口父类的泛型对象
     * @Description: interTypeTest
     */
    public static Class getInterActualType(Class objClass)
    {
        // 获取实例对象父接口
        Type[] interTypes = objClass.getGenericInterfaces();
        // 转化抽象父类为参数类,因为这里我们只有一个父接口,所以下表0位我们需要的
        ParameterizedType pType=(ParameterizedType) interTypes[0];
        // 获取父接口的参数类型数组
        Type[] types = pType.getActualTypeArguments();
        // 因为我们父接口中泛型参数只有一个,所以泛型类型数组第一个就是我们的泛型类型,Class是Type的子类
        Class clazz=(Class) types[0];
        return clazz;
    }


    public static void setFieldValue(Object object, String fieldName, Object newValue) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object,newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void copyPropertiesToNullFields(Object source, Object target)  {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();
        try {
            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true);
                Object sourceValue = sourceField.get(source);
                for (Field targetField : targetFields) {
                    targetField.setAccessible(true);
                    if (sourceField.getName().equals(targetField.getName()) && ObjectUtils.isEmpty(targetField.get(target))) {
                        targetField.set(target, sourceValue);
                        break;
                    }
                }
            }
        }catch (Exception e){

        }
    }


    /**
     * 找com.dicomclub.paysystem.module.**.entity
     * 包下的所有类
     * @param packageName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> find(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pattern = "classpath*:/" + packageName.replace('.', '/') + "/*.class";
        Resource[] resources = resolver.getResources(pattern);
        SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
        for (Resource resource : resources) {
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
            String className = metadataReader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(className);
            classes.add(clazz);
        }
        return classes;
    }

    public static boolean isCommonType(Object obj) {
        return isString(obj) ||isPrimitive(obj) ||
                isWrapperType(obj) ||
                isDate(obj);
    }

    public static boolean isPrimitive(Object obj) {
        return obj instanceof Boolean ||
                obj instanceof Character ||
                obj instanceof Byte ||
                obj instanceof Short ||
                obj instanceof Integer ||
                obj instanceof Long ||
                obj instanceof Float ||
                obj instanceof Double;
    }

    public static boolean isWrapperType(Object obj) {
        return obj instanceof Boolean ||
                obj instanceof Character ||
                obj instanceof Byte ||
                obj instanceof Short ||
                obj instanceof Integer ||
                obj instanceof Long ||
                obj instanceof Float ||
                obj instanceof Double;
    }

    public static boolean isString(Object obj) {
        return obj instanceof String;
    }

    public static boolean isDate(Object obj) {
        return obj instanceof Date;
    }



    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        System.out.println(UUID.randomUUID());
        Class<?> clazz = getClazz("top.anets.modules.system.mapper.DictMapper");
        Class<?> classT = getInterActualType(clazz);
        System.out.println(classT);
        Field[] fields = classT.getDeclaredFields();
        for(Field fie : fields){
            if(!fie.isAccessible()){
                fie.setAccessible(true);
            }
            TableField annotation = fie.getAnnotation(TableField.class);
            if(annotation!=null){
                System.out.println(annotation.value());
            }
        }

//
//        String str ="The error may involve com.ruoyi.module.sys.mapper.OrganinfoMapper.updateById-Inline";
//        Pattern pattern = Pattern.compile("(The error may involve )(.*)(Mapper.updateById-Inline)");
//        Matcher matcher = pattern.matcher(str);
//        while (matcher.find()) {
//            System.out.println(matcher.group());
//            System.out.println(matcher.group(0));
//            System.out.println(matcher.group(1));
//            System.out.println(matcher.group(2));
//        }
//        String error ="org.springframework.dao.DataIntegrityViolationException: \n" +
//                "### Error updating database.  Cause: com.mysql.cj.jdbc.exception.MysqlDataTruncation: Data truncation: Data too long for column 'Description' at row 1\n" +
//                "### The error may exist in com/ruoyi/module/sys/mapper/OrganinfoMapper.java (best guess)\n" +
//                "### The error may involve com.ruoyi.module.sys.mapper.OrganinfoMapper.updateById-Inline\n" +
//                "### The error occurred while setting parameters";
//        List<String> strByLikeMatch = RegexUtil.findStrByLikeMatch("The error may involve ", "Mapper", error);
//        strByLikeMatch.forEach(item->{
//            System.out.println(item);
//        });

    }
}
