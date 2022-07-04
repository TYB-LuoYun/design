package top.anets.thread;


import java.util.concurrent.*;

/**
 * @author LuoYun
 * @since 2022/6/13 16:35
 */
public class ThreadPoolUtils {
    //使用volatile关键字保其可见性
    volatile private static ThreadPoolExecutor threadPool = null;

    /**
     * 无返回值直接执行
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        getThreadPool().execute(runnable);
    }

    /**
     * 返回值直接执行
     *
     * @param callable
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return getThreadPool().submit(callable);
    }

    private static ThreadPoolExecutor getThreadPool() {
        try {
            if (threadPool == null) {
                synchronized (ThreadPoolUtils.class) {
                    //二次检查
                    if (threadPool == null) {
                        //创建实例之前可能会有一些准备性的耗时工作
                        Thread.sleep(300);
                        // 获取处理器数量
                        int cpuNum = Runtime.getRuntime().availableProcessors();
                        // 根据cpu数量,计算出合理的线程并发数
                        int threadNum = cpuNum * 2 + 1;

                        //暂时1个 就够了
                        threadNum = 1;
                        threadPool = new ThreadPoolExecutor(
                                // 核心线程数
                                threadNum - 1,
                                // 最大线程数
                                threadNum,
                                // 闲置线程存活时间,60分钟
                                1000,
                                // 时间单位
                                TimeUnit.MILLISECONDS,
                                // 线程队列
                                new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),
                                // 线程工厂
                                Executors.defaultThreadFactory(),
                                // 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略
                                new ThreadPoolExecutor.AbortPolicy() {
                                    @Override
                                    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                                        super.rejectedExecution(r, e);
                                    }
                                }
                        );
                        //允许核心线程关闭
                        threadPool.allowCoreThreadTimeOut(true);
                    }
                    return threadPool;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return threadPool;
    }

}
