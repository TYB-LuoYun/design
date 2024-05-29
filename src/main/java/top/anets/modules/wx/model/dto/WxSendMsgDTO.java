package top.anets.modules.wx.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxSendMsgDTO {
    //标题
    @NotBlank(message = "标题不能为空")
    String title;
    //医生姓名
    @NotBlank(message = "医生姓名不能为空")
    String doctorName;
    //患者端小程序id
    @NotBlank(message = "患者端小程序id不能为空")
    String patientAppId;
    //患者在患者端小程序的openid
    @NotBlank(message = "患者在患者端小程序的openid不能为空")
    String patientOpenid;
}
