package top.anets.modules.log.appender;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import top.anets.support.mongodb.collection.CleanUpCollection;
import top.anets.support.mongodb.danamicdatasource.MongoDB;

import java.util.Date;

/**
 * @author ftm
 * @date 2024-04-08 17:31
 */
@Data
@CleanUpCollection(mongoDB = MongoDB.LOGDB)
@Document(collection = "Loginfo")//mongo对应集合名称
public class Loginfo {
    @Id
    private String _id;
    //    操作业务名
    private String business ;
    //    分类
    private String classify;

    private String loggerName;

    private String location;

    private Integer lineNumber;

    private String method;

    private String threadName;

    private String level;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Indexed(expireAfterSeconds = 2000000) // TTL索引，108000秒 = 30小时,添加@Indexed后在MongoDB中未创建索引，原因是配置中的auto-index-creation未设置为true。
    private Date time;
}
