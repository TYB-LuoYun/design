package top.anets.modules.wx.model.gzh;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 * 公众号消息
 * @author rain
 * @since 2024-04-09
 */
@Data
@TableName("mp_gzh_messages")
@ApiModel(value = "MpGzhMessages对象", description = "")
public class MpGzhMessages implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId("id")
    private String id;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("标签名")
    @TableField("tagName")
    private String tagName;

    @ApiModelProperty("发送用户类型 1小程序用户 2全部关注公众号用户 3指定标签用户")
    @TableField("userType")
    private Integer userType;

    @ApiModelProperty("携带的重定向url类型 1模板页面 2外部页面")
    @TableField("redirectUrlType")
    private Integer redirectUrlType;

    @ApiModelProperty("重定向地址url")
    @TableField("redirectUrl")
    private String redirectUrl;

    @ApiModelProperty("模板页面内容，当携带的重定向url类型为外部页面时，此项为空")
    @TableField("content")
    private String content;

    @ApiModelProperty("发送状态 1已发送 0未发送")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("发送时间")
    @TableField("sendAt")
    private Date sendAt;

    @ApiModelProperty("添加时间")
    @TableField("addAt")
    private Date addAt;
}




