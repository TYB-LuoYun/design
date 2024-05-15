package top.anets.support.redis;

/**
 * @author LuoYun
 * @since 2022/6/23 18:28
 */

import org.springframework.data.redis.core.RedisCallback;

import java.util.Collection;
import java.util.Set;

public interface IRedisService<T> {

    void set(String key, T value);

    void set(String key, T value, long time);

    T get(String key);

    void delete(String key);

    void delete(Collection<String> keys);

    boolean expire(String key, long time);

    Long getExpire(String key);

    boolean hasKey(String key);

    Long increment(String key, long delta);

    Long decrement(String key, long delta);

    void addSet(String key, T value);

    Set<T> getSet(String key);

    void deleteSet(String key, T value);

    T execute(RedisCallback<T> redisCallback);

    public  boolean tryLock(String key, T value, long expireTime);

    public void unlock(String key, String value);
}