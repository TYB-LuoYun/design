package top.anets.modules.wx.service;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author: rain
 * 微信相关service服务类
 * 2023/3/7 16:15
 */
public interface WxService {
    String msgSecCheckVersion2(String content, int scene, String openid, String access_token);

    String mediaCheckAsyncVersion2(String media_url, int media_type, int scene, String openid, String access_token);

    JSONObject templatePush(String openid, JSONObject data, String mesPushTemplateId);

    JSONObject templatePush(String openid, Map<String, Object> map, String mesPushTemplateId, String appId, String secret);
}
