package top.anets.modules.asyncs;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import top.anets.modules.serviceMonitor.server.Sys;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ftm
 * @date 2023/4/23 0023 13:39
 */
public class ThreadSemaphore {
    String cache = null;
    Long start = null;
    private LoadingCache<String, Semaphore> parameterLocks = CacheBuilder.newBuilder()
            // 最大容量为 100 超过容量有对应的淘汰机制，下文详述
            .maximumSize(2)
            .expireAfterWrite(6, TimeUnit.SECONDS) // 设置锁的过期时间为5分钟
            .build(new CacheLoader<String, Semaphore>() {
                @Override
                public Semaphore load(String parameter) {
                    return new Semaphore(1);
                }
            });

    public Object myMethod(String parameter,int integer) throws InterruptedException {
        Thread.sleep(100);
        String key = Thread.currentThread().getName()+"线程，"+parameter+"任务："+integer+":";
        System.out.println(key+"首-检查大小-"+parameterLocks.size());
        Semaphore parameterLock = parameterLocks.getUnchecked(parameter);
        System.out.println(key+"获取锁");
        // 尝试获取锁，如果获取不到则等待
        parameterLock.acquire();
        try {
            System.out.println(key+"中-检查大小-"+parameterLocks.size());
            System.out.println(key+"获取锁成功");
            if(cache == null){
                Thread.sleep(5000);
                cache = "新"+parameter+"-"+integer;
            }            // Perform the resource-intensive query here
            else{
                Thread.sleep(5000);
                System.out.println(key+"取缓存");
            }
        } finally {
            // 释放锁
            parameterLock.release(2-parameterLock.availablePermits());
            System.out.println(key+"释放锁成功====,剩余"+parameterLock.availablePermits());
            System.out.println(key+"消耗时间："+(System.currentTimeMillis()-start));
        }
        return cache;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadSemaphore resource = new ThreadSemaphore();
        resource.start = System.currentTimeMillis();

//        for(int i = 0;i<3;i++){
//            int finalI = i;
//            if(i==2){
//                Thread.sleep(8000);
//            }
//            ThreadPoolUtils.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Object o = resource.myMethod("3432", finalI);
//                        System.out.println(finalI+"返回结果"+o);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
        Semaphore parameterLock = resource.parameterLocks.getUnchecked("4333");
//        System.out.println(parameterLock);
        parameterLock.release(7);
        Semaphore parameterLock2 = resource.parameterLocks.getUnchecked("5898");
        Semaphore parameterLoc3 = resource.parameterLocks.getUnchecked("44322");
//        System.out.println(resource.parameterLocks.size()+":"+(System.currentTimeMillis()-resource.start));
        while (true){
            System.out.println(resource.parameterLocks.getIfPresent("4333"));
            System.out.println(resource.parameterLocks.getUnchecked("4333"));
            System.out.println(resource.parameterLocks.size()+":"+(System.currentTimeMillis()-resource.start));
            Thread.sleep(1000);
        }
    }

}
