package top.anets.utils;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.modules.serviceMonitor.utils.DateUtils;
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
    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
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
     * 获取方法上的泛型
     * @param method
     * @param paramIndex
     * @return
     */
    public static Class getMethodParamGenericType(Method method , Integer paramIndex){
        Type[] genericParameterTypes=method.getGenericParameterTypes();
        Type genericType  = genericParameterTypes[paramIndex];
        if(genericType instanceof ParameterizedType){
            ParameterizedType parameterizedType=(ParameterizedType)genericType;
            Type[] types= parameterizedType.getActualTypeArguments();
            for (Type type:types){
                Class realType=(Class) type;
                return realType;
            }
        }

        return null;
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


//    public static void setFieldValue(Object object, String fieldName, Object newValue) {
//        try {
//            Field field = object.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//            field.set(object,newValue);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }




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


    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(Object obj, String propertyName)
    {
        Object object = obj;
        if(object instanceof Map){
            return (E)((Map)object).get(propertyName);
        }
        for (String name : StringUtils.split(propertyName, "."))
        {
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
        }
        return (E) object;
    }
    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     */
    public static <E> void invokeSetter(Object obj, String propertyName, E value)
    {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++)
        {
            if (i < names.length - 1)
            {
                String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
            }
            else
            {
                String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] { value });
            }
        }
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    @SuppressWarnings("unchecked")
    public static <E> E getFieldValue(final Object obj, final String fieldName)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null)
        {
            logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
            return null;
        }
        E result = null;
        try
        {
            result = (E) field.get(obj);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static <E> void setFieldValue(final Object obj, final String fieldName, final E value)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null)
        {
            // throw new IllegalArgumentException("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
            logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
            return;
        }
        try
        {
            field.set(obj, value);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常: {}", e.getMessage());
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
     * 同时匹配方法名+参数类型，
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                     final Object[] args)
    {
        if (obj == null || methodName == null)
        {
            return null;
        }
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null)
        {
            logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
            return null;
        }
        try
        {
            return (E) method.invoke(obj, args);
        }
        catch (Exception e)
        {
            String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethodByName(final Object obj, final String methodName, final Object[] args)
    {
        Method method = getAccessibleMethodByName(obj, methodName, args.length);
        if (method == null)
        {
            // 如果为空不报错，直接返回空。
            logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
            return null;
        }
        try
        {
            // 类型转换（将参数数据类型转换为目标方法参数类型）
            Class<?>[] cs = method.getParameterTypes();
            for (int i = 0; i < cs.length; i++)
            {
                if (args[i] != null && !args[i].getClass().equals(cs[i]))
                {
                    if (cs[i] == String.class)
                    {
                        args[i] = Convert.toStr(args[i]);
                        if (StringUtils.endsWith((String) args[i], ".0"))
                        {
                            args[i] = StringUtils.substringBefore((String) args[i], ".0");
                        }
                    }
                    else if (cs[i] == Integer.class)
                    {
                        args[i] = Convert.toInt(args[i]);
                    }
                    else if (cs[i] == Long.class)
                    {
                        args[i] = Convert.toLong(args[i]);
                    }
                    else if (cs[i] == Double.class)
                    {
                        args[i] = Convert.toDouble(args[i]);
                    }
                    else if (cs[i] == Float.class)
                    {
                        args[i] = Convert.toFloat(args[i]);
                    }
                    else if (cs[i] == Date.class)
                    {
                        if (args[i] instanceof String)
                        {
                            args[i] = DateUtils.parseDate(args[i]);
                        }
                        else
                        {
                            args[i] = DateUtil.getJavaDate((Double) args[i]);
                        }
                    }
                    else if (cs[i] == boolean.class || cs[i] == Boolean.class)
                    {
                        args[i] = Convert.toBool(args[i]);
                    }
                }
            }
            return (E) method.invoke(obj, args);
        }
        catch (Exception e)
        {
            String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName)
    {
        // 为空不报错。直接返回 null
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            try
            {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            }
            catch (NoSuchFieldException e)
            {
                continue;
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
                                             final Class<?>... parameterTypes)
    {
        // 为空不报错。直接返回 null
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            try
            {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            }
            catch (NoSuchMethodException e)
            {
                continue;
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 只匹配函数名。
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName, int argsNum)
    {
        // 为空不报错。直接返回 null
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum)
                {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method)
    {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible())
        {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field)
    {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible())
        {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * 如无法找到, 返回Object.class.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz)
    {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     */
    public static Class getClassGenricType(final Class clazz, final int index)
    {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType))
        {
            logger.debug(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0)
        {
            logger.debug("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class))
        {
            logger.debug(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    public static Class<?> getUserClass(Object instance)
    {
        if (instance == null)
        {
            throw new RuntimeException("Instance must not be null");
        }
        Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR))
        {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass))
            {
                return superClass;
            }
        }
        return clazz;

    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(String msg, Exception e)
    {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException)
        {
            return new IllegalArgumentException(msg, e);
        }
        else if (e instanceof InvocationTargetException)
        {
            return new RuntimeException(msg, ((InvocationTargetException) e).getTargetException());
        }
        return new RuntimeException(msg, e);
    }
}
