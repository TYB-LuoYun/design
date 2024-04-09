package top.anets.support.mongodb.danamicdatasource;

/**
 * @author ftm
 * @date 2023/3/9 0009 12:35
 */

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


@Component
public class MongodbContextHolder {
    // 数据源对应的MongoDbFactory Map
    private static final Map<String, MongoDatabaseFactory> MONGO_CLIENT_DB_FACTORY_MAP = new HashMap<>();

    // 当前线程ThreadLocal绑定的数据源工厂
    private static final ThreadLocal<MongoDatabaseFactory> MONGO_DB_FACTORY_THREAD_LOCAL = new ThreadLocal<>();

    @Autowired
    private MongodbConfig mongodbConfig;


    @Autowired
    private MongoMappingContext mongoMappingContext; // 注入Mon

    @PostConstruct
    public void afterPropertiesSet() throws UnknownHostException {
        Map<String, MongodbProperties> databases = mongodbConfig.getDatabases();
        if (!CollectionUtils.isEmpty(databases)) {
            for(Entry<String, MongodbProperties> entry : databases.entrySet()) {
                ConnectionString connectionString = new ConnectionString(entry.getValue().getUri());

                MongoCredential credential =null;
                if(!StringUtils.isEmpty(entry.getValue().getUsername())){
                    credential =    MongoCredential.createCredential(
                            entry.getValue().getUsername(),
                            entry.getValue().getAuthenticationDatabase(),
                            entry.getValue().getPassword().toCharArray());
                }
//                MongoTemplate template = new MongoTemplate(mongoClient, entry.getValue().getName());
//                QueryMap queryMap = new QueryMap();
//                Query query = WrapperQueryForMongo.query(queryMap);
//                long count = template.count(query, ExamSearchList.class);
                MongoDatabaseFactory item=  null;
                if(credential == null){
                    item =  new SimpleMongoClientDatabaseFactory(connectionString);
                }else{
                    MongoClientSettings settings  = MongoClientSettings.builder()
                            .applyConnectionString(connectionString)
                            .credential(credential)
                            .build();
                    MongoClient mongoClient = MongoClients.create(settings);
                    item =  new SimpleMongoClientDatabaseFactory(mongoClient, entry.getValue().getDb());
                }
                MongodbContextHolder.MONGO_CLIENT_DB_FACTORY_MAP.put(entry.getKey(),
                        item
                );
            }
        }
    }


    @Bean(name = "dynamicMongoTemplate")
    public DynamicMongoTemplate dynamicMongoTemplate() {
        Iterator<MongoDatabaseFactory> iterator = MONGO_CLIENT_DB_FACTORY_MAP.values().iterator();
        MongoDatabaseFactory mongoDatabaseFactory = iterator.next();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        mongoMappingContext.setAutoIndexCreation(true); // 可选,自动索引创建
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        //去掉_class字段
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return new DynamicMongoTemplate(mongoDatabaseFactory,mappingConverter);
    }



    @Bean(name = "mongoDbFactory")
    public MongoDatabaseFactory mongoDbFactory() {
        Iterator<MongoDatabaseFactory> iterator = MONGO_CLIENT_DB_FACTORY_MAP.values().iterator();
        return iterator.next();
    }


    public static void setMongoDbFactory(String name) {
        MONGO_DB_FACTORY_THREAD_LOCAL.set(MONGO_CLIENT_DB_FACTORY_MAP.get(name));
    }


    public static MongoDatabaseFactory getMongoDbFactory() {
        return MONGO_DB_FACTORY_THREAD_LOCAL.get();
    }


    public static void setMongoDbFactory(MongoDatabaseFactory factory) {
        MONGO_DB_FACTORY_THREAD_LOCAL.set(factory);
    }

    public static void removeMongoDbFactory(){
        MONGO_DB_FACTORY_THREAD_LOCAL.remove();
    }


    public static MongoDatabaseFactory getDefaultMongoDatabaseFactory(){
        return MONGO_CLIENT_DB_FACTORY_MAP.get(MongoDB.DATACENTER.getValue());
    }

}