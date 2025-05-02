package top.anets.support.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 *   spring的定时任务有两个坑:
 *
 *      1.单线程执行，多个定时任务不能同时执行，某个任务阻塞影响其他任务，解决:定时任务线程: 配置多线程
 *      2.如果当前定时任务还没有结束，那么下一次定时任务的时间到了也不会触发或者累计次数之后一次性执行。解决办法:加上@EnableAsync注解
 *
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

    // 多线程调度器
    @Bean(name ="multiThreadScheduler")
    public ThreadPoolTaskScheduler multiThreadScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10); // 设置线程池大小
        taskScheduler.setThreadNamePrefix("scheduler-thread-");
        return taskScheduler;
    }
}