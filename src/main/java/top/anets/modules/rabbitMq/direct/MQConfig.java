package top.anets.modules.rabbitMq.direct;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * direct模式，生产者不经过交换机直接将消息放进队列
 */
@Configuration
public class MQConfig {
    public static final String QUEUE = "queue";
    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }
}
