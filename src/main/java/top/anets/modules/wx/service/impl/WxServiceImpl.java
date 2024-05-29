package top.anets.modules.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.anets.exception.ServiceException;
import top.anets.modules.wx.WxUtil;
import top.anets.modules.wx.service.WxService;
import top.anets.utils.Constants;
import top.anets.utils.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: rain
 * 微信相关service服务实现类
 * 2023/3/7 16:15
 */
@RefreshScope
@Service
public class WxServiceImpl implements WxService {
    private final Logger log = LoggerFactory.getLogger(WxServiceImpl.class);


    @Value("${wechat.messagePush.programState:}")
    private String mesPushProgramState;

    @Value("${wechat.domain:}")
    private String wechatDomain;

    @Override
    public String msgSecCheckVersion2(String content, int scene, String openid, String access_token) {
        String returnStr = "文字审核通过";
        Map<String, Object> reqMap = new HashMap<>();
        int version = 2;
        reqMap.put("content", content);
        reqMap.put("version", version);
        reqMap.put("scene", scene);
        reqMap.put("openid", openid);
        String requestUrl = Constants.MSGSECCHECK_URL + access_token;
        String returnJson = HttpClientUtil.doPost(requestUrl, JSON.toJSONString(reqMap), "UTF-8");
        if (!StringUtils.isEmpty(returnJson)) {
            JSONObject data = (JSONObject) JSONObject.parse(returnJson);
            log.info("文本内容安全识别接口返回结果为：" + data);
            Object resultObj = data.get("result");
            if (ObjectUtils.isNotEmpty(resultObj)) {
                String result = JSON.toJSONString(resultObj);
                JSONObject resultJsonObj = JSONObject.parseObject(result);
                Object label = resultJsonObj.get("label");
                if (ObjectUtils.isNotEmpty(label)) {
                    if (!Constants.INT_ONE_HUNDRED.equals(label)) {
                        returnStr = "您发帖中的文字包含敏感词汇";
                    }
                }
            }
        }
        return returnStr;
    }

    /**
     * @param media_url:    文件url
     * @param media_type:   图片内容安全识别文件类型 1:音频;2:图片
     * @param scene:        图片安全识别枚举值（1 资料；2 评论；3 论坛；4 社交日志）
     * @param openid:       微信openid
     * @param access_token: 微信token
     * @return String
     * 微信图片影音校验版本2.0接口实现
     * rain
     * 2023/3/7 16:24
     */
    @Override
    public String mediaCheckAsyncVersion2(String media_url, int media_type, int scene, String openid, String access_token) {
        String traceId = "";
        Map<String, Object> reqMap = new HashMap<>();
        int version = 2;
        reqMap.put("media_url", media_url);
        reqMap.put("media_type", media_type);
        reqMap.put("version", version);
        reqMap.put("scene", scene);
        reqMap.put("openid", openid);
        String requestUrl = Constants.MEDIACHECKASYNC_URL + access_token;
        String returnJson = HttpClientUtil.doPost(requestUrl, JSON.toJSONString(reqMap), "UTF-8");
        if (!StringUtils.isEmpty(returnJson)) {
            JSONObject data = (JSONObject) JSONObject.parse(returnJson);
            log.info("图片内容安全识别接口返回结果为：" + data);
            traceId = ObjectUtils.isNotEmpty(data.get(Constants.TRACE_ID)) ? data.get(Constants.TRACE_ID).toString() : "";
        }
        return traceId;
    }

    @Override
    public JSONObject templatePush(String openid, JSONObject data, String mesPushTemplateId) {
        if (ObjectUtils.isEmpty(data)) {
            throw new ServiceException("请确定您的消息提示内容！");
        }
        log.info("当前登录用户的openid为：" + openid);
        //接口调用凭证，该参数为 URL 参数，非 Body 参数。使用access_token或者authorizer_access_token获取accessToken
        String accessToken = WxUtil.generateWeChatToken();
        log.info("accessToken为" + accessToken);
        Map<String, Object> pushMap = new HashMap<>();
        //所需下发的订阅模板id
        pushMap.put("template_id", mesPushTemplateId);
        //接收者（用户）的 openid
        pushMap.put("touser", openid);
        //模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }的object
        pushMap.put("data", data);
        //跳转小程序类型：配置在nacos中：developer为开发版；trial为体验版；formal为正式版；默认为正式版
        pushMap.put("miniprogram_state", mesPushProgramState);
        //进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
        pushMap.put("lang", "zh_CN");
        pushMap.put("page", "pages/index/index");
        log.info("请求的参数是：" + pushMap);
        String result = HttpClientUtil.doPost(wechatDomain + "/cgi-bin/message/subscribe/send" + "?access_token=" + accessToken, JSON.toJSONString(pushMap), "UTF-8");
        log.info("请求参数转换为json形式为：" + JSON.toJSONString(pushMap));
        log.info("调用微信订阅模板接口返回:" + result);
        JSONObject resultJSONObject = JSON.parseObject(result);
        return resultJSONObject;
    }

    @Override
    public JSONObject templatePush(String openid, Map<String, Object> map, String mesPushTemplateId, String appId, String secret) {
        if (ObjectUtils.isEmpty(map)) {
            throw new ServiceException("请确定您的消息提示内容！");
        }
        JSONObject data = WxUtil.getTemplateMes(map);

        log.info("当前登录用户的openid为：" + openid);
        //接口调用凭证，该参数为 URL 参数，非 Body 参数。使用access_token或者authorizer_access_token获取accessToken
        String accessToken = WxUtil.generateWeChatToken(appId, secret);
        log.info("accessToken为" + accessToken);
        Map<String, Object> pushMap = new HashMap<>();
        //所需下发的订阅模板id
        pushMap.put("template_id", mesPushTemplateId);
        //接收者（用户）的 openid
        pushMap.put("touser", openid);
        //模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }的object
        pushMap.put("data", data);
        //跳转小程序类型：配置在nacos中：developer为开发版；trial为体验版；formal为正式版；默认为正式版
        pushMap.put("miniprogram_state", mesPushProgramState);
        //进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
        pushMap.put("lang", "zh_CN");
        pushMap.put("page", "pages/index/index");
        log.info("请求的参数是：" + pushMap);
        String result = HttpClientUtil.doPost(wechatDomain + "/cgi-bin/message/subscribe/send" + "?access_token=" + accessToken, JSON.toJSONString(pushMap), "UTF-8");
        log.info("请求参数转换为json形式为：" + JSON.toJSONString(pushMap));
        log.info("调用微信订阅模板接口返回:" + result);
        JSONObject resultJSONObject = JSON.parseObject(result);
        return resultJSONObject;
    }
}
