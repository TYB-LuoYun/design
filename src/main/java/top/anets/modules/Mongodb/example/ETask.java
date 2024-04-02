package top.anets.modules.Mongodb.example;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import top.anets.support.mongodb.collection.CappedCollection;
import top.anets.support.mongodb.danamicdatasource.MongoDB;

import java.util.Date;

/**
 * @author ftm
 * @date 2022/10/25 0025 16:45
 */

@Data
@CappedCollection(maxSize = 100,maxDocs = 2)
@Document(collection = "ETask")  //表名
public class ETask {
    @Id
    private String _id;
    private String content;

//    @Indexed(expireAfterSeconds = 60) // TTL索引，108000秒 = 30小时,添加@Indexed后在MongoDB中未创建索引，原因是配置中的auto-index-creation未设置为true。
    @CreatedDate
    private Date time;
}

