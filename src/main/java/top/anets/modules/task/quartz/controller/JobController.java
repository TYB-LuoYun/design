package top.anets.modules.task.quartz.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.quartz.JobDataMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.task.quartz.common.QuartzJobService;
import top.anets.modules.task.quartz.pojo.QuartzBean;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ftm
 * @date 2024-04-07 13:29
 */
@RestController
@RequestMapping("/jobs")
public class JobController {


    @Resource
    private QuartzJobService quartzJobService;


    @RequestMapping("test")
    public void test() throws Exception {
        for(int i = 0; i<5;i++){
            QuartzBean quartzBean = new QuartzBean();
            quartzBean.setJobClass("top.anets.modules.task.quartz.task.MyTask");
            quartzBean.setJobName("数据源"+i);
            quartzBean.setJobGroup("数据源组");
            quartzBean.setJobDescription("数据源"+i);

            JobDataMap map = new JobDataMap();
            map.put("jobDataMapParam", "数据源"+i);
            quartzBean.setJobDataMap(map);
            //10s中后开始
            Calendar newTimeStart = Calendar.getInstance();
            newTimeStart.setTime(new Date());
            newTimeStart.add(Calendar.SECOND, 10);
            quartzBean.setStartTime(newTimeStart.getTime());
            //50s后结束
            Calendar newTimeEnd = Calendar.getInstance();
            newTimeEnd.setTime(new Date());
            newTimeEnd.add(Calendar.SECOND, 150);
            quartzBean.setEndTime(newTimeEnd.getTime());
            quartzBean.setEndTime(null);
            //每隔3秒钟一次
            quartzBean.setInterval(i*2+3);
            quartzJobService.createScheduleSimpleJob(quartzBean);
        }
    }
}
