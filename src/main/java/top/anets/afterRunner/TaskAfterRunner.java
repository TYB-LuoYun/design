package top.anets.afterRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author ftm
 * @date 2022/10/26 0026 15:18
 */
@Component
@Order(value = 1)
public class TaskAfterRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("项目已启动完成");
    }



}
