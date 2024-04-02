package top.anets.support.mongodb.danamicdatasource;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.lang.Nullable;

/**
 * @author ftm
 * @date 2023/3/9 0009 12:35
 */

public class DynamicMongoTemplate extends MongoTemplate {

    public DynamicMongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    public DynamicMongoTemplate(MongoDatabaseFactory mongoDbFactory, @Nullable MongoConverter mongoConverter) {
        super(mongoDbFactory,mongoConverter);
    }


    /**
     * 	springboot 1.x版本
     * 	重写getDb
     */
//    @Override
//    public DB getDb() {
//        MongoDbFactory mongoDbFactory = MongodbContextHolder.getMongoDbFactory();
//        return mongoDbFactory.getDb();
//    }

//      springboot2.x 版本
     @Override
     protected MongoDatabase doGetDatabase() {
         MongoDatabaseFactory mongoDbFactory = MongodbContextHolder.getMongoDbFactory();
//       如果没走注释，不走切面，则采用默认配置
         if(mongoDbFactory == null){
             mongoDbFactory = MongodbContextHolder.getDefaultMongoDatabaseFactory();
         }
         return mongoDbFactory == null ? super.doGetDatabase() : mongoDbFactory.getMongoDatabase();
     }





}