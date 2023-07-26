package top.anets.support.mongodb.danamicdatasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ftm
 * @date 2023/3/9 0009 12:33
 */

@Configuration
@ConfigurationProperties(prefix = "mongodb")
@PropertySource("classpath:application.yml")
public class MongodbConfig {

    // databases也要和配置文件属性名对应
    private Map<String, MongodbProperties> databases = new HashMap<>();

    public Map<String, MongodbProperties> getDatabases() {
        return databases;
    }

    public void setDatabases(Map<String, MongodbProperties> databases) {
        this.databases = databases;
    }

}