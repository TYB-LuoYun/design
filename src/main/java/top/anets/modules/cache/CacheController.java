package top.anets.modules.cache;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.cache.enums.Cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ftm
 * @date 2023-11-15 17:39
 */
@RestController
@RequestMapping("cache")
public class CacheController {
    @RequestMapping("cache")
    @Cache(name = "cache",key = "#id")
    public Object cache(String id,String code){
        SystemSql.put(id,code );
        return SystemSql;
    }
    public static ConcurrentHashMap<String,String> SystemSql =  new ConcurrentHashMap<String,String>();
    @RequestMapping("cacheUpdate")
    @CacheUpdate(name = "cache",key = "#id")
    public Object updatecache(String id,String code){
        SystemSql.put(id,code );
        return SystemSql;
    }
}
