package top.anets.modules.log.appender;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.support.mongodb.danamicdatasource.MongoDB;
import top.anets.support.mongodb.danamicdatasource.MongoDataSource;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ftm
 * @date 2024-04-09 14:17
 */
@RequestMapping("logAdjust")
@RestController
@MongoDataSource(MongoDB.LOGDB)
public class LogAdjustController {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static String ServiceLevelCache = "ServiceLevels";

    private static String SystemSqlCache = "SystemSqlCaches";

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        String o = (String) redisTemplate.opsForValue().get(ServiceLevelCache);
        if(o!=null){
            MongoAppender.ServiceLevel =JSON.parseObject(o, ConcurrentHashMap.class);
        }

        String o2 = (String) redisTemplate.opsForValue().get(SystemSqlCache);
        if(o2!=null){
            MongoAppender.SystemSql =JSON.parseObject(o2, ConcurrentHashMap.class);
        }
    }


    @RequestMapping("update")
    public void update(String classify, String level,Boolean ifPrintSql){
        if(StringUtils.isNotBlank(level)){
            MongoAppender.ServiceLevel.put(classify,level );
            redisTemplate.opsForValue().set(ServiceLevelCache, JSON.toJSONString( MongoAppender.ServiceLevel));
        }
        if(ifPrintSql!=null){
            MongoAppender.SystemSql.put(classify,ifPrintSql );
            redisTemplate.opsForValue().set(SystemSqlCache, JSON.toJSONString(MongoAppender.SystemSql));
        }
    }

    @RequestMapping("list")
    public Map list(){
        return MongoAppender.ServiceLevel;
    }


    /**
     * 更新索引保存时间
     */
    @RequestMapping("index")
    public void updateIndex(  Integer second){
        if(second == null||second<60){
            return;
        }
        smartInitIndexTTL("time",second);
    }

    private void smartInitIndexTTL(String time, Integer second) {
        IndexOperations indexOps = mongoTemplate.indexOps(Loginfo.class);
        List<IndexInfo> indexes = indexOps.getIndexInfo();
        if(CollectionUtils.isNotEmpty(indexes)){
            AtomicReference<String> timeindex = new AtomicReference<>();
            indexes.forEach(item->{
                item.getIndexFields().forEach(each->{
                    if(time.equals(each.getKey())){
                        timeindex.set(item.getName());
                    }
                });
            });
            if(timeindex.get()!=null){
                try {
                    // 删除旧的TTL索引
                    indexOps.dropIndex(timeindex.get());
                }catch (Exception e){

                }
            }
        }
        mongoTemplate.indexOps(Loginfo.class).ensureIndex(new Index().on(time, Sort.Direction.ASC).expire(second));
    }
}
