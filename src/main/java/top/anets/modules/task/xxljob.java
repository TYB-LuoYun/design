//package top.anets.modules.task;
//
//import com.xxl.job.core.handler.annotation.XxlJob;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Map;
//
///**
// * @author ftm
// * @date 2024-04-03 17:09
// */
//@RestController
//@RequestMapping("/xxljob")
//public class xxljob {
//
//    @Autowired
//    private XxlJobService xxlJobService;
//
//    //添加任务并运行
//    @PostMapping("/addTask")
//    @ResponseBody
//    public void addTask(@RequestBody Map data) {
//        try {
//            /**
//             customId – 自定义的唯一的业务id，此id在所有任务中保持唯一,注意：这个唯一标识被放到了负责人字段
//             triggerTime – 任务执行时间，必须大于当前时间
//             executorParam – 任务执行参数
//             executorHandler – 任务处理器，关联@XxlJob的value
//             */
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Integer taskId = xxlJobService.addJustExecuteOnceJob("1",dateFormat.parse("2023-08-04 13:45:00"),"我是执行的参数","testHandler");
//            //开始运行任务
//            xxlJobService.start(taskId);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //删除
//    @PostMapping("/deleteTask")
//    @ResponseBody
//    public void deleteTask(@RequestBody Map data) {
//        try {
//            //通过自定义的唯一的业务id获取任务的id
//            Integer taskId = xxlJobService.getJobIdByCustomId("1");
//            //删除任务
//            xxlJobService.remove(taskId);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //任务处理器
//    @XxlJob("testHandler")
//    public void test() {
//        System.out.println("计划任务开始执行");
//        // 获取任务参数
//        String param = XxlJobHelper.getJobParam();
//        System.out.println("计划任务传输过来的参数 ------" + param);
//    }
//}
