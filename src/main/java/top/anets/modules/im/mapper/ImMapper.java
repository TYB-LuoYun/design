package top.anets.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.anets.modules.im.model.MessageRecord;

/**
 *
 *
 */
@Mapper
public interface ImMapper extends BaseMapper<MessageRecord> {

}
