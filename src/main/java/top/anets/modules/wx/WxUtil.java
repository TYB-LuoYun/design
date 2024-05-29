package top.anets.modules.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.anets.support.redis.RedisUtils;
import top.anets.utils.Constants;
import top.anets.utils.HttpClientUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@RefreshScope
@Slf4j
@Component
public class WxUtil {
    private static Logger logger = LoggerFactory.getLogger(WxUtil.class);

    private static String WX_LOGIN_APPID;    //小程序的appid
    private static String WX_GZH_APPID;    //公众号的appid
    private static String WX_LOGIN_SECRET;   //小程序的secret
    private static String WX_GZH_SECRET;   //公众号的secret
    private static String WX_LOGIN_GRANT_TYPE = "authorization_code";

    private static String WX_TOKEN_URL = "/cgi-bin/token";
    private static String WX_PHONE_URL = "/wxa/business/getuserphonenumber";
    private static String WX_LOGIN_URL = "/sns/jscode2session";

    private static String WX_DOMAIN;   //微信接口域名


    private static String WX_SKIP_APPID;    //要跳转的appid
    private static String WX_SKIP_SECRET;   //要跳转的secret
    
    
    private static final String ACCESS_TOKEN = "access_token";


    @Value("${wechat.domain:}")
    public void setWX_DOMAIN(String domain) {
        WX_DOMAIN = domain;
    }

    // 跳转的微信appid
    @Value("${wechat.skipAppid:}")
    public void setWxSkipAppid(String skipAppid) {
        WX_SKIP_APPID = skipAppid;
    }

    // 跳转的微信密匙
    @Value("${wechat.skipsecret:}")
    public void setWxSkipSecret(String skipSecret) {
        WX_SKIP_SECRET = skipSecret;
    }

    // appid
    @Value("${wechat.appid:}")
    public void setWxLoginAppid(String appid) {
        WX_LOGIN_APPID = appid;
    }

    // 密匙
    @Value("${wechat.secret:}")
    public void setWxLoginSecret(String secret) {
        WX_LOGIN_SECRET = secret;
    }

    // 公众号appid
    @Value("${gzh.appId:}")
    public void setWxGzhAppid(String appid) {
        WX_GZH_APPID = appid;
    }

    // 公众号appSecret
    @Value("${gzh.appSecret:}")
    public void setWxGzhSecret(String secret) {
        WX_GZH_SECRET = secret;
    }



    //通过code换取微信小程序官网获取的信息  ==== 登录
    public static JSONObject getOpenIdByCode(String code) {
        //配置请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("appid", WX_LOGIN_APPID);
        params.put("secret", WX_LOGIN_SECRET);
        params.put("js_code", code);
        params.put("grant_type", WX_LOGIN_GRANT_TYPE);
        log.info("请求地址：" + WX_DOMAIN + WX_LOGIN_URL);
        log.info("请求参数：" + params.toString());
        //向微信服务器发送请求
        String wxRequestResult = HttpClientUtil.doGet(WX_DOMAIN + WX_LOGIN_URL, params, "UTF-8");
        if (StringUtils.isBlank(wxRequestResult)) {
            throw new RuntimeException("请求失败:" + WX_DOMAIN + WX_LOGIN_URL);
        }
        JSONObject resultJson = JSONObject.parseObject(wxRequestResult);
        return resultJson;
    }

    //获取openid
    public static String getOpenid(String code) {
        return getOpenIdByCode(code).getString("openid");
    }

