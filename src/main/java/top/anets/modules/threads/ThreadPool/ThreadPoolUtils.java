package top.anets.modules.threads.ThreadPool;


import java.util.concurrent.*;

/**
 * @author LuoYun
 * @since 2022/6/13 16:35
 * 注意死锁：外层需要内层执行结束的结果，内层需要外层释放的线程，两者相持不下，造成程序卡死。
 *
 *
 * 核心线程池大小 : 活动线程在 队列未满的情况下  一直在核心线程数内处理任务 ；
 * 最大线程池大小:线程池允许的最大线程数，BlockingQueue满了，当线程池中的线程数< 最大线程数时，当有新的任务到来时，会继续创建新的线程去处理
 * 线程空闲时间: 当线程池中空闲线程数量超过了核心线程数时，多余的线程会在多长时间内被销毁
 * 线程工作队列:任务队列，被添加到线程池中，但尚未被执行的任务；
 * 任务拒绝策略:当任务超过了线程工作队列时，对任务的拒绝策略
 */
public class ThreadPoolUtils {
    //使用volatile关键字保其可见性
    volatile private static ThreadPoolExecutor threadPool = null;


    //使用volatile关键字保其可见性
    volatile private static PriorityThreadPoolExecutor priorityThreadPoolExecutor = null;



    /**
     * 无返回值直接执行
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {

        log();
        getThreadPool().execute(runnable);
    }

    private static void log() {
        System.out.println("corepoolsize:核心线程数:"+ThreadPoolUtils.getThreadPool().getCorePoolSize());
        System.out.println("maximumpoolsize:最大线程数:" + ThreadPoolUtils.getThreadPool().getMaximumPoolSize());
        System.out.println("poolsize:Worker(活动)线程数量:"+ThreadPoolUtils.getThreadPool().getPoolSize());
//                  返回正在执行任务的大致线程数
        System.out.println("taskcount:任务数"+ ThreadPoolUtils.getThreadPool().getTaskCount());
        System.out.println("ActiveCount:正在执行任务的Worker(活动任务数)线程数量"+ThreadPoolUtils.getThreadPool().getActiveCount());

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

//                        //暂时1个 就够了
//                        threadNum = 1;
                        threadPool = new ThreadPoolExecutor(
                                // 核心线程数
                                threadNum - 1,
                                // 最大线程数
                                threadNum,
                                // 闲置线程存活时间,60分钟
                                1000*60*60,
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



    public static PriorityThreadPoolExecutor getThreadPoolPriority() {
        try {
            if (priorityThreadPoolExecutor == null) {
                synchronized (ThreadPoolUtils.class) {
                    //二次检查
                    if (priorityThreadPoolExecutor == null) {
                        //创建实例之前可能会有一些准备性的耗时工作
                        Thread.sleep(300);
                        // 获取处理器数量
                        int cpuNum = Runtime.getRuntime().availableProcessors();
                        // 根据cpu数量,计算出合理的线程并发数
                        int threadNum = cpuNum * 2 + 1;

//                        //暂时1个 就够了
//                        threadNum = 1;
                        priorityThreadPoolExecutor = new PriorityThreadPoolExecutor(
                                // 核心线程数
                                threadNum - 1,
                                // 最大线程数
                                threadNum,
                                // 闲置线程存活时间,60分钟
                                1000*60*60,
                                // 时间单位
                                TimeUnit.MILLISECONDS,
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
                        priorityThreadPoolExecutor.allowCoreThreadTimeOut(true);
                    }
                    return priorityThreadPoolExecutor;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return priorityThreadPoolExecutor;
    }



    public static void main(String[] args){
        ThreadPoolUtils.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<50;i++){
                    ThreadPoolUtils.getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            while (true){
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }

                while (true){


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }
        });
    }
}
