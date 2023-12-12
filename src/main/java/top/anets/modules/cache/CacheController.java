package top.anets.modules.cache;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ftm
 * @date 2023-11-15 17:39
 */
@RestController
@RequestMapping("cache")
public class CacheController {
    @RequestMapping("cache")
    @Cache(async = true)
    public String cache(String id,String code){
        return null;
    }
}
