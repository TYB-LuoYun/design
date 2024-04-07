package top.anets.modules.task.quartz.pojo;

/**
 * @author ftm
 * @date 2024-04-07 11:44
 */
import lombok.Data;
import org.quartz.JobDataMap;

import java.util.Date;

@Data
public class QuartzBean {

    private String id;

    private String jobName;

    private String jobGroup;

    private String jobDescription;

    private String jobClass;

    private String jobStatus;
    /**
     * 设置表示指定时间后任务开始执行
     * 一般都是设置当前时间，表示立即执行
     */
    private Date startTime;
    /**
     * 为null表示任务执行一次
     * 为数字n表示每隔n秒后执行一次
     */
    private Integer interval;
    /**
     * 表示在指定时间后任务结束
     * 如果不设置，表示任务不会结束，
     * 一般我们都会默认为null
     */
    private Date endTime;

    private String cronExpression;

    private JobDataMap jobDataMap;
}