package top.anets.modules.log.appender;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author ftm
 * @date 2024-04-08 17:31
 */
@Data
@Document(collection = "Loginfo")//mongo对应集合名称
public class Loginfo {
    @Id
    private String _id;
    //    操作业务名
    private String business ;
    //    操作业务编码
    private String service;

    private String loggerName;

    private String location;

    private Integer lineNumber;

    private String method;

    private String threadName;

    private String level;

    private String content;

    private Date time;
}
