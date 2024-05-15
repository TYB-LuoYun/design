package top.anets.modules.lock;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.exception.ServiceException;
import top.anets.modules.lock.redission.RedissonLock;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.thread.ThreadPoolUtils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ftm
 * @date 2023-11-16 14:39
 */
@RestController
@RequestMapping("lock")
@Slf4j
public class LockController{
    private   static final Lock lock = new ReentrantLock(true);


    @RequestMapping("testLock")
    @RedissonLock(key = "#id",waitTime = -1)
    public String testLock(String id) throws InterruptedException {
        Thread.sleep(60000);
        return id;
    }




    @RequestMapping("reentrantLock")
    public static String ReentrantLock(Integer i) throws InterruptedException {

        lock.lock();
        System.out.println(i);
        Thread.sleep(5000);

        lock.unlock();

        return "ok";
    }


    @Autowired
    private RedissonClient redissonClient;


    @GetMapping("redissonLock")
    public   String redissonInvoke(String id){
        LockController _this = this;
        for(int i = 0 ;i<2;i++){
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    _this.ReentrantLock(id);
                }
            });
        }
        return "ok";
    }


    /**
     * 可重入锁（Reentrant Lock） 指的是同一个线程可以多次获得同一个锁，而不会因此发生死锁指的是同一个线程可以多次获得同一个锁，而不会因此发生死锁。简单来说，如果一个线程已经获得了某个锁，那么在持有锁的情况下，可以再次获得相同的锁，而不会被自己所持有的锁所阻塞
     * @param id
     */
    @SneakyThrows
    public void ReentrantLock(String id) {
        RLock lock = redissonClient.getLock(  "ssssss:" + id);
        boolean lockSuccess = lock.tryLock(5,TimeUnit.SECONDS);
        if(!lockSuccess){
            log.debug("获取锁失败");
            throw new ServiceException("获取锁失败");
        }
        log.info(id);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        log.info("结束");
    }

    /**
     * 公平锁（Fair Lock）：公平锁（Fair Lock）是一种锁机制，它按照请求锁的顺序来分配锁
     *  new ReentrantLock(true); 就可以
     */




    private static LoadingCache<String, Semaphore> parameterLocks = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Semaphore>() {
                @Override
                public Semaphore load(String parameter) {
                    return new Semaphore(1);
                }
            });



    public static void main(String[] args){
        for (int i =0 ; i<10 ; i ++){
            int finalI = i;
            ThreadPoolUtils.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    runs("you", finalI);
                }
            });
        }
    }
    public static void runs(String key,Integer i) throws InterruptedException {
        Semaphore parameterLock = parameterLocks.getUnchecked(key);
        parameterLock.acquire();
        System.out.println(i);
        Thread.sleep(10000);
        // 释放锁
        parameterLock.release(1);
    }
}
