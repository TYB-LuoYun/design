package top.anets.modules.log.appender;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.log.enums.ServiceEnum;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ftm
 * @date 2024-04-09 14:17
 */
@RequestMapping("logAdjust")
@RestController
public class LogAdjustController {

    private static String ServiceLevelCache = "ServiceLevel";

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        Object o = redisTemplate.opsForValue().get(ServiceLevelCache);
        if(o!=null){
            MongoAppender.ServiceLevel = (ConcurrentHashMap<String, String>) o;
        }
    }


    @RequestMapping("update")
    public void update(ServiceEnum serviceEnum,String level){
        if(StringUtils.isNotBlank(level)){
            MongoAppender.ServiceLevel.put(serviceEnum.name(),level );
            redisTemplate.opsForValue().set(ServiceLevelCache, MongoAppender.ServiceLevel);
        }
    }

    @RequestMapping("list")
    public Map list(){
        return MongoAppender.ServiceLevel;
    }
}
