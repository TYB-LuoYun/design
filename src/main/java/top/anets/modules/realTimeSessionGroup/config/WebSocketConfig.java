package top.anets.modules.realTimeSessionGroup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author ftm
 * @date 2022/10/28 0028 14:02
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    注册一个websocket端点，客户端将使用它连接到我们的websocket服务器
//    STOMP是来自Spring框架STOMP实现。 STOMP代表简单文本导向的消息传递协议。它是一种消息传递协议，用于定义数据交换的格式和规则
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        withSockJS()是用来为不支持websocket的浏览器启用后备选项，使用了SockJS。
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

//    配置一个消息代理，用于将消息从一个客户端路由到另一个客户端。此处为内存中的消息代理，之后也可以使用RabbitMQ或ActiveMQ等其他消息代理。
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        以“/session”开头的消息应该路由到@MessageMapping注释的消息处理方法
        registry.setApplicationDestinationPrefixes("/session");
//        以“/topic”开头的消息应该路由到消息代理。消息代理向订阅特定主题的所有连接客户端广播消息(聊天室)
        registry.enableSimpleBroker("/topic");


        // 这个是使用rabbitMq作为消息代理  featured broker like RabbitMQ
//        registry.enableStompBrokerRelay("/topic")
//                .setRelayHost("localhost")
//                .setRelayPort(61613)
//                .setClientLogin("guest")
//                .setClientPasscode("guest");
        //配置一个/topic广播消息代理和“/user”一对一消息代理  ,群发（mass），单独聊天（alone）
        registry.enableSimpleBroker("/topic","/user");

        //点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        registry.setUserDestinationPrefix("/user");

    }
}