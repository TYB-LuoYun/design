package top.anets.utils;

import io.swagger.models.auth.In;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.modules.system.entity.Dict;
import top.anets.modules.system.entity.SysMenu;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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



    public static void main(String[] args){
        System.out.println(UUID.randomUUID());

    }
}
