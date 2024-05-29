package top.anets.modules.wx.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WxPayRefundsDTO {
    @NotNull(message = "原支付交易对应的商户订单号不能为空")
    String out_trade_no;
    @NotNull(message = "退款原因不能为空")
    String reason;
    @NotNull(message = "退款金额不能为空")
    Integer refund;
    @NotNull(message = "原支付交易的订单总金额不能为空")
    Integer total;
    @NotNull(message = "目前只支持人民币：CNY不能为空")
    String currency;

    String out_refund_no;
    String transaction_id;
    String notify_url;
    String funds_account;
    String account;
    String merchant_goods_id;
    String wechatpay_goods_id;
    String goods_name;
    Integer amount = 0;
    Integer unit_price = 0;
    Integer refund_amount = 0;
    Integer refund_quantity = 0;
}
