package top.anets.utils.dingding;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.anets.utils.HttpClientUtil;
import top.anets.utils.SpringContextUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
/**
 * @author ftm
 * @date 2022/10/12 0012 16:56
 */
@Component
public class AbnormalRemindUtil {

    private static final Logger logger = LoggerFactory.getLogger(AbnormalRemindUtil.class);

    /**
     * @param : paramMap: 包含errorType-异常类型；content-异常提醒内容；remindAllParam-是否@全体成员(boolean型)
     *          钉钉异常提醒
     *          rain
     *          2022/10/11 13:36
     */
    public void dingTalkAbnormalPush(String errorType, String content, boolean remindAllParam) throws IOException, XmlPullParserException {
        //获取项目名称
        String rootPath = System.getProperty("user.dir");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        String myPom = rootPath + File.separator + "pom.xml";
        Model model = reader.read(new FileReader(myPom));
        String projectName = model.getName();
        //先做提醒内容格式化处理，加上项目名称，异常类型
        content = "项目名称:" + projectName + ";\n" + "异常类型:" + errorType + ";\n" + "异常内容:" + content;
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        AbnormalRemind abnormalRemind = applicationContext.getBean(AbnormalRemind.class);
        String errorDingTalkPushUrl = abnormalRemind.getErrorDingTalkPushUrl();
        Map<String, Object> requestMap = new HashMap<>(Constants.INT_FOUR);
        Map<String, Object> atMap = new HashMap<>(Constants.INT_FOUR);
        Map<String, Object> contentMap = new HashMap<>(Constants.INT_ONE);
        atMap.put("atMobiles", null);
        atMap.put("atUserIds", null);
        atMap.put("isAtAll", remindAllParam);
        contentMap.put("content", content);
        requestMap.put("at", atMap);
        requestMap.put("text", contentMap);
        requestMap.put("msgtype", "text");
        String result = HttpClientUtil.doPost(errorDingTalkPushUrl, JSON.toJSONString(requestMap), "UTF-8");
        JSONObject resultObject = JSONObject.parseObject(result);
        if ((resultObject.get(Constants.ERRCODE)).equals(Constants.STRING_ZERO)) {
            logger.info("异常推送钉钉群聊成功", resultObject);
        } else {
            logger.info("异常推送钉钉群聊失败" + resultObject.get(Constants.ERRMSG));
        }
    }
}
