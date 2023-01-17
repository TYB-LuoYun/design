package top.anets.modules.realTimeSessionGroup.model;

import lombok.Data;

/**
 * @author ftm
 * @date 2022/10/28 0028 14:11
 */
@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    private String userId;
    private String receiveUserId;

    private String groupId;


    public enum MessageType {
        CHAT,//消息
        JOIN,//加入
        LEAVE//离开
    }
}
