package top.anets.modules.rabbitMq.direct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String msg){
        log.info("接收到消息："+msg);
    }
}
