package top.anets.support.mongodb.danamicdatasource;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author ftm
 * @date 2023/3/9 0009 12:33
 */

@Data
public class MongodbProperties {

    private String db;
    private String uri;
    private String username;
    private String password;
    private String authenticationDatabase;
    //    private String authenticationMechanism;
}