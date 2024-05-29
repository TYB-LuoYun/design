package top.anets.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.modules.im.model.MessageRecord;
import top.anets.utils.Result;


/**
 *
 *
 */
public interface ImService extends IService<MessageRecord> {

    Result saveMsg(MessageRecord messageRecord);
}
