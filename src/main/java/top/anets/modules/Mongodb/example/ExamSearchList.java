package top.anets.modules.Mongodb.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author ftm
 * @date 2022/10/25 0025 16:45
 */

@Data
@Document(collection = "E_ExamSearchList")  //表名
public class ExamSearchList {
    @Id
    private String _id;
    /**
     * 报告id
     */
    private String ReportID;


}

