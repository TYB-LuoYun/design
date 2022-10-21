package top.anets.utils.dingding;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author: rain
 * 异常推送entity
 * 2022/10/11 16:07
 */
@Component
@RefreshScope
@Data
public class AbnormalRemind {
    @Value("${dingTalk.errorPushUrl}")
    private String errorDingTalkPushUrl;
}
