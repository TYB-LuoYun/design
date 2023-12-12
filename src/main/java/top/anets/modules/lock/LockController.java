package top.anets.modules.lock;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
public class LockController{
    private   static final Lock lock = new ReentrantLock();
    @RequestMapping("reentrantLock")
    public static String ReentrantLock(Integer i) throws InterruptedException {

        lock.lock();
        System.out.println(i);
        Thread.sleep(5000);

        lock.unlock();

        return "ok";
    }

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
