package top.anets.modules.messageService;

import top.anets.modules.messageService.message.model.Msg;
import top.anets.modules.messageService.message.provider.*;

/**
 * @author ftm
 * @date 2022/10/12 0012 13:29
 *
 */
public class MsgService implements MessageProvider{
    MessageProvider messageProvider;

    public MsgService(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public boolean send(Msg message) {
          return  messageProvider.send(message);
    }


    public static void sendToAllProvider(Msg message){
        new MsgService(new MailMessageProvider()).send(message);
        new MsgService(new APPMessageProvider()).send(message);
        new MsgService(new SMSMessageProvider()).send(message);
        new MsgService(new DINGMessageProvider()).send(message);
    }


    public static MessageProvider use(MessageProvider messageProvider) {
        return  messageProvider;
    }

}
