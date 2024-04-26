package top.anets.modules.verify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.anets.base.AssertUtil;
import top.anets.exception.ServiceException;
import top.anets.modules.verify.entity.App;
import top.anets.modules.verify.model.RMDResult;
import top.anets.modules.verify.model.RMDResultEnum; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.anets.utils.MD5Utils;
import top.anets.utils.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ftm
 * @date 2023/1/31 0031 17:30
 */
@Order(1)
@Aspect
@Component
@Slf4j
public class VerifyAop {

    public static ThreadLocal<App> contextHolder = new ThreadLocal<>();

    @Autowired
    private Validator validator;

    @Autowired
    private VerifyService verifyService;
    @Pointcut("@within(top.anets.modules.verify.Verify)||@annotation(top.anets.modules.verify.Verify)")
    public void myPoinitCut(){
    }




    @Around("myPoinitCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] params = joinPoint.getArgs();

        App app = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        request.setAttribute("RESPONSE", "RMD");
        String appId = request.getHeader("appId");
        String timestampStr = request.getHeader("timestamp");
//        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        AssertUtil.isNotEmpty(appId,"appId不能为空");
        AssertUtil.isNotEmpty(timestampStr,"timestamp不能为空" );
//        AssertUtil.isNotEmpty(nonce,"nonce不能为空" );
        AssertUtil.isNotEmpty(sign,"sign不能为空" );
        long timestamp = Long.parseLong(timestampStr);
        if(System.currentTimeMillis() - timestamp>60*1000*20){
            throw new ServiceException("请求过期");
        }
        app = verifyService.verifyIdentity(appId);
        AssertUtil.isNotEmpty(app,"appId不存在" );


        Map<String, Object> requestParam = ServletUtil.getParams();
//        TreeMap<String, Object> map = new TreeMap<>();
//        map.putAll(requestParam);
//        map.put("appId",appId);
//        map.put("timestamp",timestamp);
//        map.put("nonce", nonce);

        String signThis = MD5Utils.MD5Lower(JSON.toJSONString(requestParam), app.getAppSecret());
        AssertUtil.isTrue(signThis.equalsIgnoreCase(sign), "签名不正确");
        packageParams(signature,method,app,params );
        Object p = null;
        try {
            contextHolder.set(app);
            p = joinPoint.proceed(params);
        }finally {
            contextHolder.remove();
        }
        return p;

    }

    private void packageParams(MethodSignature signature, Method method, App verifyVo, Object[] params ) throws IllegalAccessException, InstantiationException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0 ; i<parameterTypes.length ;i++) {
            Class<?> parameterType  = parameterTypes[i];
            if(parameterType == App.class){
                params[i] = verifyVo;
            }
        }
    }


    @Autowired
    private ObjectMapper mapper;

    /**
     * <p>@Description: 对于valid校验结果进行转换结果</p >
     * <p>@param </p >
     * <p>@return </p >
     * <p>@throws </p >
     * <p>@date 13:43 13:43</p >
     */
    public static <T> String getResultFromValidate(Set<ConstraintViolation<T>> violations) {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<T> violation : violations) {
            sb.append(violation.getPropertyPath()).append(violation.getMessage()) ;
            break;
        }
        return sb.toString();
    }

    public static void main(String [] args){


        Map<String,Object> map1 = new HashMap<>();
        map1.put("addTime","1610977949556");
        map1.put("email","112312312");
        map1.put("id","1");
        map1.put("ss",null );
        map1.put("password","125454");
        map1.put("phone","12121212");
        TreeMap<String, Object> hashMap = new TreeMap<>(map1);
        map1.put("child", hashMap);
        System.out.println(" {\"regStartTime\":\"\",\"phone\":\"18787805236\",\"pageStart\":1,\"chineseName\":\"李卫国\",\"pageSize\":1,\"identityCard\":\"532325194908260011\",\"regEndTime\":\"\"}".length());

        String inputString = "{\"pageSize\":1,\"timeType\":1,\"tableName\":\"CasesQuery\",\"pageStart\":5,\"identityCard\":\"532325194908260011\",\"phone\":\"18787805236\",\"examineFeesSum\":0.0,\"totalCount\":6,\"pageEnd\":7,\"startTime\":\"\",\"examineRegisterList\":[{\"accessionNumber\":\"US4315\",\"age\":72,\"ageAndUnitName\":\"72岁\",\"ageUnit\":\"岁\",\"ageUnitName\":\"岁\",\"allergic\":\"\",\"annotation\":\"\",\"appendExamineEnd\":1635935764000,\"area\":\"\",\"areaName\":\"\",\"auditDoctor\":\"石医生\",\"auditDoctorID\":\"smh\",\"auditDoctorName\":\"石医生\",\"auditEnd\":1635935764000,\"auditStart\":1635935764000,\"bedNo\":\"19\",\"biopsylabelList\":\"\",\"birthdate\":\"1949-08-26\",\"cardNo\":\"2325002951\",\"caseDiagnosis\":\"\",\"chargeFlag\":\"0\",\"chargeFlagN";
        String result = inputString.length() <= 139 ? inputString : inputString.substring(0, 139) + "...";
        System.out.println(result);
//        System.out.println(map1);
//
//        TreeMap<String, Object> map = new TreeMap<>(map1);
//        System.out.println(map );
//
//        System.out.println(RSAUtil.sortAndGroupStringParam(map1));


//        System.out.println(SignUtils.getSignContent(map1));

    }



    private void responseWithSign(Object p,String accessSecret) {
//        try {
//            boolean commonType = ReflectUtil.isCommonType(p);
//            if(commonType){
//                return ;
//            }
//            Map<String, Object> map = WrapperQuery.objectToMap(p);
//            String sign = MD5Utils.MD5(RSAUtil.sortAndGroupStringParam(map),accessSecret);
//            if(p instanceof PayOrderQueryRes &&map.get("state").equals(2) && map.get("returnUrl")!=null){
//                map.put("sign",sign );
//                ReflectUtil.setFieldValue(p,"returnUrl", UrlUtil.appendUrlQuery((String) map.get("returnUrl"), map) );
//            }
//            ServletUtil.getRequestAttributes().getResponse().setHeader("sign", sign);
//        }catch (Exception e){
//
//        }
    }


//    private static Map<String,Object> parseSecurityCode(String securityCode,String mode) throws Exception {
//
////      //        String parse = RSAUtil.decryptByPriKey(securityCode,privateKey );
////        String parse = null;
////        if(SignType.MD5withRSA.equals(mode)){
////            parse = RSAUtil.privateKeyDecrypt(securityCode,privateKey,"xxx" );
////        }
////        if(SignType.MD5withAES.equals(mode)){
////            parse =  AESUtil.decryptAES(securityCode, aseKeyPublic, aseKeyIv);
////        }
//        return JSON.parseObject(parse, Map.class);
//    }
}
