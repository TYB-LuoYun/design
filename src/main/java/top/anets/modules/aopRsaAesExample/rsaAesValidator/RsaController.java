package top.anets.modules.aopRsaAesExample.rsaAesValidator;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.utils.AESUtil;
import top.anets.utils.HttpClientUtil;
import top.anets.utils.RSAUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ftm
 * @date 2022/11/22 0022 10:36
 */
@RestController
@RequestMapping("/rsa")
public class RsaController {
//    /**
//     * 测试AuthorizeToken
//     * @return
//     */
//    @RequestMapping("ping")
//    public String ping(){
//
//        String securityKey = "fdbd898c448b6c5a1ede29b195e38fa4b4109fee73dd9e4abf8e5b0845699c93";
//        long timestamp = System.currentTimeMillis();
//        String token = MD5Util.EncodeByMD5(securityKey + "$" + timestamp);
//
//        Header[] headers = {
//                new BasicHeader(AuthorizeConstant.TOKEN, token),
//                new BasicHeader(AuthorizeConstant.TIMESTAMP,timestamp+"" )
//        };
//        String s = HttpClientUtil.doGet("http://127.0.0.1:8133/authorize/test", new HashMap<>(), "UTF-8", headers);
//        return s;
//    }


    /**
     * 测试rsa-私钥加密，公钥解密验签
     */
    @RequestMapping("rsa")
    public String rsa() throws Exception {
//      调用方的私钥
        String privateKey =  "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANJSY5fJCZhhKdlpynRu28BkI68I0+3OLECnt4mbOYAbiaDL9WCtj+zUEMuH2pM0chFZd6lS3Ayt/WRi6X8DjmoT/ajEht56rjbfxgNZDsyh05VdH3K0QexPSOkIW+hh9hdhmeehZGssgHKVB5fOrFssPHEHEYKzgCOhEqHlLuc1AgMBAAECgYEAqTB9zWx7u4juEWd45ZEIVgw4aGXBllt0Xc6NZrTn3JZKcH+iNNNqJCm0GQaAXkqiODKwgBWXzttoK4kmLHa/6D7rXouWN8PGYXj7DHUNzyOe3IgmzYanowp/A8gu99mJQJzyhZGQ+Uo9dZXAgUDin6HAVLaxF3yWD8/yTKWN4UECQQD8Q72r7qdAfzdLMMSQl50VxRmbdhQYbo3D9FmwUw6W1gy2jhJyPXMi0JZKdKaqhxMZIT3zy4jYqw8/0zF2xc5/AkEA1W+n24Ef3ucbPgyiOu+XGwW0DNpJ9F8D3ZkEKPBgjOMojM7oqlehRwgy52hU+HaL4Toq9ghL1SwxBQPxSWCYSwJAGQUO9tKAvCDh9w8rL7wZ1GLsG0Mm0xWD8f92NcrHE6a/NAv7QGFf3gAaJ+BR92/WMRPe9SMmu3ab2JS1vzX3OQJAdN70/T8RYo8N3cYxNzBmf4d59ee5wzQb+8WD/57QX5UraR8LS+s8Bpc4uHnqvTq8kZG2YI5eZ9YQ6XwlLVbVTQJAKOSXNT+XEPWaol1YdWZDvr2m/ChbX2uwz52s8577Tey96O4Z6S/YA7V6Fr7hZEzkNF+K0LNUd79EOB6m2eQq5w==";
//      封装请求体
        Map<String,Object> map = new HashMap<>();
//      用RSA公钥对aes密钥进行加密
//      requestId 请求的唯一ID，可以用getUUID方法生成
        map.put("params","参数测试");
        map.put("timestamp" ,System.currentTimeMillis());
//      签名
        String sign = RSAUtil.sign(RSAUtil.sortAndGroupStringParam(map),privateKey);
        map.put("sign", sign);
        String s = HttpClientUtil.doPost("http://127.0.0.1:8133/authorize/rsa", JSON.toJSONString(map), "UTF-8" );
        return s;
    }


