package top.anets.modules.log.appender;

import lombok.Data;

/**
 * @author ftm
 * @date 2024-04-08 17:52
 */
@Data
public class NeedLog {
    //所属服务
    private String service ;
    //所属业务
    private String business;
    private String method;
}
