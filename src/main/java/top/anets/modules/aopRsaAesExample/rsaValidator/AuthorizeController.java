package top.anets.modules.aopRsaAesExample.rsaValidator;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * @author ftm
 * @date 2022/11/22 0022 11:37
 */


@RestController
@RequestMapping("/authorize")
public class AuthorizeController {

//    @RequestMapping("test")
//    @AuthorizeToken
//    public String test() {
//        return "ok";
//    }
//

    @Rsa
    @RequestMapping("rsa")
    public Map rsa(@RequestBody Map rsa) {
        return rsa;
    }



}