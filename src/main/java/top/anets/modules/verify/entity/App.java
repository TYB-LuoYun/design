package top.anets.modules.verify.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ftm
 * @since 2023-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("app")
@ApiModel(value="App对象", description="")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组织编码")
    private String organCode;

    @TableId
    @ApiModelProperty(value = "厂商编码")
    private String appId;


    private Long hospitalId;

    @ApiModelProperty(value = "组织名称")
    private String organName;

    @ApiModelProperty(value = "授权码")
    private String appSecret;



    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private Date createTime;



    @TableField(exist = false)
    private Long confServeId;

    @TableLogic
    @TableField("delete_flag")
    private Integer deleteFlag;

}
