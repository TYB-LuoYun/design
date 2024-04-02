package top.anets.modules.threads.springThreadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ftm
 * @date 2024-03-07 16:31
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer  {
    /**
     * 项目共用线程池
     */
    public static final String THREAD_POOL_EXECUTOR = "THREAD_POOL_EXECUTOR";

    @Override
    public Executor getAsyncExecutor() {
        return MYExecutor();
    }

    @Bean(THREAD_POOL_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor MYExecutor() {
        // 获取处理器数量
        int cpuNum = Runtime.getRuntime().availableProcessors();
        // 根据cpu数量,计算出合理的线程并发数
        int threadNum = cpuNum * 2 + 1;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadNum - 1);
        executor.setMaxPoolSize(threadNum);
        //优雅停机,不设置的化好像也可以，spring处理了的
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setQueueCapacity(200);//支持同时处理200个，防止任务队列无限增长导致内存溢出或其他问题
        executor.setThreadNamePrefix("we-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadFactory(new MyThreadFactory(executor));
        executor.initialize();
        return executor;
    }
}
