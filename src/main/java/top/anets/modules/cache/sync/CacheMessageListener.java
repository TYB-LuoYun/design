package top.anets.modules.cache.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import top.anets.modules.cache.service.RedisCaffeineCache;

/**
 *  缓存消息监听器
 *
 * @author xub
 * @date 2022/3/16 上午10:31
 */
public class CacheMessageListener implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(CacheMessageListener.class);

    private RedisTemplate redisTemplate;

    private RedisCaffeineCache redisCaffeineCacheManager;

    public CacheMessageListener(RedisTemplate redisTemplate, RedisCaffeineCache redisCaffeineCacheManager) {
        super();
        this.redisCaffeineCacheManager = redisCaffeineCacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        logger.debug("recevice a redis topic message, clear local cache, the cacheName is {}, the key is {}", cacheMessage.getCacheName(), cacheMessage.getKey());
        redisCaffeineCacheManager.evict(cacheMessage.getCacheName()+cacheMessage.getKey());
    }
}
