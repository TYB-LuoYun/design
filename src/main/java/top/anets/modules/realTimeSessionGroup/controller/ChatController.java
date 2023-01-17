package top.anets.modules.realTimeSessionGroup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import top.anets.modules.realTimeSessionGroup.model.ChatMessage;

/**
 * @author ftm
 * @date 2022/10/28 0028 14:13
 */
@Controller
public class ChatController {


    @Autowired
    private SimpMessagingTemplate template;


    //    广播用户加入事件 ,或者把这个写到监听里面去
    @MessageMapping("/chat.addUser")
//   将返回的消息发布到public这个群组中
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        System.out.println("用户加入"+chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        template.convertAndSend("/topic/public",chatMessage);
        return chatMessage;
    }





//   以/app开头的客户端发送的所有消息都将路由到这些使用@MessageMapping注释的消息处理方法
    @MessageMapping("/sendMsgToGroup")
//   将返回的消息发布到public这个群组中
    public void sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println("用户发送消息到某个群组："+chatMessage.getSender()+"->"+chatMessage.getContent());
        template.convertAndSend("/topic/"+chatMessage.getGroupId(),chatMessage);
    }



    /**
     * 为目标用户发送消息(单独聊天)
     */
    @MessageMapping("/sendMsgToUser")
    public void message(ChatMessage msg){
        this.template.convertAndSendToUser(msg.getReceiveUserId(),"custom",msg);
    }



}