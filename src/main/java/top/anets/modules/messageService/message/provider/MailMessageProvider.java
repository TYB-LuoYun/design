package top.anets.modules.messageService.message.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.anets.modules.messageService.message.model.Msg;

@Service
@Slf4j
public class MailMessageProvider implements MessageProvider {
    @Override
    public boolean send(Msg message) {
        log.info("邮件发送");
        return true;
    }
}
