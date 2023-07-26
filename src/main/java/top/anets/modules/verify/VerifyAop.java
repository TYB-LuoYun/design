package top.anets.modules.verify;//package com.dicomclub.module.verify;
//
//import com.alibaba.fastjson.JSON;
//import com.dicomclub.common.utils.*;
//import com.dicomclub.log.MyRequestWrapper;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author ftm
// * @date 2023/1/31 0031 17:30
// */
//@Aspect
//@Component
//public class VerifyAop {
//    @Pointcut("@within(Verify)||@annotation(Verify)")
//    public void myPoinitCut(){
//    }
////  RSA密钥对
//    private static final String publicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSUmOXyQmYYSnZacp0btvAZCOvCNPtzixAp7eJmzmAG4mgy/VgrY/s1BDLh9qTNHIRWXepUtwMrf1kYul/A45qE/2oxIbeeq4238YDWQ7ModOVXR9ytEHsT0jpCFvoYfYXYZnnoWRrLIBylQeXzqxbLDxxBxGCs4AjoRKh5S7nNQIDAQAB";
//    private static final String privateKey =  "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANJSY5fJCZhhKdlpynRu28BkI68I0+3OLECnt4mbOYAbiaDL9WCtj+zUEMuH2pM0chFZd6lS3Ayt/WRi6X8DjmoT/ajEht56rjbfxgNZDsyh05VdH3K0QexPSOkIW+hh9hdhmeehZGssgHKVB5fOrFssPHEHEYKzgCOhEqHlLuc1AgMBAAECgYEAqTB9zWx7u4juEWd45ZEIVgw4aGXBllt0Xc6NZrTn3JZKcH+iNNNqJCm0GQaAXkqiODKwgBWXzttoK4kmLHa/6D7rXouWN8PGYXj7DHUNzyOe3IgmzYanowp/A8gu99mJQJzyhZGQ+Uo9dZXAgUDin6HAVLaxF3yWD8/yTKWN4UECQQD8Q72r7qdAfzdLMMSQl50VxRmbdhQYbo3D9FmwUw6W1gy2jhJyPXMi0JZKdKaqhxMZIT3zy4jYqw8/0zF2xc5/AkEA1W+n24Ef3ucbPgyiOu+XGwW0DNpJ9F8D3ZkEKPBgjOMojM7oqlehRwgy52hU+HaL4Toq9ghL1SwxBQPxSWCYSwJAGQUO9tKAvCDh9w8rL7wZ1GLsG0Mm0xWD8f92NcrHE6a/NAv7QGFf3gAaJ+BR92/WMRPe9SMmu3ab2JS1vzX3OQJAdN70/T8RYo8N3cYxNzBmf4d59ee5wzQb+8WD/57QX5UraR8LS+s8Bpc4uHnqvTq8kZG2YI5eZ9YQ6XwlLVbVTQJAKOSXNT+XEPWaol1YdWZDvr2m/ChbX2uwz52s8577Tey96O4Z6S/YA7V6Fr7hZEzkNF+K0LNUd79EOB6m2eQq5w==";
////  aes向量
//    private static final String aseKeyPublic = "ejdhsgdhtyiojhrr";
//    private static final String aseKeyIv = "zcits@loginIV161";
////  md5盐向量
//    private static final String  publicInv = "swiu382j22";
//    @Around("myPoinitCut()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object[] params = null;
//        try {
//            long start = System.currentTimeMillis();
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletRequest request = attributes.getRequest();
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            Method method = signature.getMethod();
//            Verify annotation = method.getAnnotation(Verify.class);
//            if(annotation == null){
//                annotation = joinPoint.getTarget().getClass().getAnnotation(Verify.class);
//            }
//            String value = annotation.value();
//            Map<String, Object> invoke = ServletUtil.getParams(request);
//            System.out.println("对方调用参数:"+invoke);
////          验证时间戳是否过期
//            Long time = (Long) invoke.get("timestamp");
//            long timeSpace = Math.abs(System.currentTimeMillis() - time);
//            if (timeSpace > 1 * 60 * 1000) {
//                throw new RuntimeException("时间校验不合法或者超过60s，请检查是否传输正确,当前时间:"+System.currentTimeMillis());
//            }
//            String sign = (String) invoke.get("sign");
//            invoke.remove("sign");
//            String signNow = null;
//            if(VerifyType.MD5withRSA.equals(value)||VerifyType.MD5withAES.equals(value)){
//                Map<String, Object> map = parseSecurityCode( (String) invoke.get("authCode"),value);
//                signNow = MD5Utils.MD5Lower(RSAUtil.sortAndGroupStringParam(invoke), (String) map.get("appSecret"));
//
//            }else  if(VerifyType.MD5.equals(value)){
//                signNow = MD5Utils.MD5Lower(RSAUtil.sortAndGroupStringParam(invoke), invoke.get("appSecret") + publicInv);
//            }else{
//                throw new RuntimeException("不支持的验签方式");
//            }
////          验签
//            if(!signNow.equals(sign)){
//                throw new RuntimeException("验签失败");
//            }
//            long end = System.currentTimeMillis();
//            System.out.println("验签耗时:"+(end-start));
//
////          获取业务数据
//            Object data = invoke.get("data");
//            Object[] argsCopy = new Object[1];
//            argsCopy[0] = data;
//            params=argsCopy;
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new RuntimeException("参数校验失败");
//        }
//        Object p = joinPoint.proceed(params);
//        return p;
//    }
//
//
//    private static Map<String,Object> parseSecurityCode(String securityCode,String mode) throws Exception {
//
//      //        String parse = RSAUtil.decryptByPriKey(securityCode,privateKey );
//        String parse = null;
//        if(VerifyType.MD5withRSA.equals(mode)){
//            parse = RSAUtil.privateKeyDecrypt(securityCode,privateKey,"xxx" );
//        }
//        if(VerifyType.MD5withAES.equals(mode)){
//            parse =  AESUtil.decryptAES(securityCode, aseKeyPublic, aseKeyIv);
//        }
//        return JSON.parseObject(parse, Map.class);
//    }
//}
