package top.anets.modules.limit2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuoYun
 * @since 2022/7/6 14:09
 * 分布式 限流
 */
@RestController
public class LimitLuaController {
    @RedisLimit(limitType = LimitType.CUSTOMER, key = "cachingTest", count = 2, period = 2, msg = "当前排队人数较多，请稍后再试！")
    @RequestMapping("limitTest")
    public String test(){
        return "ok";
    }
}
