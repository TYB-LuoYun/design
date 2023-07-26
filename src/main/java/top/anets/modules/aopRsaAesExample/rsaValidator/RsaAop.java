package top.anets.modules.aopRsaAesExample.rsaValidator;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.anets.utils.RSAUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author LuoYun
 * @since 2022/6/21 9:45
 */
@Aspect
@Component
public class RsaAop {
    private String publicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSUmOXyQmYYSnZacp0btvAZCOvCNPtzixAp7eJmzmAG4mgy/VgrY/s1BDLh9qTNHIRWXepUtwMrf1kYul/A45qE/2oxIbeeq4238YDWQ7ModOVXR9ytEHsT0jpCFvoYfYXYZnnoWRrLIBylQeXzqxbLDxxBxGCs4AjoRKh5S7nNQIDAQAB";
//    服务商返回： 公钥加密内容，私钥解密内容，保证内容的安全性。
//    调用方 ： 私钥加密签章，公钥解密签章，保证签章来自特定的人
//     RSA+AES混合加密 ：  RSA公钥对AES密钥加密 ，签名 ，RSA私钥对AES密钥解密 ，验签  ，AES对内容进行加密  (加密>签名>解密>验签)
//     AES加密不会 限制加密内容长度，RSA 加密安全，但长度限制性能低，所以可以使用 RSA分段加密 和 RSA+AES 混合加密

    @Pointcut("@within(Rsa)||@annotation(Rsa)")
    public void myPoinitCut(){
    }


    @Around("myPoinitCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        System.out.println("RSA参数解析----------------------------------------------------");
        Map<String, Object> map = getParams(request);


        //参数判空（略）
        if(map.get("timestamp") == null){
            throw new RuntimeException("timestamp字段不能为null");
        }
//        if(map.get("appId") == null){
//            throw new RuntimeException("appId字段不能为null");
//        }

        //appId校验（略）


        //本条请求的合法性校验《唯一不重复请求；时间合理》（略）

        //20分钟不合法
        Long time = (Long) map.get("timestamp");
        long timeSpace = Math.abs(System.currentTimeMillis() - time);
        if (timeSpace > 120 * 60 * 1000) {
            throw new RuntimeException("时间校验不合法或者超过20分钟，请检查是否传输正确,当前时间:"+System.currentTimeMillis());
        }
        //验签
        String sign = (String) map.get("sign");
        System.out.println("接收到sign:"+sign);
        if(StringUtils.isBlank(sign)){
            throw new RuntimeException("没有签名");
        }
        map.remove("sign");
        boolean verify = RSAUtil.verify(RSAUtil.sortAndGroupStringParam(map), sign, publicKey);
        if(!verify){
            throw new RuntimeException("验签失败");
        }
        Object[] params = joinPoint.getArgs();
        Object p = joinPoint.proceed(params);
        return p;

    }


    /**
     * 获取本类及其父类的属性的方法
     * @param clazz 当前类对象
     * @return 字段数组
     */
    private static Field[] getAllDeclaredFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }

    private Map<String,Object> getParams(HttpServletRequest request) {
        Map combineResultMap = new HashMap();
        Map<String,Object> map = new HashMap<>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        combineResultMap.putAll(map);
        if(!"POST".equals(request.getMethod())){
            return combineResultMap;
        }
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while(line != null){
                builder.append(line);
                line = reader.readLine();
            }
            String reqBody = builder.toString();
            Map<String,Object> map2 = JSON.parseObject(reqBody, Map.class);
            if(map2!=null){
                combineResultMap.putAll(map2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return combineResultMap;
    }

    /**
     * 修改header信息，key-value键值对儿加入到header中
     * @param request
     * @param key
     * @param value
     */
    private void reflectSetparam(HttpServletRequest request, String key, String value){
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        System.out.println("request实现类="+requestClass.getName());
        try {
//            Field request1 = requestClass.getDeclaredField("request");
//          如果自定义了request实现类的话需要自行修改
            Field request1 = requestClass.getDeclaredField("orgRequest");

            request1.setAccessible(true);
            Object o = request1.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("exchange");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            System.out.println("exchange实现类="+o1.getClass().getName());
//            Field headers = o1.getClass().getDeclaredField("headers");
            Field headers = o1.getClass().getDeclaredField("requestHeaders");
            headers.setAccessible(true);
//            MimeHeaders o2 = (MimeHeaders)headers.get(o1);
//            HeaderMap o2 = (HeaderMap)headers.get(o1);
//            o2.addValue(key).setString(value);
//            o2.addFirst(new HttpString(key),value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
