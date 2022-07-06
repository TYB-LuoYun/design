package top.anets.modules.limit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuoYun
 * @since 2022/7/6 11:34
 */
@RestController
@TAop
public class LimitController {
    @RequestMapping("testLimit")
    @RequestLimit(time = 30,maxCount = 1)
    public String testLimit() throws InterruptedException {
        System.out.println("你好=======");
        Thread.sleep(20000);
        System.out.println("结束=======");
        return "ok";
    }
}
