package top.anets.modules.wx.model.dto;

import lombok.Data;

@Data
public class WxPayDTO {
    /**
     * 商户号
     */
    String mchid;
    /**
     * 内部商户号
     */
    String out_trade_no;
    String appid;
    String description;
    /**
     * 回调url
     */
    String notify_url;
    /**
     * 订单总金额，单位为分。
     */
    int total;
    /**
     * 货币类型 CNY：人民币，境内商户号仅支持人民币。
     */
    String currency;
    String openid;
    String userId;
    /**
     * 预支付订单id
     */
    String prepay_id;
    /**
     * 是否指定分账，枚举值
     * true：是
     * false：否
     */
    Boolean profitSharing;
}