    /**
     * 获取用户手机号
     *
     * @param code
     * @return
     */
    public static String getPhoneNumber(String code) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String accessToken =   RedisUtils.get( ACCESS_TOKEN);
        paramMap.put("code", code);
        //生成token
        String result = HttpClientUtil.doPost(WX_DOMAIN + WX_PHONE_URL + "?access_token=" + accessToken, JSONObject.toJSONString(paramMap), "UTF-8");
        System.out.println(result);
        if (!StringUtils.isBlank(result)) {
            throw new RuntimeException("手机号获取失败！");
        }
        JSONObject resultJson = JSONObject.parseObject(result);
        Integer errcode = resultJson.getInteger("errcode");
        if (!Constants.INT_ZERO.equals(errcode)) {
//          重新获取
            accessToken = WxUtil.generateWeChatToken();
            RedisUtils.set(ACCESS_TOKEN, accessToken, 2 * 60 * 55);
            result = HttpClientUtil.doPost(WX_DOMAIN + WX_PHONE_URL + "?access_token=" + accessToken, JSONObject.toJSONString(paramMap), "UTF-8");
            System.out.println(result);
            if (!StringUtils.isBlank(result)) {
                throw new RuntimeException("手机号获取失败！");
            }
            resultJson = JSONObject.parseObject(result);
            errcode = resultJson.getInteger("errcode");
            if (!Constants.INT_ZERO.equals(errcode)) {
                throw new RuntimeException("重新获取token失败：" + resultJson.getString("errmsg"));
            }

        }
        JSONObject phoneInfoJson = resultJson.getJSONObject("phone_info");
        String phone = phoneInfoJson.getString("phoneNumber");
        return phone;
    }

    /**
     * 获取accessToken
     *
     * @return
     */
    public static String generateWeChatToken() {
        Map<String, Object> tokenMap = new HashMap<String, Object>();

        tokenMap.put("grant_type", "client_credential");
        tokenMap.put("appid", WX_LOGIN_APPID);
        tokenMap.put("secret", WX_LOGIN_SECRET);
        String result = HttpClientUtil.doGet(WX_DOMAIN + WX_TOKEN_URL, tokenMap, "UTF-8");
        if (!StringUtils.isBlank(result)) {
            logger.info("获取accessToken失败!");
            throw new RuntimeException("获取accessToken失败！");
        }
        JSONObject resultJson = JSONObject.parseObject(result);
        String accessToken = resultJson.getString("access_token");
        return accessToken;
    }

    /**
     * 获取accessToken
     *
     * @return
     */
    public static String generateWeChatToken(String appId, String secret) {
        Map<String, Object> tokenMap = new HashMap<String, Object>();

        tokenMap.put("grant_type", "client_credential");
        tokenMap.put("appid", appId);
        tokenMap.put("secret", secret);
        String result = HttpClientUtil.doGet(WX_DOMAIN + WX_TOKEN_URL, tokenMap, "UTF-8");
        if (!StringUtils.isBlank(result)) {
            logger.info("获取accessToken失败!");
            throw new RuntimeException("获取accessToken失败！");
        }
        JSONObject resultJson = JSONObject.parseObject(result);
        String accessToken = resultJson.getString("access_token");
        return accessToken;
    }

    /**
     * 根据map获取微信消息模板
     *
     * @param map
     * @return
     */
    public static JSONObject getTemplateMes(Map<String, Object> map) {
        Map<String, Object> mapOutside = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            map = new HashMap<>();
            map.put("value", entry.getValue());
            mapOutside.put(entry.getKey(), map);
        }
        String jsonObject = JSON.toJSONString(mapOutside);
        JSONObject data = JSON.parseObject(jsonObject);
        return data;
    }


    /**
     * 获取微信跳转连接
     *
     * @return
     */
    public static String getOpenLink() {
        String token = generateWeChatSkipToken();
        String url = "https://api.weixin.qq.com/wxa/generatescheme?access_token=" + token;
        JSONObject jumpWxa = new JSONObject();
        //通过 scheme 码进入的小程序页面路径，必须是已经发布的小程序存在的页面，不可携带 query。path 为空时会跳转小程序主页。
        jumpWxa.put("path", "");
        //通过 scheme 码进入小程序时的 query，最大1024个字符，只支持数字，大小写英文以及部分特殊字符：`!#$&'()*+,/:;=?@-._~%``
        jumpWxa.put("query", "");
        jumpWxa.put("env_version", "release");

        JSONObject reqMap = new JSONObject();
        reqMap.put("jump_wxa", jumpWxa);
        String result = "";
        try {

            result = HttpClientUtil.doPost(url, JSONObject.toJSONString(reqMap), "UTF-8");

            result = URLDecoder.decode(result, "UTF-8");
            System.out.print("微信获取getOpenLink回调报文" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 根据nacos配置获取accessToken(获取跳转的app Token)
     *
     * @return
     */
    public static String generateWeChatSkipToken() {
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        ;
        tokenMap.put("grant_type", "client_credential");
        tokenMap.put("appid", WX_SKIP_APPID);
        tokenMap.put("secret", WX_SKIP_SECRET);
        String result = HttpClientUtil.doGet(WX_DOMAIN + WX_TOKEN_URL, tokenMap, "UTF-8");
        if (!StringUtils.isBlank(result)) {
            logger.info("获取accessToken失败!");
            throw new RuntimeException("获取accessToken失败！");
        }
        JSONObject resultJson = JSONObject.parseObject(result);
        String accessToken = resultJson.getString("access_token");
        return accessToken;
    }

    /**
     * 获取JSApi请求所需的临时票据
     *
     * @return
     */
    public static String getJsApiTicket(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
        String result = HttpClientUtil.doGet(url, null, "utf-8");
        JSONObject resultJson = JSONObject.parseObject(result);
        String ticket = resultJson.getString("ticket");
        if (StringUtils.isEmpty(ticket)) {
            logger.info("获取JSApi请求所需的临时票据失败");
            throw new RuntimeException("获取JSApi请求所需的临时票据失败");
        }
        return ticket;
    }

    /**
     * 获取公众号accessToken
     *
     * @return
     */
    public static String generateWeChatGzhToken() {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", WX_GZH_APPID);
        params.put("secret", WX_GZH_SECRET);
        String result = HttpClientUtil.doGet(WX_DOMAIN + WX_TOKEN_URL, params, "UTF-8");
        if (!StringUtils.isBlank(result)) {
            logger.info("获取accessToken失败!");
            throw new RuntimeException("获取accessToken失败！");
        }
        JSONObject resultJson = JSONObject.parseObject(result);
        String accessToken = resultJson.getString("access_token");
        return accessToken;
    }
}
