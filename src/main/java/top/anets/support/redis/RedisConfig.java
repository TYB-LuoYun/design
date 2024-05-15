package top.anets.support.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author LuoYun
 * @since 2022/6/23 18:03
 *
 * Jedis 是直连模式，在多个线程间共享一个 Jedis 实例时是线程不安全的，
 * Lettuce 是一个可伸缩线程安全的 Redis 客户端。多个线程可以共享同一个 RedisConnection。它利用优秀 netty NIO 框架来高效地管理多个连接。
 *
 * redisTemplate.opsForValue(); // 操作字符串
 * redisTemplate.opsForHash(); // 操作hash
 * redisTemplate.opsForList(); // 操作list
 * redisTemplate.opsForSet(); // 操作set
 * redisTemplate.opsForZSet(); // 操作zset
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 设置序列化器
        template.setDefaultSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
