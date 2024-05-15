package top.anets.modules.cache.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.anets.modules.cache.model.NullValue;
import top.anets.modules.cache.util.NullValueUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author ftm
 * @date 2024-05-11 14:36
 *
 * 二级缓存需要考虑的点还很多。
 *
 * 1.保证分布式节点一级缓存的一致性？ 需要redis的发布订阅
 *
 * 2.是否允许存储空值？   ✔  如果某个查询缓存和数据库中都没有，那么就会导致频繁查询数据库，导致数据库Down,这也是我们 常说的缓存穿透。
 *
 * 但如果存储空值呢，因为可能会存储大量的空值，导致缓存变大，所以这个最好是可配置，按照业务来决定是否开启。   ✔
 *
 * 3.是否需要缓存预热？ 某些key一开始就会非常的热，也就是热点数据，那么我们是否可以一开始就先存储到缓存中，避免缓存击穿。
 *
 * 4.一级缓存存储数量上限的考虑？   ✔ 一级缓存是应用内缓存，那你是否考虑一级缓存存储的数据给个限定最大值，避免存储太多的一级缓存导致OOM。
 *
 * 5.一级缓存过期策略的考虑？
 LRU -  Least Recently Used 淘汰最长时间没有被访问的。 每次访问就把这个元素放到队列的头部，队列满了就淘汰队列尾部的数据，即淘汰最长时间没有被访问的。缺点是，如果某一时刻大量数据到来，很容易将热点数据挤出缓存，留下来的很可能是只访问一次，今后不会再访问的或频率极低的数据
 LFU -  Least Frequently Used   也就是淘汰一定时间内被访问次数最少的数据。优点是，避免了 LRU 的缺点，因为根据频率淘汰，不会出现大量进来的挤压掉老的，如果在数据的访问的模式不随时间变化时候，LFU 能够提供绝佳的命中率。其缺点是，偶发性的、周期性的批量操作会导致LRU命中率急剧下降，缓存污染情况比较严重。
 W-TinyLFU 是 Caffeine 提出的一种全新算法，它可以解决频率统计不准确以及访问频率衰减的问题.结合LRU和LFU算法的优缺点的一个缓存淘汰算法，说白了就是要保留LRU和LFU的这些优点(淘汰策略要结合最近访问时间和访问次数)，并规避掉上述各自的问题
 *
 * 6.一级缓存过期了如何清除？
 *
 */
@Service
public class RedisCaffeineCache extends AbstractCache{
    @Autowired
    private RedisTemplate redisTemplate;

      Cache<String, Object> LocalCache = Caffeine.newBuilder()
            .maximumSize(100)
//           最后访问后间隔多久60s淘汰
            .expireAfter(new Expiry<String, Object>(){
                @Override
                public long expireAfterCreate(@NonNull String s, @NonNull Object cacheInfo, long  currentTime) {
                    return 0;
                }

                @Override
                public long expireAfterUpdate(@NonNull String s, @NonNull Object cacheInfo, long l, @NonNegative long currentDuration) {
                    return currentDuration;
                }

                @Override
                public long expireAfterRead(@NonNull String s, @NonNull Object cacheInfo, long l, @NonNegative long currentDuration) {
                    return currentDuration;
                }
            })
              .build();



    Cache<String, Object> NullValueLocalCache = Caffeine.newBuilder()
            .maximumSize(100)
//           最后访问后间隔多久60s淘汰
            .expireAfterWrite(60,TimeUnit.SECONDS).build();



    public void set(String key, Object value ) {
        set(  key,  value,null, null);
    }


    public void set(String key, Object value, Long expire, TimeUnit timeUnit) {
//        存储空值防止缓存穿透，空值的过期时间尽量短
//        在设定的过期时间基础上，补充随机时间，防止缓存雪崩
        Object toStoreValue = NullValueUtil.toStoreValue(value, true);
        if(expire != null){
            redisTemplate.opsForValue().set(key,toStoreValue,expire+(int) (Math.random() * 10 + 5), TimeUnit.SECONDS);
        }else{
            Long ttl = redisTemplate.getExpire(key);
            if(ttl!=null&& ttl>= -1){
                redisTemplate.opsForValue().set(key,toStoreValue ,ttl,TimeUnit.SECONDS);
            }

        }
        if(value == null){
            NullValueLocalCache.put(key, toStoreValue); //空值比较多，最好与有效数据分开存储
        }else{
            if(expire != null){
                LocalCache.policy().expireVariably().ifPresent(e->{
                    e.put(key, toStoreValue, expire, timeUnit);
                });
            }else{
                LocalCache.put(key,toStoreValue );
            }

        }
    }

    public Object get(String key) {
        Object ifAbsent = this.getIfPresent(key);
        return NullValueUtil.fromStoreValue(ifAbsent, true);
    }

    /**
     * 需要处理null值
     * @param key
     * @return
     */
    public Object getIfPresent(String key) {
        Object ifPresent = LocalCache.getIfPresent(key);
        if(ifPresent == null){
            ifPresent = NullValueLocalCache.getIfPresent(key);
        }
        if(ifPresent == null){
            ifPresent = redisTemplate.opsForValue().get(key);
        }
        return ifPresent;
    }


    public void evict(Object key) {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        redisTemplate.delete( key);
//          push(new CacheMessage(this.cacheName, key));  //通过RedisTemplate().convertAndSend
        LocalCache.invalidate(key);
        NullValueLocalCache.invalidate(key);
    }
}
