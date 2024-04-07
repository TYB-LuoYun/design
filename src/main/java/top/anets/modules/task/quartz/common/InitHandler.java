package top.anets.modules.task.quartz.common;

/**
 * @author ftm
 * @date 2024-04-07 11:42
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
@Service
public class InitHandler {

    @Autowired
    private TaskInitHandler taskInitHandler;

    /**
     * 系统初始化任务入口。
     * 如果系统在启动时需要添加一些定时任务，把业务逻辑写到taskInitHandler.init()方法里即可
     */
    @PostConstruct
    public void init() {
        taskInitHandler.init();
    }
}