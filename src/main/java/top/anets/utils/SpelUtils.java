package top.anets.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Description: spring el表达式解析
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-04-22
 */
public class SpelUtils {
    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public static String parseSpEl(Method method, Object[] args, String spEl) {
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});//解析参数名
        EvaluationContext context = new StandardEvaluationContext();//el解析需要的上下文对象
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);//所有参数都作为原材料扔进去
        }
        Expression expression = parser.parseExpression(spEl);
        return expression.getValue(context, String.class);
    }


    public static String parseSmart(Method method, Object[] args, String keyExpression) {
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});//解析参数名
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        if(!keyExpression.startsWith("#")){
            return keyExpression;
        }

        String value = null;
        try {
            value = parser.parseExpression(keyExpression).getValue(context, String.class);
        }catch (Exception e){
        }
        return smartParse(keyExpression,context,null) ;
    }

    private static String smartParse(String keyExpression, EvaluationContext context, Object prevObj) {
        if(StringUtils.isBlank(keyExpression)){
            return (String) prevObj;
        }
        if(keyExpression.startsWith("#")){
            keyExpression = keyExpression.substring(1);
        }
        if(prevObj == null){//说明不知道值
            int first = keyExpression.indexOf(".");
            if(first<0){//说明是最后一个
                return (String) context.lookupVariable(keyExpression);
            }
            String now = keyExpression.substring(0, first);
            prevObj = context.lookupVariable(now);
            if(prevObj == null){//走不下去
                return null;
            }
            keyExpression =  keyExpression.substring(first+1);
        }
        int first = keyExpression.indexOf(".");
        if(first<0){//说明是最后一个
            return ReflectUtil.invokeGetter(prevObj, keyExpression);
        }
        String now = keyExpression.substring(0, first);
        keyExpression =  keyExpression.substring(first+1);
        prevObj =ReflectUtil.invokeGetter(prevObj, now);
        return smartParse(keyExpression, context, prevObj);
    }

    public static String getMethodKey(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }
}
