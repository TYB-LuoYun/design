package top.anets.modules.task;

import org.quartz.*;
import org.quartz.spi.MutableTrigger;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.impl.matchers.GroupMatcher;
import top.anets.modules.task.entity.JobInfo;

import java.util.*;

import static org.quartz.Trigger.TriggerState.*;
/**
 * @author ftm
 * @date 2024-04-07 11:28
 */
@Slf4j
public class QuartzUtil {

    private static Map<Trigger.TriggerState, String> TRIGGER_STATE;

    public static final String END_JOB_SUFFIX = ":endTask";

    static {
        TRIGGER_STATE = new HashMap<>();
        TRIGGER_STATE.put(NONE, "空");
        TRIGGER_STATE.put(NORMAL, "正常");
        TRIGGER_STATE.put(PAUSED, "暂停");
        TRIGGER_STATE.put(COMPLETE, "完成");
        TRIGGER_STATE.put(ERROR, "错误");
        TRIGGER_STATE.put(BLOCKED, "阻塞");
    }

    /**
     * 创建定时任务，创建定时任务后默认为启动状态
     * @param scheduler 调度器
     * @param jobInfo 定时任务信息类
     * 原文链接：https://blog.csdn.net/yyyy11119/article/details/121139580
     */
    public static void createScheduleJob(Scheduler scheduler, JobInfo jobInfo)throws Exception{
        //获取定时任务的执行类，必须是类的绝对路径
        //定时任务类需要是job类的具体实现 QuartzJobBean是job的抽象类。
        Class<? extends Job> jobClass = (Class<? extends Job>)Class.forName(jobInfo.getJobClassName());
        //构建定时任务信息
        JobKey jobKey = JobKey.jobKey(jobInfo.getJobName(),jobInfo.getJobGroup());
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName(),jobInfo.getJobGroup());
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey).build();
        //设置定时任务的执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());
        //构建触发器trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withDescription(jobInfo.getDescription())
                .usingJobData("jobClassName",jobInfo.getJobClassName())
                .withSchedule(scheduleBuilder)
                .startAt(jobInfo.getStartTime())
                //如果设置了结束时间，到期任务自动删除，我们不想任务过期就删除，所以zhudiao
                //.endAt(jobInfo.getEndTime())
                .build();
        // 修改 misfire 策略,如果开始时间在添加任务之前，不修改策略会导致新增时任务会立即运行一次
        chgMisFire(trigger);
        //如果参数不为空，写入参数
        ObjectMapper mapper = new ObjectMapper();
        if (!StringUtils.isBlank(jobInfo.getParameter())) {
            Map param = mapper.readValue(jobInfo.getParameter(), Map.class);
            jobDetail.getJobDataMap().putAll(param);
        }
        scheduler.scheduleJob(jobDetail,trigger);
        //如果设置了结束时间，添加一个终止任务
        if (null != jobInfo.getEndTime()){
            addEndJob(scheduler,jobInfo);
        }
    }

    /**
     * 添加停止任务
     * @param scheduler 调度器
     * @param jobInfo 定时任务信息类
     */
    public static void addEndJob(Scheduler scheduler,JobInfo jobInfo)throws SchedulerException,JsonProcessingException,ClassNotFoundException{
        String jobName = jobInfo.getJobName();
        String group = jobName + END_JOB_SUFFIX;
        JobKey jobKey = JobKey.jobKey(jobName,group);
        String endJobName = "jobName-" + System.currentTimeMillis();
        TriggerKey triggerKey = TriggerKey.triggerKey(endJobName,group);
        if (scheduler.checkExists(triggerKey) && scheduler.checkExists(jobKey)) {
            log.info("{}----开始添加终止任务",jobName);
            SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withDescription(jobInfo.getDescription() + "-[结束任务]")
                    .withIdentity(triggerKey)
                    .startAt(jobInfo.getEndTime()).build();
            // 修改 misfire 策略
            chgMisFire(trigger);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            ObjectMapper mapper = new ObjectMapper();
            if (!StringUtils.isBlank(jobInfo.getParameter())) {
                Map param = mapper.readValue(jobInfo.getParameter(), Map.class);
                jobDetail.getJobDataMap().putAll(param);
            }
            scheduler.deleteJob(jobKey);
            scheduler.scheduleJob(jobDetail, trigger);
            return;
        }
        Class<? extends Job> jobClass = (Class<? extends Job>)Class.forName(jobInfo.getJobClassName());
        //构建定时任务信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                .withDescription(jobKey.getGroup() + "-[结束任务]")
                .startAt(jobInfo.getEndTime())
                .build();
        // 修改 misfire 策略
        chgMisFire(trigger);

        ObjectMapper mapper = new ObjectMapper();
        if (!StringUtils.isBlank(jobInfo.getParameter())) {
            log.info("parameter:{}",jobInfo.getParameter());
            Map param = mapper.readValue(jobInfo.getParameter(), Map.class);
            jobDetail.getJobDataMap().putAll(param);
        }
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 根据任务明细暂停定时任务
     * @param scheduler 调度器
     * @param jobName 定时任务名称
     */
    public static void pauseScheduleJob(Scheduler scheduler,String jobName,String jobGroup)throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 根据任务名称恢复定时任务
     * @param scheduler 调度器
     * @param jobName 任务名称
     */
    public static void resumeScheduleJob(Scheduler scheduler,String jobName,String jobGroup)throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 根据任务名称运行一次定时任务
     * @param scheduler 调度器
     * @param jobName 任务名称
     */
    public static void runOnce(Scheduler scheduler,String jobName,String jobGroup)throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新定时任务
     * @param scheduler 调度器
     * @param jobInfo 定时任务类
     */
    public static void updateScheduleJob(Scheduler scheduler,JobInfo jobInfo)throws SchedulerException,JsonProcessingException,ClassNotFoundException{
        //获取对应任务的触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName(),jobInfo.getJobGroup());
        //设置定时任务执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());
        //重新构建定时任务的触发器
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        cronTrigger = cronTrigger.getTriggerBuilder()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .withDescription(jobInfo.getDescription())
                .usingJobData("jobClassName",jobInfo.getJobClassName())
                .startAt(jobInfo.getStartTime())
                .endAt(jobInfo.getEndTime()).build();
        // 修改 misfire 策略,测试时注掉，方便观察
        //chgMisFire(trigger);
        //重置对应的定时任务
        //如果参数不为空，写入参数
        JobKey jobKey = JobKey.jobKey(jobInfo.getJobName(),jobInfo.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        ObjectMapper mapper = new ObjectMapper();
        if (!StringUtils.isBlank(jobInfo.getParameter())) {
            Map param = mapper.readValue(jobInfo.getParameter(), Map.class);
            jobDetail.getJobDataMap().clear();
            jobDetail.getJobDataMap().putAll(param);
        }
        scheduler.rescheduleJob(triggerKey,cronTrigger);
        //如果设置了结束时间，添加一个终止任务
        if (null != jobInfo.getEndTime()){
            addEndJob(scheduler,jobInfo);
        }
    }

    /**
     * 删除定时任务
     * @param scheduler 调度器
     * @param jobName 任务名称
     */
    public static void deleteScheduleJob(Scheduler scheduler,String jobName,String jobGroup)throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        scheduler.deleteJob(jobKey);
    }

    /**
     * 查询所有定时任务
     * @param scheduler 调度器
     */
    public static List<JobInfo> findAll(Scheduler scheduler,String jobGroup)throws SchedulerException{
        GroupMatcher<JobKey> matcher;
        if (StringUtils.isBlank(jobGroup)){
            matcher = GroupMatcher.anyGroup();
        }else {
            matcher = GroupMatcher.groupEquals(jobGroup);
        }

        List<JobInfo> list = new ArrayList<>();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey:jobKeys){
            JobInfo jobInfo = new JobInfo();
            jobInfo.setJobName(jobKey.getName());
            jobInfo.setJobGroup(jobKey.getGroup());
            TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup());
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //终止任务没有设置业务实现类
            if (!jobKey.getGroup().contains(END_JOB_SUFFIX)){
                jobInfo.setJobClassName(cronTrigger.getJobDataMap().getString("jobClassName"));
                System.out.println("className:" + scheduler.getJobDetail(jobKey).getJobDataMap().getString("jobClassName"));
                jobInfo.setDescription(cronTrigger.getDescription());
                jobInfo.setCronExpression(cronTrigger.getCronExpression());
                jobInfo.setStartTime(cronTrigger.getStartTime());
            }

            jobInfo.setJobStatus(getJobStatus(scheduler,triggerKey));
            JobDataMap map = scheduler.getJobDetail(jobKey).getJobDataMap();
            jobInfo.setParameter(JSONObject.toJSONString(map));

            //直接在cronTrigger设置结束时间，到期后会自动删除，我们用结束时间设置了一个定时任务，到期停止
            //jobInfo.setEndTime(cronTrigger.getEndTime());
            TriggerKey endJobTriggerKey = TriggerKey.triggerKey(jobKey.getName(),jobKey.getName() + END_JOB_SUFFIX);
            SimpleTrigger endJobTrigger = (SimpleTrigger) scheduler.getTrigger(endJobTriggerKey);
            if (null != endJobTrigger){
                jobInfo.setEndTime(endJobTrigger.getStartTime());
            }
            list.add(jobInfo);
        }
        return list;
    }

    /**
     * 获取定时任务状态
     * @param scheduler 调度器
     * @param triggerKey 触发器
     * @return
     * @throws SchedulerException
     */
    public static String getJobStatus(Scheduler scheduler, TriggerKey triggerKey) throws SchedulerException {
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        return TRIGGER_STATE.get(triggerState);
    }

    /**
     * 错过触发策略：即错过了定时任务的开始时间可以做什么，此处设置什么也不做，即过了开始时间也不执行
     * 如果不设置，默认为立即触发一次，例如设置定时任务开始时间为10点，没有设置结束时间，在10点后重启系统，
     * 如果不设置为CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING，那么开始时间在重启前的定时任务会全部重新触发一次
     * @param trigger
     */
    private static void chgMisFire(Trigger trigger) {
        // 修改 misfire 策略
        if (trigger instanceof MutableTrigger) {
            MutableTrigger mutableTrigger = (MutableTrigger) trigger;
            mutableTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        }
    }
}
