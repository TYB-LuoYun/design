package top.anets.modules.highConcurrency;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.tableTestData.entity.TestData;
import top.anets.modules.tableTestData.service.ITestDataService;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ftm
 * @date 2023-08-17 10:31
 */
@Slf4j
@RestController
@RequestMapping("highConcurrency")
public class HighConcurrencyController {

    @Autowired
    private ITestDataService testDataService;



    @Resource
    private RedissonClient redisson;


    /**
     * 多个线程对同个金额并发 增加操作
     * 行锁-排他锁测试
     */
    @RequestMapping("money")
    public void money(){
        BigDecimal amount = BigDecimal.ZERO;
        TestData admin = testDataService.getById("admin");
        AtomicInteger atomicInteger = new AtomicInteger(admin.getBalance().intValue());

        for(int i = 0 ;i<10 ;i++){
            int finalI = i;
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    if(finalI == 0){
                        log.info("行锁");
                        testDataService.addBalanceSleep("admin",BigDecimal.ONE);
                    }else{
                        log.info("("+finalI+")" +"执行开始");
                        testDataService.addBalance("admin",BigDecimal.ONE);

                    }
                    log.info("("+finalI+")" +"增加金额:"+1);
                    int result = atomicInteger.addAndGet(1);
                    log.info("("+finalI+")" +"余额:"+result);

                }
            });
        }
    }



    @RequestMapping("redisson/lock")
    public String redissonLock(String key){
        RLock lock = redisson.getLock(key);       //获取锁
        try {
            lock.lock();    //上锁
            log.info("锁已开启"+key);
            Thread.sleep(20000);
            return key;
        }catch (Exception e){
            log.warn("系统错误，稍后重试");
        }
        finally {
            lock.unlock();    //删除锁
            log.info("锁已关闭"+key);
        }
        return null;

    }

}
