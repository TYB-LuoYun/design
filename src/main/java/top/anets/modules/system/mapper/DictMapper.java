package top.anets.modules.system.mapper;

import top.anets.modules.system.entity.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
    * mapper接口
    * </p>
 *
 * @author ftm
 * @since 2022-10-11
 */
public interface DictMapper extends BaseMapper<Dict> {
     IPage pagesAssociate(IPage page, @Param("param") Map<String, Object> params);


}