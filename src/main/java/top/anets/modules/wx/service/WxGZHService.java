package top.anets.modules.wx.service;


import com.alibaba.fastjson2.JSONObject;

/**
 * @author: rain
 * 微信公众号服务类
 * 2024/3/22
 */
public interface WxGZHService {
    /**
     * 根据标签名获取标签id
     *
     * @param tagName
     * @return
     */
    String getTagIdByName(String access_token, String tagName);

    /**
     * 获取公众号token
     *
     * @return
     */
    String getAccessToken();

    /**
     * 获取标签下的用户列表的信息
     *
     * @param tagId       标签id
     * @param next_openid 第一个拉取的OPENID，不填默认从头开始拉取(不填时传空字符串)
     * @return
     */
    JSONObject getTagUsers(String access_token, String tagId, String next_openid);

    /**
     * 公众号消息推送
     *
     * @param accessToken   请求微信公众号接口所需token
     * @param gzhOpenid     微信用户关注公众号之后会获取到openid
     * @param gzhTemplateId 公众号消息发送的模板id
     * @param redirectUrl   模板跳转链接（海外账号没有跳转能力）（非必填）
     * @param miniAppId     跳转至小程序的appid（非必填）
     * @param miniPagePath  跳转至小程序页面的路由 like:index?foo=bar（非必填）
     * @param data          发送的消息内容字符串
     */
    void gzhMsgPush(String accessToken, String gzhOpenid, String gzhTemplateId, String redirectUrl, String miniAppId, String miniPagePath, String data);
}
