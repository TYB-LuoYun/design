//package top.anets.modules.aopRsaAes;
//
//import com.alibaba.fastjson.JSON;
//import org.apache.commons.lang3.StringUtils;
//import top.anets.utils.AESUtil;
//import top.anets.utils.RSAUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author LuoYun
// * @since 2022/6/21 9:30
// * @description 仅供参考
// */
//public class RSAInvoiceExample {
//    public static void main(String[] args) throws Exception {
////      公钥,私钥 事先约定好 ，并配置
//        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSUmOXyQmYYSnZacp0btvAZCOvCNPtzixAp7eJmzmAG4mgy/VgrY/s1BDLh9qTNHIRWXepUtwMrf1kYul/A45qE/2oxIbeeq4238YDWQ7ModOVXR9ytEHsT0jpCFvoYfYXYZnnoWRrLIBylQeXzqxbLDxxBxGCs4AjoRKh5S7nNQIDAQAB";
//        String privateKey =  "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANJSY5fJCZhhKdlpynRu28BkI68I0+3OLECnt4mbOYAbiaDL9WCtj+zUEMuH2pM0chFZd6lS3Ayt/WRi6X8DjmoT/ajEht56rjbfxgNZDsyh05VdH3K0QexPSOkIW+hh9hdhmeehZGssgHKVB5fOrFssPHEHEYKzgCOhEqHlLuc1AgMBAAECgYEAqTB9zWx7u4juEWd45ZEIVgw4aGXBllt0Xc6NZrTn3JZKcH+iNNNqJCm0GQaAXkqiODKwgBWXzttoK4kmLHa/6D7rXouWN8PGYXj7DHUNzyOe3IgmzYanowp/A8gu99mJQJzyhZGQ+Uo9dZXAgUDin6HAVLaxF3yWD8/yTKWN4UECQQD8Q72r7qdAfzdLMMSQl50VxRmbdhQYbo3D9FmwUw6W1gy2jhJyPXMi0JZKdKaqhxMZIT3zy4jYqw8/0zF2xc5/AkEA1W+n24Ef3ucbPgyiOu+XGwW0DNpJ9F8D3ZkEKPBgjOMojM7oqlehRwgy52hU+HaL4Toq9ghL1SwxBQPxSWCYSwJAGQUO9tKAvCDh9w8rL7wZ1GLsG0Mm0xWD8f92NcrHE6a/NAv7QGFf3gAaJ+BR92/WMRPe9SMmu3ab2JS1vzX3OQJAdN70/T8RYo8N3cYxNzBmf4d59ee5wzQb+8WD/57QX5UraR8LS+s8Bpc4uHnqvTq8kZG2YI5eZ9YQ6XwlLVbVTQJAKOSXNT+XEPWaol1YdWZDvr2m/ChbX2uwz52s8577Tey96O4Z6S/YA7V6Fr7hZEzkNF+K0LNUd79EOB6m2eQq5w==";
////      调用方标识，需要进行配置
//        String appId ="222222222";
////      ase偏移向量(不能更改)
//        String aseKeyIv = "zcits@loginIV161";
//        consume(privateKey,publicKey,appId,aseKeyIv);
//    }
//
//    public static void consume(String privateKey, String publicKey,String appId,String aseKeyIv) throws Exception {
////      使用16位(可以自己定义)作为AES密钥，并对密钥进行rsa公钥加密,调用方决定
//        String aseKeyPublic = "ejdhsgdhtyiojhrr";
//
////      业务请求路径,以请求获取  《总体的超限率、案件数（件）》 为例
//        String url ="http://192.168.20.29:8101/zc-biz/api-public/anji/getMapStaticstics";
////      你需要封装的业务参数(结合需要进行修改)
//        Map<String,Object>  data = new HashMap<>();
////        stationType：21是超限检测点，31是非现场检测点，71是重点货运源头
//        data.put("stationType",31);
//
//
//
////      封装请求体
//        Map<String,Object> map = new HashMap<>();
////      用RSA公钥对aes密钥进行加密
////      requestId 请求的唯一ID，可以用getUUID方法生成
//        map.put("requestId","23322");
//        map.put("appId",appId);
//        map.put("timestamp" ,System.currentTimeMillis());
//        String aseKey = RSAUtil.encryptByPriKey(aseKeyPublic, privateKey);
//        map.put("aseKey",aseKey);
////      ASE对业务内容加密
//        String body = AESUtil.encryptAES(JSON.toJSONString(data), aseKeyPublic, aseKeyIv);
//        map.put("body",body);
////      签名
//        String sign = RSAUtil.sign(RSAUtil.sortAndGroupStringParam(map),privateKey);
//        map.put("sign", sign);
//        System.out.println("请求URL:"+url);
//        System.out.println("请求业务参数=========");
//        if(data!=null ){
//            data.forEach((key,val)->{
//                System.out.println(key+":"+val);
//            });
//        }
//        System.out.println("请求体内容=========");
//        map.forEach((key,val)->{
//            System.out.println(key+":"+val);
//        });
//        System.out.println("=================");
////      http发送post请求到指定接口,参数为map
////      略........
//        String response = HttpClientUtil.postJson(url, JSON.toJSONString(map));
//        System.out.println("请求响应:");
//        System.out.println(response);
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
//    }
//}
