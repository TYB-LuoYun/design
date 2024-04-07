package top.anets.modules.task.quartz.common;

/**
 * @author ftm
 * @date 2024-04-07 11:43
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskInitHandler {

    public void init() {
        log.info("初始化定时任务业务逻辑......");
    }
}