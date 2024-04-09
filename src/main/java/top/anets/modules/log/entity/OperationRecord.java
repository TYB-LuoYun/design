package top.anets.modules.log.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import top.anets.utils.ServletUtil;

import java.util.Date;

/**
 * @author ftm
 * @date 2023-12-07 13:47
 * 操作记录
 */
@Data
@Document(collection = "HrOperationRecord")//mongo对应集合名称
public class OperationRecord {
    @Id
    private String _id;
//    操作业务名
    private String BusinessName ;
//    操作业务编码
    private String BusinessCode;
//    请求路径
    private String RequestUrl;
//    请求方式
    private String RequestMethod;
//    请求参数
    private String RequestParam;

//    操作状态
    private String Status;

    private String  Msg;

    private String StackTrace;
//    操作结果记录(0本院1跨院)
    private String ResultRecord;

//    结果记录描述
    private String ResultDesc;

//    操作人员
    private String UserCode;
//    操作员姓名
    private String UserName;
    private String Ip;
//    操作时间
    private Date OperationTime;
//    耗时
    private Long KillTime;

    private String OrganCode;

    public static void recordResult(String data, String desc) {
        ServletUtil.getRequest().setAttribute("LOG-RDATA",data );
        ServletUtil.getRequest().setAttribute("LOG-RDESC",desc );
    }
}
