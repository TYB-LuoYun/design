package top.anets.modules.verify;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.utils.HttpClientUtil;
import top.anets.utils.MD5Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;


/**
 * @author ftm
 * @date 2023/1/31 0031 17:32
 */
@Slf4j
public class DemoExample {

    public static void main(String[] args){
       test();
    }
    public static void test(){
         /**
          * sign 由对 业务参数 生成json串 后 进行md5得到，其中md5的盐是appSecret
          */
         HashMap<String, Object> businessMap = new HashMap<>();
         businessMap.put("AccessionNumber","US4875");
         businessMap.put("HospitalName","三甲综合演示医院" );
         String res = PostWithSign("http://127.0.0.1:6002/ris/queryReport", businessMap, "U1713942155", "rygqq6foa55viwi3i2b6sklkgit0fb");
         System.out.println("响应结果："+res);
    }


    public static  String PostWithSign(String url ,Map<String, Object> businessMap, String appId,String appSecret){
//         Map<String,Object> signMap=new TreeMap<>();
//         signMap.put("timestamp",System.currentTimeMillis());
//         signMap.put("appId",appId);
////         signMap.put("nonce", UUID.randomUUID().toString());
//         signMap.putAll(businessMap);


         String string = JSON.toJSONString(businessMap);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String string = null;
//        try {
//            string = objectMapper.writeValueAsString(signMap);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        System.out.println("签名参数:"+string);
         String sign = MD5Utils.MD5Lower(string, appSecret);
         System.out.println("签名:"+sign);

         Header[] headers = {
                 new BasicHeader("appId", appId),
                 new BasicHeader("timestamp",  ""+System.currentTimeMillis()),
//                 new BasicHeader("nonce",  (String)signMap.get("nonce")),
                 new BasicHeader("sign", sign)
         };

         String s = HttpClientUtil.doPost(url, JSON.toJSONString(businessMap), "UTF-8", headers);
         return s;
    }

     public static  String GetWithSign(String url ,Map<String, Object> businessMap, String appId,String appSecret){
//          Map<String,Object> signMap=new TreeMap<>();
//          signMap.put("appId",appId);
//          signMap.put("timestamp",System.currentTimeMillis());
//          signMap.put("nonce", UUID.randomUUID().toString());
//          signMap.putAll(businessMap);

          String string = JSON.toJSONString(businessMap);
          String sign = MD5Utils.MD5Lower(string, appSecret);
          System.out.println("签名:"+sign);

          Header[] headers = {
                  new BasicHeader("appId", appId),
                  new BasicHeader("timestamp", ""+System.currentTimeMillis()),
//                  new BasicHeader("nonce",  (String)signMap.get("nonce")),
                  new BasicHeader("sign", sign)
          };

          String s = HttpClientUtil.doGet(url,  businessMap, "UTF-8", headers);
          return s;
     }
}
