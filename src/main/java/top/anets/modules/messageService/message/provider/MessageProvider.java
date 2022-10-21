package top.anets.modules.messageService.message.provider;

import top.anets.modules.messageService.message.model.Msg;

public interface MessageProvider {
    boolean send(Msg message);
}
