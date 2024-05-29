package top.anets.modules.im.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mp_message_record")
@ApiModel(value = "MessageRecord对象", description = "专家问诊消息记录表，用于下载消息")
public class MessageRecord implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "消息发送方")
    @TableField("Operator_Account")
    private String patientId;

    @ApiModelProperty(value = "消息接收方")
    @TableField("Peer_Account")
    private String doctorId;

    @ApiModelProperty("开始时间")
    @TableField("MinTime")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @TableField("MaxTime")
    private Date endTime;

    @TableField("chatId")
    private String chatId;

    @ApiModelProperty(value = "专家问诊表id")
    @TableField(exist = false)
    private String doctorInquiryId;

    @TableField("isSuccess")
    private Integer isSuccess;

}
