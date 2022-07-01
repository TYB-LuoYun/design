package top.anets.modules.aopRsaAes;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuoYun
 * @since 2022/6/23 17:48
 */
@RestController("/rsa")
public class RsaController {
    @RequestMapping("/test")
    public void test(  @RsaParam("stationType") Integer stationType,@RsaParam("statisticType")Integer statisticType){
        return;
    }
}
