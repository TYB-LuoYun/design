package top.anets.modules.asyncs;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ftm
 * @date 2023/4/23 0023 10:38
 */
public class ThreadSnatchResource {


    Long start = null;

    private LoadingCache<String, ReentrantLock> parameterLocks = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.SECONDS) // 这里设置了锁的过期时间为5分钟
            .build(new CacheLoader<String, ReentrantLock>() {
                @Override
                public ReentrantLock load(String parameter) {
                    return new ReentrantLock();
                }
            });


    public Object myMethod(String parameter,int integer) throws InterruptedException {
        String key = Thread.currentThread().getName()+"线程，"+parameter+"任务："+integer+":";
        ReentrantLock parameterLock = parameterLocks.getUnchecked(parameter);
        System.out.println(key+"获取锁");
        parameterLock.lock();
        try {
            System.out.println(key+"获取锁成功");
            System.out.println(key+"检查大小-"+parameterLocks.size());
            Thread.sleep(5000);
            // Perform the resource-intensive query here
        } finally {
            parameterLock.unlock();
            System.out.println(key+"释放锁成功====");
            System.out.println(key+"消耗时间："+(System.currentTimeMillis()-start));
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {

        ThreadSnatchResource resource = new ThreadSnatchResource();
        resource.start = System.currentTimeMillis();
        for(int i = 0;i<3;i++){
            int finalI = i;
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Object o = resource.myMethod("2222", finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
