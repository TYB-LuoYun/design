package top.anets.modules.im.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Document("RoamMessage")
@Validated
public class RoamMsg {
    @Id
    private String _id;

    @NotNull
    @ApiModelProperty(value = "消息发送方账号")
    @JsonProperty("From_Account")
    private String from_Account;

    @NotNull
    @ApiModelProperty(value = "消息接收方账号")
    @JsonProperty("To_Account")
    private String to_Account;

    @ApiModelProperty(value = "消息序列号")
    @JsonProperty("MsgSeq")
    private String msgSeq;

    @ApiModelProperty(value = "消息随机数")
    @JsonProperty("MsgRandom")
    private Integer msgRandom;

    @ApiModelProperty(value = "消息时间戳，UNIX 时间戳，单位为秒")
    @JsonProperty("MsgTimeStamp")
    private Long msgTimeStamp;

    @JsonProperty("MsgClientTime")
    private Long msgClientTime;

    @JsonProperty("MsgFlagBits")
    @ApiModelProperty(value = "该条消息的属性，0表示正常消息，8表示被撤回的消息")
    private Integer msgFlagBits;

    @JsonProperty("IsPeerRead")
    @ApiModelProperty(value = "消息接收方是否发送该条消息的已读回执。0表示未发送，1表示已发送")
    private Integer isPeerRead;

    @JsonProperty("IsNeedReadReceipt")
    private Integer isNeedReadReceipt;

    @JsonProperty("SupportMessageExtension")
    private Integer supportMessageExtension;

    @ApiModelProperty(value = "消息类型 该字段只能填2或5")
    private Integer syncFromOldSystem;

    @JsonProperty("MsgKey")
    @ApiModelProperty(value = "标识该条消息")
    private String msgKey;

    @ApiModelProperty(value = "消息内容")
    @JsonProperty("MsgBody")
    private List<RoamMsgBody> msgBody;

    @JsonProperty("CloudCustomData")
    private String cloudCustomData;

    @JsonProperty("MsgVersion")
    private Integer msgVersion;

    @ApiModelProperty(value = "专家问诊表id")
    @JsonProperty("DoctorInquiryId")
    private String doctorInquiryId;

}
