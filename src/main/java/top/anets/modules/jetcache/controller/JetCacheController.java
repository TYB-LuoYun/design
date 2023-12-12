package top.anets.modules.jetcache.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author ftm
 * @date 2023-11-15 15:59
 */
@RestController
@RequestMapping("jetcache")
public class JetCacheController {
    @RequestMapping("get")
    public String get(Long id,String code){
        System.out.println("没进缓存");
        return "cache";
    }
}
