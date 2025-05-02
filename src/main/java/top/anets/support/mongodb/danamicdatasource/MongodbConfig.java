package top.anets.support.mongodb.danamicdatasource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "mongodb", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "mongodb")
@PropertySource("classpath:application.yml")
public class MongodbConfig {

    // databases也要和配置文件属性名对应
    private Map<String, MongodbProperties> databases = new HashMap<>();

    private String enable = "true";

    public String getEnable(){
        return enable;
    }

    public Map<String, MongodbProperties> getDatabases() {
        return databases;
    }


    public void setEnable(String enable) {
        this.enable = enable;
    }

    public void setDatabases(Map<String, MongodbProperties> databases) {
        this.databases = databases;
    }

}