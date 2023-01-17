//package top.anets.modules.realTimeSessionGroup.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Controller;
//import top.anets.modules.realTimeSession.model.User;
//import top.anets.modules.realTimeSessionGroup.model.ChatMessage;
//
///**
// * @author ftm
// * @date 2022/10/28 0028 16:46
// */
//
//@Controller
//public class PushMsgController {
//    @Autowired
//    private SimpMessagingTemplate template;
//
//    //广播推送消息
//    @Scheduled(fixedRate = 10000)
//    public void sendTopicMessage() {
//        System.out.println("后台广播推送！");
//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setSender("admin");
//        chatMessage.setContent("ping ok .....");
//        this.template.convertAndSend("/topic/public",chatMessage);
//    }
//
//
//    //一对一推送消息
//    @Scheduled(fixedRate = 10000)
//    public void sendQueueMessage() {
//        System.out.println("后台一对一推送！");
//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setSender("admin");
//        chatMessage.setContent("hellow");
//        chatMessage.setReceiveUserId("12344");
//        this.template.convertAndSendToUser(chatMessage.getReceiveUserId() , "custom", chatMessage);
//    }
//}
