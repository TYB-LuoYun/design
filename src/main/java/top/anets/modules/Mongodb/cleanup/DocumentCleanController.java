package top.anets.modules.Mongodb.cleanup;

import cn.hutool.core.date.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.Mongodb.example.ETask;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.support.mongodb.danamicdatasource.MongodbConfig;

import java.util.Date;
import java.util.List;

/**
 * @author ftm
 * @date 2024-04-02 9:29
 * https://help.aliyun.com/zh/mongodb/use-cases/use-mongodb-to-store-logs
 */
@RequestMapping("document")
@RestController
@ConditionalOnBean(MongoTemplate.class)
public class DocumentCleanController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @RequestMapping("clean")
    public void clean(){
        createCappedCollectionForEntity(ETask.class);
//       创建索引
//         createTTLIndex();

        for(int i = 0;i<1000;i++){
            ETask eTask = new ETask();
            eTask.setContent(""+i);
//            eTask.setTime(new Date());
            mongoTemplate.save(eTask);
        }
    }


    /**
     * 创建有指定大小的集合，当容量超限，会清理久远的数据
     * 使用Capped集合，限制日志库存储大小的上限
     *
     * 使用Capped集合的话，就不能创建ttl索引了
     * @param entityClass
     */
    public void createCappedCollectionForEntity(Class<?> entityClass) {
        String collectionName = mongoTemplate.getCollectionName(entityClass);
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(entityClass);
//            CollectionOptions options = CollectionOptions.empty()
//                    .capped()
//                    .size(100000) // 设置大小为100KB
//                    .maxDocuments(1000); // 最大文档数为1000
//            100MB
//            mongoTemplate.createCollection(collectionName, CollectionOptions.empty().capped().size(104857600));\
//            mongoTemplate.createCollection(collectionName, CollectionOptions.empty().capped().size(100));
        }
    }

    /**
     * 创建有过期时间的集合，当时间过期，会自动清理
     */
    public void createTTLIndex() {
        IndexOperations indexOps = mongoTemplate.indexOps(ETask.class);
        List<IndexInfo> indexes = indexOps.getIndexInfo();
        try {
            // 删除旧的TTL索引
            indexOps.dropIndex("time_1");
        }catch (Exception e){

        }
        mongoTemplate.indexOps(ETask.class).ensureIndex(new Index().on("time", Sort.Direction.ASC).expire(108000));
        List<IndexInfo> indexesz = indexOps.getIndexInfo();
        System.out.println(indexesz);
    }
}
