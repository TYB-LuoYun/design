package top.anets.modules.rabbitMq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.rabbitMq.direct.MQConfig;
import top.anets.utils.Result;

@RestController
@Slf4j
@RequestMapping("/mq")
public class MqSenderController {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @RequestMapping("/sendDirect")
    public Result sendDirect(String msg){
        log.info("发送消息："+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
        return Result.success();
    }
}
