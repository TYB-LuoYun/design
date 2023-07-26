package top.anets.support.mongodb.danamicdatasource;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ftm
 * @date 2023/3/9 0009 12:33
 */

public class MongodbProperties {

    private String name;
    private String uri;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}