package top.anets.modules.wx.model.vo;

import lombok.Data;

@Data
public class WxPaymentVO {
    long timeStamp;
    String nonceStr;
    String packageStr;
    String signType;
    String paySign;
}
