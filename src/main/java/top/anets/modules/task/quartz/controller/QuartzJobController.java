package top.anets.modules.task.quartz.controller;

/**
 * @author ftm
 * @date 2024-04-07 11:47
 */
import org.quartz.JobDataMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.task.quartz.common.QuartzJobService;
import top.anets.modules.task.quartz.pojo.QuartzBean;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("quartz")
public class QuartzJobController {

    @Resource
    private QuartzJobService quartzJobService;


    @GetMapping("get")
    public List<QuartzBean> getQuartzList() {
        return quartzJobService.getScheduleJobList();
    }

    /**
     * 创建简单的任务
     *
     * @param jobName        任务名称
     * @param jobGroup       任务组名称
     * @param jobDescription 任务描述
     * @return
     * @throws Exception
     */
    @GetMapping("createSimpleJob")
    public String createSimpleJob(String jobName, String jobGroup, String jobDescription) throws Exception {
        QuartzBean quartzBean = new QuartzBean();
        quartzBean.setJobClass("cn.yx.zg.task.MyTask");
        quartzBean.setJobName(jobName);
        quartzBean.setJobGroup(jobGroup);
        quartzBean.setJobDescription(jobDescription);

        JobDataMap map = new JobDataMap();
        map.put("jobDataMapParam", "aaa");
        quartzBean.setJobDataMap(map);
        //10s中后开始
        Calendar newTimeStart = Calendar.getInstance();
        newTimeStart.setTime(new Date());
        newTimeStart.add(Calendar.SECOND, 10);
        quartzBean.setStartTime(newTimeStart.getTime());
        //50s后结束
        Calendar newTimeEnd = Calendar.getInstance();
        newTimeEnd.setTime(new Date());
        newTimeEnd.add(Calendar.SECOND, 50);
        quartzBean.setEndTime(newTimeEnd.getTime());
        quartzBean.setEndTime(null);
        //每隔3秒钟一次
        quartzBean.setInterval(3);
        quartzJobService.createScheduleSimpleJob(quartzBean);
        return "SUCCESS";
    }

    /**
     * 获取任务状态
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    @GetMapping("/getScheduleJobStatus")
    public String getScheduleJobStatus(String jobName, String jobGroup) throws Exception {
        /**
         * ("BLOCKED", " 阻塞 ");
         * ("COMPLETE", "完成");
         * ("ERROR", "出错");
         * ("NONE", "不存在");
         * ("NORMAL", "正常");
         * ("PAUSED", "暂停");
         */
        return quartzJobService.getScheduleJobStatus(jobName, jobGroup);
    }

    /**
     * 创建Cron的任务
     *
     * @param jobName
     * @param jobGroup
     * @param jobDescription
     * @return
     * @throws Exception
     */
    @GetMapping("/createCronJob")
    public String createCronJob(String jobName, String jobGroup, String jobDescription) throws Exception {
        QuartzBean quartzBean = new QuartzBean();
        quartzBean.setJobClass("cn.yx.zg.task.MyTask");
        quartzBean.setJobName(jobName);
        quartzBean.setJobGroup(jobGroup);
        quartzBean.setJobDescription(jobDescription);
        quartzBean.setCronExpression("*/10 * * * * ?");
        JobDataMap map = new JobDataMap();
        map.put("jobDataMapParam", "aaa");
        quartzBean.setJobDataMap(map);
        quartzJobService.createScheduleCronJob(quartzBean);
        return "SUCCESS";
    }


    public void updateCronJob(){

    }

    /**
     * 删除任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/delete")
    public String delete(String jobName, String jobGroup) throws Exception {
        quartzJobService.deleteScheduleJob(jobName, jobGroup);
        return "SUCCESS";
    }

    /**
     * 立即执行定时任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/runJob")
    public String runJob(String jobName, String jobGroup) throws Exception {
        quartzJobService.runJob(jobName, jobGroup);
        return "SUCCESS";
    }

    /**
     * 暂停定时任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/pauseScheduleJob")
    public String pauseScheduleJob(String jobName, String jobGroup) throws Exception {
        quartzJobService.pauseScheduleJob(jobName, jobGroup);
        return "SUCCESS";
    }

    /**
     * 恢复运行定时任务
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/resumeScheduleJob")
    public String resumeScheduleJob(String jobName, String jobGroup) throws Exception {
        quartzJobService.resumeScheduleJob(jobName,jobGroup );
        return "SUCCESS";
    }

    /**
     * 校验定时任务是否存在
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    @GetMapping(value = "check")
    public String check(String jobName, String jobGroup) throws Exception {
        if (quartzJobService.checkExistsScheduleJob(jobName, jobGroup)) {
            return "存在定时任务：" + jobName;
        } else {
            return "不存在定时任务：" + jobName;
        }
    }

    /**
     * 更新定时任务，写法是一样的，直接调用quartzJobService里更新的方法即可
     */


}