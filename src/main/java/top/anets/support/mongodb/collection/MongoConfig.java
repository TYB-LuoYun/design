package top.anets.support.mongodb.collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * @author ftm
 * @date 2024-04-02 15:21
 */
@EnableMongoAuditing  //启用时间注解的自动创建
@Configuration
public class MongoConfig {
}
