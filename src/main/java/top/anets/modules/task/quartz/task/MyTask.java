package top.anets.modules.task.quartz.task;

/**
 * @author ftm
 * @date 2024-04-07 11:46
 */
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MyTask implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("Task" + ">>" + sdf.format(new Date()) + ":" + context.getJobDetail().getKey() + "执行中..." + context.getJobDetail().getDescription());
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        log.info(mergedJobDataMap.getString("jobDataMapParam"));
    }
}