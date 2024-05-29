package top.anets.modules.wx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxUserInfo {
//    @ApiModelProperty("openid")
    private String openid;

//    @ApiModelProperty("用户昵称")
    private String nickName;

//    @ApiModelProperty("头像地址")
    private String avatarUrl;

//    @ApiModelProperty("用户性别")
    private String gender;

//    @ApiModelProperty("省份")
    private String province;

//    @ApiModelProperty("城市")
    private String city;

//    @ApiModelProperty("区")
    private String district;

//    @ApiModelProperty("手机号")
    private String phone;

//    @ApiModelProperty("用户实名")
    private String name;

//    @ApiModelProperty("身份证号")
    private String sfznum;

//    @ApiModelProperty("用户地址")
    private String address;

    //微信code
    private String code;

    //获取手机号的code
    private String phoneCode;

    //用户类型
    private String userCategory;
}
