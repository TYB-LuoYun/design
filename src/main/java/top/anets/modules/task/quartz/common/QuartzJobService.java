package top.anets.modules.task.quartz.common;

/**
 * @author ftm
 * @date 2024-04-07 11:43
 */
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import top.anets.modules.task.quartz.pojo.QuartzBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 该类提供对quartz任务的创建、删除、查询、修改
 */
@Service
public class QuartzJobService {

    @Resource
    private Scheduler scheduler;

    /**
     * 查询定时任务列表
     *
     * @return
     */
    public List<QuartzBean> getScheduleJobList() {
        List<QuartzBean> list = new ArrayList<>();
        try {
            for (String groupJob : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))) {
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger : triggers) {
                        QuartzBean quartzBean = new QuartzBean();
                        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                        String cronExpression = "";
                        if (trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            cronExpression = cronTrigger.getCronExpression();
                            quartzBean.setCronExpression(cronExpression);
                        }
                        quartzBean.setStartTime(trigger.getStartTime());
                        quartzBean.setEndTime(trigger.getEndTime());
                        quartzBean.setJobName(jobKey.getName());
                        quartzBean.setJobGroup(jobKey.getGroup());
                        quartzBean.setJobDescription(jobDetail.getDescription());
                        quartzBean.setJobStatus(triggerState.name());
                        quartzBean.setJobClass(jobDetail.getJobClass().toGenericString());
                        quartzBean.setJobDataMap(jobDetail.getJobDataMap());
                        list.add(quartzBean);
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 创建简单调度任务
     *
     * @param quartzBean
     * @throws Exception
     */
    public void createScheduleSimpleJob(QuartzBean quartzBean) throws Exception {
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(quartzBean.getJobClass());
        //job
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(quartzBean.getJobName(), quartzBean.getJobGroup())
                .setJobData(quartzBean.getJobDataMap())
                .withDescription(quartzBean.getJobDescription())
                .build();
        //trigger
        Trigger trigger = null;
        //单次还是循环
        if (quartzBean.getInterval() == null) {
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzBean.getJobName(), quartzBean.getJobGroup())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionNextWithExistingCount())
                    .startAt(quartzBean.getStartTime())
                    .build();
        } else {
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzBean.getJobName(), quartzBean.getJobGroup())
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(quartzBean.getInterval())
                            .withMisfireHandlingInstructionNextWithExistingCount())
                    .startAt(quartzBean.getStartTime())
                    .endAt(quartzBean.getEndTime())
                    .build();
        }
        scheduler.scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * 创建cron调度任务
     *
     * @param quartzBean
     * @throws Exception
     */
    public void createScheduleCronJob(QuartzBean quartzBean) throws Exception {
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(quartzBean.getJobClass());
        // job
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(quartzBean.getJobName(), quartzBean.getJobGroup())
                .setJobData(quartzBean.getJobDataMap())
                .withDescription(quartzBean.getJobDescription())
                .build();
        // trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzBean.getJobName(), quartzBean.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzBean.getCronExpression()))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * 暂停任务
     *
     * @param jobName
     * @param jobGroup
     * @throws Exception
     */
    public void pauseScheduleJob(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
    }


    /**
     * 恢复任务
     *
     * @param jobName
     * @param jobGroup
     * @throws Exception
     */
    public void  resumeScheduleJob(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 立即执行任务
     *
     * @param jobName
     * @param jobGroup
     * @throws Exception
     */
    public void runJob(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新简单任务
     *
     * @param quartzBean
     * @throws Exception
     */
    public void updateScheduleSimpleJob(QuartzBean quartzBean) throws Exception {
        //原任务触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzBean.getJobName(), quartzBean.getJobGroup());
        //trigger
        Trigger trigger = null;
        //单次还是循环
        if (quartzBean.getInterval() == null) {
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzBean.getJobName(), quartzBean.getJobGroup())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionNextWithExistingCount())
                    .startAt(quartzBean.getStartTime())
                    .build();
        } else {
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzBean.getJobName(), quartzBean.getJobGroup())
                    .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(quartzBean.getInterval()).withMisfireHandlingInstructionNextWithExistingCount())
                    .startAt(quartzBean.getStartTime())
                    .endAt(quartzBean.getEndTime())
                    .build();
        }
        //重置对应的job
        scheduler.rescheduleJob(triggerKey, trigger);
        //用该方法一样可以修改定时任务
        //scheduler.scheduleJob(jobDetail, trigger, true);
    }

    /**
     * 更新定时任务Cron
     *
     * @param quartzBean 定时任务信息类
     * @throws SchedulerException
     */
    public void updateScheduleCronJob(QuartzBean quartzBean) throws Exception {
        //原任务触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzBean.getJobName(),quartzBean.getJobGroup());
        // trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzBean.getJobName())
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzBean.getCronExpression()))
                .build();
        //重置对应的job
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 删除定时任务
     *
     * @param jobName
     * @param jobGroup
     * @throws Exception
     */
    public void deleteScheduleJob(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
    }

    /**
     * 获取任务状态
     * (" BLOCKED ", " 阻塞 ");
     * ("COMPLETE", "完成");
     * ("ERROR", "出错");
     * ("NONE", "不存在");
     * ("NORMAL", "正常");
     * ("PAUSED", "暂停");
     */
    public String getScheduleJobStatus(String jobName, String jobGroup) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
        return state.name();
    }

    /**
     * 检查任务是否存在
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    public Boolean checkExistsScheduleJob(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        return scheduler.checkExists(jobKey);
    }
}