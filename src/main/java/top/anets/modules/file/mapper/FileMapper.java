package top.anets.modules.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.anets.modules.file.model.File;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sghc
 * @since 2021-05-21
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

}