    /**
     * 测试-安全性高-对参数也进行加密
     */
    public String rsaaes() throws Exception{
        //      使用16位(可以自己定义)作为AES密钥，并对密钥进行rsa公钥加密,调用方决定
        String aseKeyPublic = "ejdhsgdhtyiojhrr";
        String privateKey =  "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANJSY5fJCZhhKdlpynRu28BkI68I0+3OLECnt4mbOYAbiaDL9WCtj+zUEMuH2pM0chFZd6lS3Ayt/WRi6X8DjmoT/ajEht56rjbfxgNZDsyh05VdH3K0QexPSOkIW+hh9hdhmeehZGssgHKVB5fOrFssPHEHEYKzgCOhEqHlLuc1AgMBAAECgYEAqTB9zWx7u4juEWd45ZEIVgw4aGXBllt0Xc6NZrTn3JZKcH+iNNNqJCm0GQaAXkqiODKwgBWXzttoK4kmLHa/6D7rXouWN8PGYXj7DHUNzyOe3IgmzYanowp/A8gu99mJQJzyhZGQ+Uo9dZXAgUDin6HAVLaxF3yWD8/yTKWN4UECQQD8Q72r7qdAfzdLMMSQl50VxRmbdhQYbo3D9FmwUw6W1gy2jhJyPXMi0JZKdKaqhxMZIT3zy4jYqw8/0zF2xc5/AkEA1W+n24Ef3ucbPgyiOu+XGwW0DNpJ9F8D3ZkEKPBgjOMojM7oqlehRwgy52hU+HaL4Toq9ghL1SwxBQPxSWCYSwJAGQUO9tKAvCDh9w8rL7wZ1GLsG0Mm0xWD8f92NcrHE6a/NAv7QGFf3gAaJ+BR92/WMRPe9SMmu3ab2JS1vzX3OQJAdN70/T8RYo8N3cYxNzBmf4d59ee5wzQb+8WD/57QX5UraR8LS+s8Bpc4uHnqvTq8kZG2YI5eZ9YQ6XwlLVbVTQJAKOSXNT+XEPWaol1YdWZDvr2m/ChbX2uwz52s8577Tey96O4Z6S/YA7V6Fr7hZEzkNF+K0LNUd79EOB6m2eQq5w==";
        String aseKeyIv = "zcits@loginIV161";

//      业务请求路径,以请求获取  《总体的超限率、案件数（件）》 为例
        String url ="http://192.168.20.29:8101/zc-biz/api-public/anji/getMapStaticstics";
//      你需要封装的业务参数(结合需要进行修改)
        Map<String,Object>  data = new HashMap<>();
//        stationType：21是超限检测点，31是非现场检测点，71是重点货运源头
        data.put("stationType",31);



//      封装请求体
        Map<String,Object> map = new HashMap<>();
//      用RSA公钥对aes密钥进行加密
//      requestId 请求的唯一ID，可以用getUUID方法生成
        map.put("requestId","23322");
//        map.put("appId",appId);
        map.put("timestamp" ,System.currentTimeMillis());
        String aseKey = RSAUtil.encryptByPriKey(aseKeyPublic, privateKey);
        map.put("aseKey",aseKey);
//      ASE对业务内容加密
        String body = AESUtil.encryptAES(JSON.toJSONString(data), aseKeyPublic, aseKeyIv);
        map.put("body",body);
//      签名
        String sign = RSAUtil.sign(RSAUtil.sortAndGroupStringParam(map),privateKey);
        map.put("sign", sign);
        System.out.println("请求URL:"+url);
        System.out.println("请求业务参数=========");
        if(data!=null ){
            data.forEach((key,val)->{
                System.out.println(key+":"+val);
            });
        }
        System.out.println("请求体内容=========");
        map.forEach((key,val)->{
            System.out.println(key+":"+val);
        });
        System.out.println("=================");
//      http发送post请求到指定接口,参数为map
//      略........
        String response = HttpClientUtil.doPost(url, JSON.toJSONString(map),"utf-8");
        System.out.println("请求响应:");
        System.out.println(response);
//
////      响应内容解析
//        Map result = JSON.parseObject(response, Map.class);
//        if(result == null){
//            System.out.println("请求返回结果为null");
//            return;
//        }
//        if((Integer) result.get("code") != 200){
//            System.out.println("失败:"+(result.get("msg")==null?result.get("message"):result.get("msg")));
//            return;
//        }
//        String resultDataAes = (String) result.get("data");
//        if(StringUtils.isBlank(resultDataAes)){
//            System.out.println("请求未返回数据");
//            return;
//        }
////      解析内容
//        String resultDataStr = AESUtil.decryptAES(resultDataAes, aseKeyPublic, aseKeyIv);
//        System.out.println("解析内容=============");
////      响应的业务内容,可能是数组，可能是对象
//        System.out.println(resultDataStr);
        return response;
    }



}
