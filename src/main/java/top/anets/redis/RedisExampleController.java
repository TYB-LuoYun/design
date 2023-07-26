package top.anets.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.temp.IllegalCaseWfxwEnum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LuoYun
 * @since 2022/6/23 18:40
 */
@RestController
@RequestMapping("redis")
public class RedisExampleController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IRedisService<String> redisService;
    @RequestMapping("set")
    public void set(String key ,String value){
        redisTemplate.opsForValue().set(key,value);
        String  s = redisService.get(key);
        System.out.println(s);
    }

    @RequestMapping("lock")
    public void lock(String value){
        System.out.println(redisService.getExpire("trty"));
        boolean trty = redisService.tryLock("trty", value, 30);
        System.out.println(trty);
    }
     public static void main(String[] args) {
//         String s = "{\"status\":200,\"errorCode\":\"B0021\",\"errorMsg\":\"未查到数据，请检查参数！\",\"page\":null,\"pageSize\":null,\"count\":null,\"totalCount\":null,\"data\":["
//                 + "{\"status\":null},{\"status\":400}"+
//                 "],\"code\":null,\"msg\":null}";
//         Map result = JSON.parseObject(s, Map.class);
//         if(result == null){
//             return;
//         }
//         if(result.get("status")!=null && result.get("status").toString().equals("200")) {
//             JSONArray data = (JSONArray) result.get("data");
//             if (data == null || data.size() <= 0) {
//             }else{
//                 System.out.println("数据："+data.size());
//                 for(int i=0;i<data.size();i++){
//                     Map o = (Map) data.get(i);
//                     System.out.println( o.get("status") );
//                      getPages("enen");
//                 }
//             }
//         }else{
//             System.out.println("no：" );
//         }

//         System.out.println(format);


     }
    static  void getPages(String url){
        url = url+"1";
        System.out.println(url);
    }
}
