package top.anets.modules.serviceMonitor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import top.anets.modules.messageService.MsgService;
import top.anets.modules.messageService.message.model.Msg;
import top.anets.modules.messageService.message.provider.DINGMessageProvider;
import top.anets.modules.serviceMonitor.server.*;
import top.anets.modules.serviceMonitor.utils.Arith;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.context.ApplicationListener;

/**
 * @author ftm
 * @date 2022/10/11 0011 15:26
 */
@RefreshScope
@Component
public class MonitorService  implements ApplicationListener<RefreshScopeRefreshedEvent> {
    @Value("${monitor-server.cpu:0.8}")
    private double maxCpu;


    @Value("${monitor-server.memory:0.8}")
    private double maxMemory;

    @Value("${monitor-server.hardDisk:0.8}")
    private double maxHardDisk;



    @Value("${monitor-server.jvm:0.8}")
    private double maxJvm;




    @Value("${monitor-server.schedule.enabled:false}")
    private Boolean isEnabled;

    //0 */1 * * * ? 每分钟执行一次
//    0/10 * * * * ?  每10s 执行一次
//    @Value("")
    private String monitorCron;
    @Scheduled(cron = "${monitor-server.schedule.cron:0 */1 * * * ?}")
    public void task(){
        if(isEnabled !=null && isEnabled == true){
            this.monitorAlert();
        }
    }

    /**
     * 监听-解决加 @RefreshScope  定时任务失效的问题
     * @param event
     */
    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        this.task();
    }


    @XxlJob("monitor-server")
    public void tasks(){
        this.monitorAlert();
    }


    /**
     * 服务预警
     */
    public void monitorAlert(){
        Server server = this.server();
        /**
         * cpu预警
         */
        Cpu cpu = server.getCpu();
        double use = Arith.sub(100, cpu.getFree());
        double useRate = Arith.div(use, 100, 2);
        if(useRate>maxCpu){
            this.alert("CPU预警:当前CPU使用率"+use+"%,已超过"+maxCpu*100+"%");

        }

        /**
         * 内存消息
         */
        Mem mem = server.getMem();
        double used = mem.getUsage();
        double memUseRate = Arith.div(used, 100, 2);
        if(memUseRate>maxMemory){
            this.alert("内存预警:当前内存使用率"+used+"%,已超过"+maxMemory*100+"%,剩余内存"+mem.getFree()+"G");
        }


        /**
         * jvm
         */
        Jvm jvm = server.getJvm();
        double usedJvm = jvm.getUsage();
        double memUseJvmRate = Arith.div(usedJvm, 100, 2);
        if(memUseJvmRate>maxJvm){
            this.alert("JVM预警:当前JVM使用率"+usedJvm+"%,已超过"+maxJvm*100+"%,JVM剩余空间"+jvm.getFree()+"M");
        }

        /**
         * 盘符
         */

        List<SysFile> sysFiles = server.getSysFiles();
        if(CollectionUtils.isNotEmpty(sysFiles)){
            sysFiles.forEach(item->{
                double usage = item.getUsage();
                double hardUseRate = Arith.div( usage, 100, 2);
                if(hardUseRate>maxHardDisk){
                    this.alert("磁盘预警:当前"+item.getTypeName()+"使用率"+usage+"%,已超过"+maxHardDisk*100+"%,可用大小"+item.getFree()+"");
                }
            });

        }

    }

    private void alert(String content) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //           预警
        Msg msg = new Msg();
        msg.setContent(content+"("+format.format(new Date())+")");
        MsgService.use(new DINGMessageProvider()).send(msg);
    }


    /**
     * 获取server信息
     */
    public Server server() {
        Server server = new Server();
        try{
            server.copyTo();
        }catch (Exception e){
            e.printStackTrace();
        }
        return server;
    }

    public static void main(String[] args){
        Server server = new MonitorService().server();
        System.out.println(JSON.toJSONString(server));
        Cpu cpu = server.getCpu();
    }
}
