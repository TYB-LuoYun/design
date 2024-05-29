package top.anets.modules.wx.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import top.anets.modules.wx.service.WxSendMsgService;
import top.anets.modules.wx.service.WxService;
import top.anets.utils.Constants;
import top.anets.utils.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: rain
 * 微信发送模板消息的service类实现类
 */
@RefreshScope
@Service
public class WxSendMsgServiceImpl implements WxSendMsgService {
    private final Logger log = LoggerFactory.getLogger(WxSendMsgServiceImpl.class);

    @Value("${patientApp.appIdToTemplateId:}")
    private String app;
    @Autowired
    private WxService wxService;

    /**
     * 向对应的患者发送微信消息Api
     *
     * @param title:标题
     * @param doctorName:医生姓名
     * @param patientAppId:患者端小程序id
     * @param patientOpenid:患者在患者端小程序的openid
     */
    @Override
    public JSONObject sendWxMsgApi(String title, String doctorName, String patientAppId, String patientOpenid) {
        //封装微信消息模板
        Map<String, Object> mapOutside = new HashMap(3);

        mapOutside.put("thing9", title);
        mapOutside.put("time2", DateUtil.format(new Date(), DateUtil.PATTERN_DATE_CHINA));
        mapOutside.put("name1", doctorName);

        //向患者发送微信消息
        Map<String, String> pushTemplateMap = getPushTemplateId(patientAppId);
        //向患者发送微信消息
        String pushTemplateId = pushTemplateMap.get(Constants.PUSHTEMPLATEID);
        String secret = pushTemplateMap.get(Constants.SECRET);

        return wxService.templatePush(patientOpenid, mapOutside, pushTemplateId, patientAppId, secret);
    }

    /**
     * 根据appId获取对应的消息模板id和秘钥
     *
     * @param appId
     * @return
     */
    @Override
    public Map<String, String> getPushTemplateId(String appId) {
        try {
            Map<String, String> map = new HashMap<>();

            String[] keys = app.split(",");
            for (String item : keys) {
                if (!item.contains(appId)) {
                    continue;
                }
                String[] split = item.split(":");
                map.put(Constants.PUSHTEMPLATEID, split[1]);
                map.put(Constants.SECRET, split[2]);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
