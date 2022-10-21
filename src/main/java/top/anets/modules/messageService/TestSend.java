package top.anets.modules.messageService;

import top.anets.modules.messageService.message.model.Msg;
import top.anets.modules.messageService.message.provider.DINGMessageProvider;

/**
 * @author ftm
 * @date 2022/10/12 0012 13:45
 */
public class TestSend {
    public static void main(String[] args){
        Msg message = new Msg();
        new MsgService(new DINGMessageProvider()).send(message);

        new DINGMessageProvider().send(message);

        MsgService.sendToAllProvider(message);
    }
}
