package top.anets.support.mongodb.collection;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import top.anets.support.mongodb.danamicdatasource.MongodbContextHolder;
import top.anets.utils.ClassScanner;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ftm
 * @date 2024-04-02 14:16
 */
@Slf4j
@Component
public class CleanUpCollectionTask {
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init(){
        String pacageName = this.getClass().getPackage().getName();
        pacageName = pacageName.substring(0, pacageName.indexOf("."));
        Set<Class> scan = ClassScanner.scan(pacageName, CleanUpCollection.class);
        if(CollectionUtils.isEmpty(scan)){
            return;
        }
        for(Class item : scan){
            Document document = (Document) item.getAnnotation(Document.class);
            CleanUpCollection cleanUpCollection = (CleanUpCollection) item.getAnnotation(CleanUpCollection.class);
            if(document == null || cleanUpCollection == null){
                return;
            }
//          获取字段上的注解
            for(Field field : item.getDeclaredFields()){
                field.setAccessible(true);
                Indexed annotation = field.getAnnotation(Indexed.class);
                if(annotation!=null){
                    log.error("{}该集合已有索引创建，不能再指定为Capped集合",document.collection());
                    if(annotation.expireAfterSeconds()>0){
//                        说明是ttl索引，需要主动去创建
                        smartCreateCollection(cleanUpCollection,document);
                        String name = field.getName();
                        smartInitIndexTTL(cleanUpCollection,name,annotation.expireAfterSeconds(),item);
                    }
                    return;
                }
            }


            smartCreateCappedCollection(cleanUpCollection,document);


        }
    }

    private void smartCreateCollection(CleanUpCollection cleanUpCollection, Document document) {
        if(cleanUpCollection.mongoDB() == null){
            initCollection( document);
        }else{
            MongodbContextHolder.setMongoDbFactory(cleanUpCollection.mongoDB().getValue());
            try{
                initCollection( document);
            }finally {
                MongodbContextHolder.removeMongoDbFactory();
            }
        }
    }



    public void smartCreateCappedCollection(CleanUpCollection cleanUpCollection, Document document) {
        if(cleanUpCollection.mongoDB() == null){
            initCappedCollection(cleanUpCollection,document);
        }else{
            MongodbContextHolder.setMongoDbFactory(cleanUpCollection.mongoDB().getValue());
            try{
                initCappedCollection(cleanUpCollection,document);
            }finally {
                MongodbContextHolder.removeMongoDbFactory();
            }
        }
    }

    public void smartInitIndexTTL(CleanUpCollection cleanUpCollection,String time,Integer second ,Class item){
        if(cleanUpCollection.mongoDB() == null){
            initIndexTTL(time,second ,item);
        }else{
            MongodbContextHolder.setMongoDbFactory(cleanUpCollection.mongoDB().getValue());
            try{
                initIndexTTL(time,second ,item);
            }finally {
                MongodbContextHolder.removeMongoDbFactory();
            }
        }
    }

    private void initCollection(  Document document) {
        String collectionName = document.collection();
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
    }

    public void initCappedCollection(CleanUpCollection cleanUpCollection, Document document) {
        String collectionName = document.collection();
        if (!mongoTemplate.collectionExists(collectionName)) {
            log.info("初始化Capped集合:{}",document.collection());
            CollectionOptions options = CollectionOptions.empty()
                    .capped();
            if(cleanUpCollection.cappedMaxSize()>0){
                options = options.size(cleanUpCollection.cappedMaxSize());
            }
            if(cleanUpCollection.cappedMaxDocs()>0){
                options = options.maxDocuments(cleanUpCollection.cappedMaxDocs());
            }
//            100MB
//            mongoTemplate.createCollection(collectionName, CollectionOptions.empty().capped().size(104857600));\
            mongoTemplate.createCollection(collectionName, options);
        }
    }

    public void  initIndexTTL(String time, Integer second,Class clazz) {
        IndexOperations indexOps = mongoTemplate.indexOps(clazz);
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
        mongoTemplate.indexOps(clazz).ensureIndex(new Index().on(time, Sort.Direction.ASC).expire(second));
    }
}
