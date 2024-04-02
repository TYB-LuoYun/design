package top.anets.config.redisson;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ftm
 * @date 2023-08-17 14:49
 */

@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.database}")
    private Integer database;


    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient getRedisson(){
        //

        Config config = new Config();
        //单机模式  依次设置redis地址和密码

        if(StringUtils.isNotBlank(password)){
            config.useSingleServer()
                    .setAddress("redis://" + host + ":" + port)
                    .setPassword(password)
                    .setDatabase(database);
        }else{config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(database);

        }
        return Redisson.create(config);
    }
}
