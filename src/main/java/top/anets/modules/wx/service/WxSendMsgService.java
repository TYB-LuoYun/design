package top.anets.modules.wx.service;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author: rain
 * 微信发送模板消息的service类
 */
public interface WxSendMsgService {

    /**
     * 向对应的患者发送微信消息Api
     *
     * @param title:标题
     * @param doctorName:医生姓名
     * @param patientAppId:患者端小程序id
     * @param patientOpenid:患者在患者端小程序的openid
     */
    JSONObject sendWxMsgApi(String title, String doctorName, String patientAppId, String patientOpenid);

    /**
     * 根据appId获取对应的消息模板id和秘钥
     *
     * @param appId
     * @return
     */
    Map<String, String> getPushTemplateId(String appId);
}
