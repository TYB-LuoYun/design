//package top.anets.modules.task;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * @author清梦
// * @site www.xiaomage.com
// * @company xxx公司
// * @create 2023-06-08 16:07
// */
//@Slf4j
//public class MyJob extends QuartzJobBean {
//
//    @Autowired
//    private TriggersMapper triggersMapper;
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        JobDetail jobDetail = jobExecutionContext.getJobDetail();
//        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        //获取参数信息
//        JobDataMap jobDataMap = jobDetail.getJobDataMap();
//        String name = jobDetail.getKey().getName();
//        String jobGroup = jobDetail.getKey().getGroup();
//        Iterator<Map.Entry<String, Object>> iterator = jobDataMap.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<String, Object> entry = iterator.next();
//            log.info("定时任务-{}:{}-启动，参数为：key：{} ，value：{}",name,jobGroup,entry.getKey(),entry.getValue());
//        }
//
//        Trigger cronTrigger = jobExecutionContext.getTrigger();
//        String group = cronTrigger.getKey().getGroup();
//        //有终止任务标记的修改状态为pause
//        if (group.contains(END_JOB_SUFFIX)){
//            String jobName = group.split(END_JOB_SUFFIX)[0];
//            log.info("jobName:{}",jobName);
//            QueryWrapper<Triggers> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("job_name",jobName);
//            List<Triggers> triggers = triggersMapper.selectList(queryWrapper);
//            for (Triggers trigger:triggers){
//                triggersMapper.updateTriggerState("PAUSED",trigger.getJobName(),trigger.getJobGroup());
//            }
//            return;
//        }
//    }
//}