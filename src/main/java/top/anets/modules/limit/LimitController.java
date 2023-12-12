package top.anets.modules.limit;

import com.google.common.util.concurrent.RateLimiter;
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

    private final static RateLimiter limiter = RateLimiter.create(2.0); // 10

    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<10;i++){
            runs();
        }
        Thread.sleep(5000);
        for(int i=0;i<10;i++){
            runs();
        }
        Thread.sleep(5000);
        for(int i=0;i<10;i++){
            runs();
        }
        Thread.sleep(5000);
        for(int i=0;i<10;i++){
            runs();
        }
        Thread.sleep(5000);
        for(int i=0;i<10;i++){
            runs();
        }
        Thread.sleep(5000);
        for(int i=0;i<10;i++){
            runs();
        }
        Thread.sleep(5000);
        for(int i=0;i<10;i++){
            runs();
        }
    }
    public static  void  runs(){
        if(limiter.tryAcquire()){
            System.out.println(limiter.getRate());
        }else{
            System.out.println("被限流");
        }
    }
}
