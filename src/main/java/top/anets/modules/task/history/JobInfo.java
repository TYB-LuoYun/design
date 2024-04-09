package top.anets.modules.task.history;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author ftm
 * @date 2024-04-07 11:30
 */
@Data
@TableName("tb_job_info")
public class JobInfo{

    //任务类名
    private String jobClassName;

    //cron表达式
    private String cronExpression;

    //参数
    private String parameter;

    //任务名称
    private String jobName;

    //任务分组
    private String jobGroup;

    //描述
    private String description;

    //任务状态
    private String jobStatus;

    //任务开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    //任务结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

}
