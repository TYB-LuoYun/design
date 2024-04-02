package top.anets.support.mongodb.collection;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.support.mongodb.danamicdatasource.MongodbContextHolder;
import top.anets.utils.ClassScanner;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author ftm
 * @date 2024-04-02 14:16
 */
@Slf4j
@Component
public class CappedCollectionTask {
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init(){
        String pacageName = this.getClass().getPackage().getName();
        pacageName = pacageName.substring(0, pacageName.indexOf("."));
        Set<Class> scan = ClassScanner.scan(pacageName, CappedCollection.class);
        if(CollectionUtils.isEmpty(scan)){
            return;
        }


        for(Class item : scan){
            Document document = (Document) item.getAnnotation(Document.class);
            CappedCollection cappedCollection= (CappedCollection) item.getAnnotation(CappedCollection.class);
            if(document == null || cappedCollection == null){
                return;
            }
//          获取字段上的注解
            for(Field field : item.getDeclaredFields()){
                field.setAccessible(true);
                Indexed annotation = field.getAnnotation(Indexed.class);
                if(annotation!=null){
                    log.error("{}该集合已有索引创建，不能再指定为Capped集合",document.collection());
                    return;
                }
            }

            if(cappedCollection.mongoDB() == null){
                initCollection(cappedCollection,document);
            }else{
                MongodbContextHolder.setMongoDbFactory(cappedCollection.mongoDB().getValue());
                try{
                    initCollection(cappedCollection,document);
                }finally {
                    MongodbContextHolder.removeMongoDbFactory();
                }
            }


        }
    }

    private void initCollection(CappedCollection cappedCollection,Document document) {
        String collectionName = document.collection();
        if (!mongoTemplate.collectionExists(collectionName)) {
            log.info("初始化Capped集合:{}",document.collection());
            CollectionOptions options = CollectionOptions.empty()
                    .capped();
            if(cappedCollection.maxSize()>0){
                options = options.size(cappedCollection.maxSize());
            }
            if(cappedCollection.maxDocs()>0){
                options = options.maxDocuments(cappedCollection.maxDocs());
            }
//            100MB
//            mongoTemplate.createCollection(collectionName, CollectionOptions.empty().capped().size(104857600));\
            mongoTemplate.createCollection(collectionName, options);
        }
    }
}
