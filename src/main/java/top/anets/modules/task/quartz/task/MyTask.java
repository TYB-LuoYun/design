package top.anets.modules.task.quartz.task;

/**
 * @author ftm
 * @date 2024-04-07 11:46
 */
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import top.anets.modules.log.appender.LogDb;
import top.anets.modules.log.enums.BusinessEnum;
import top.anets.modules.system.service.ISysMenuService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MyTask implements Job {
    @Autowired
    private ISysMenuService sysMenuService;
    @Override
    @LogDb(business  = BusinessEnum.Collect)
    public void execute(JobExecutionContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("Task" + ">>" + sdf.format(new Date()) + ":" + context.getJobDetail().getKey() + "执行中..." + context.getJobDetail().getDescription());
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        log.info(mergedJobDataMap.getString("jobDataMapParam"));
        log.warn("警告信息:"+mergedJobDataMap.getString("jobDataMapParam"));
        log.error("自定义失败信息");
        long count = sysMenuService.count();
        long i = count/0;
    }
}