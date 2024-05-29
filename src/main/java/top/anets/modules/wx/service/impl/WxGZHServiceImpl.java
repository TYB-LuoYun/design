package top.anets.modules.wx.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import top.anets.exception.ServiceException;
import top.anets.modules.wx.service.WxGZHService;
import top.anets.utils.HttpClientUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: rain
 * 微信公众号服务实现类
 * 2024/3/22
 */
@RefreshScope
@Service
public class WxGZHServiceImpl implements WxGZHService {
    private Logger log = LoggerFactory.getLogger(WxGZHServiceImpl.class);

    //融御科技公众号
    @Value("${gzh.appId:wx8f7f5f674ca9fad0}")
    private String gzhAppId;

    //融御科技公众号
    @Value("${gzh.appSecret:f65244ae3fa9cb9009e766314155d5cb}")
    private String gzhAppSecret;

    /**
     * 根据标签名获取标签id
     *
     * @param tagName
     * @return
     */
    @Override
    public String getTagIdByName(String access_token, String tagName) {
        if (StringUtils.isEmpty(access_token)) {
            access_token = this.getAccessToken();
        }
        String url = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=" + access_token;
        try {
            /**
             * {
             * "tags":[{
             *     "id":1,
             *     "name":"每天一罐可乐星人",
             *     "count":0 //此标签下粉丝数
             * },
             * {
             *     "id":127,
             *     "name":"广东",
             *     "count":5
             *  }
             * ] }
             */
            String tagId = "";
            String result = HttpClientUtil.doGet(url, null, "utf-8");
            if (StringUtils.isEmpty(result)) {
                return tagId;
            }
            JSONObject resultJson = JSONObject.parseObject(result);
            JSONArray tags = resultJson.getJSONArray("tags");
            for (Object tag : tags) {
                JSONObject tagJson = (JSONObject) tag;
                String tagNameOfArr = tagJson.getString("name");
                if (tagName.equals(tagNameOfArr)) {
                    tagId = tagJson.getString("id");
                }
            }
            return tagId;
        } catch (Exception e) {
            throw new ServiceException(e.toString());
        }
    }

    /**
     * 获取公众号token
     *
     * @return
     */
    @Override
    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + gzhAppId + "&secret=" + gzhAppSecret;
        /**
         * {
         * 	"access_token":"ACCESS_TOKEN",
         * 	"expires_in":7200
         * }
         */
        try {
            String result = HttpClientUtil.doGet(url, null, "utf-8");
            JSONObject resultJson = JSONObject.parseObject(result);
            String access_token = resultJson.getString("access_token");
            return access_token;
        } catch (Exception e) {
            throw new ServiceException(e.toString());
        }
    }

    /**
     * 获取标签下的用户列表的信息
     *
     * @param tagId       标签id
     * @param next_openid 第一个拉取的OPENID，不填默认从头开始拉取(不填时传空字符串)
     * @return
     */
    @Override
    public JSONObject getTagUsers(String access_token, String tagId, String next_openid) {
        if (StringUtils.isEmpty(access_token)) {
            access_token = this.getAccessToken();
        }
        String url = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=" + access_token;
        /**
         * {
         *     "count":2,//这次获取的粉丝数量
         *     "data":{//粉丝列表
         *     "openid":[
         *     "ocYxcuAEy30bX0NXmGn4ypqx3tI0",
         *     "ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"  ]
         * },
         *     "next_openid":"ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"//拉取列表最后一个用户的openid
         * }
         */
        Map<String, Object> param = new HashMap<>();
        param.put("tagid", tagId);
        param.put("next_openid", next_openid);
        String result = HttpClientUtil.doPost(url, JSONObject.toJSONString(param), "utf-8");
        return JSONObject.parseObject(result);
    }

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
    @Override
    public void gzhMsgPush(String accessToken, String gzhOpenid, String gzhTemplateId, String redirectUrl, String miniAppId, String miniPagePath, String data) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        String redirectUrlJSONStr = StringUtils.isNotEmpty(redirectUrl) ? "  \"url\":\"" + redirectUrl + "\",\n" : "";
        String miniprogram = StringUtils.isNotEmpty(miniAppId) && StringUtils.isNotEmpty(miniPagePath) ? "  \"miniprogram\":{\n" +
                "             \"appid\":\"" + miniAppId + "\",\n" +
                "             \"pagepath\":\"" + miniPagePath + "\"\n" +
                "  },\n" : "";
        String reqJSONStr = "{\n" +
                "  \"touser\":\"" + gzhOpenid + "\",\n" +
                "  \"template_id\":\"" + gzhTemplateId + "\",\n" + redirectUrlJSONStr + miniprogram + data + "}";
        String result = HttpClientUtil.doPost(url, reqJSONStr, "utf-8");
        JSONObject resultJson = JSONObject.parseObject(result);
        int errcode = resultJson.getIntValue("errcode");
        String errmsg = resultJson.getString("errmsg");
        if (0 == errcode && "ok".equals(errmsg)) {
            log.info("调用微信公众号推送模板消息成功");
        } else {
            log.info("调用微信公众号推送模板消息失败" + "返回结果为：" + result);
        }
    }

    /**
     * 获取所有关注公众号用户的openid的集合
     * @param access_token
     * @return
     */
    public List<String> getAllUsersOpenidList(String access_token) {
        if (StringUtils.isEmpty(access_token)) {
            access_token = this.getAccessToken();
        }
        //next_openid 上一批列表的最后一个OPENID，不填默认从头开始拉取
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + access_token + "&next_openid=";
        /**
         * {
         *     "total":2,
         *     "count":2,
         *     "data":{
         *     "openid":["OPENID1","OPENID2"]},
         *     "next_openid":"NEXT_OPENID"
         * }
         */
        String result = HttpClientUtil.doGet(url, null, "utf-8");
        JSONObject resultJson = JSONObject.parseObject(result);
        List<String> openidList = resultJson.getList("openid", String.class);
        return openidList;
    }
}
