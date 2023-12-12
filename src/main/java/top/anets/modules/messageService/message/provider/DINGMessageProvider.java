package top.anets.modules.messageService.message.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.anets.modules.messageService.message.model.Msg;
import top.anets.utils.HttpClientUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ftm
 * @date 2022/10/12 0012 13:56
 */
@RefreshScope
@Slf4j
@Component
public class DINGMessageProvider implements MessageProvider{

    @Override
    public boolean send(Msg message) {

        return true;
    }

    private void push(String errorDingTalkPushUrl,String content) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> atMap = new HashMap<>();
        Map<String, Object> contentMap = new HashMap<>();
        atMap.put("atMobiles", null);
        atMap.put("atUserIds", null);
        atMap.put("isAtAll", true);
        contentMap.put("content", content);
        requestMap.put("at", atMap);
        requestMap.put("text", contentMap);
        requestMap.put("msgtype", "text");
        String result = HttpClientUtil.doPost(errorDingTalkPushUrl, JSON.toJSONString(requestMap), "UTF-8");
        JSONObject resultObject = JSONObject.parseObject(result);
        if ((resultObject.get("errcode")).equals("0")) {
            log.info("异常推送钉钉群聊成功", resultObject);
        } else {
            log.info("异常推送钉钉群聊失败" + resultObject.get("errmsg"));
        }
    }
}
