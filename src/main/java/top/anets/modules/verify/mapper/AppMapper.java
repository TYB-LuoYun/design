package top.anets.modules.verify.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

import top.anets.modules.verify.entity.App;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
    * mapper接口
    * </p>
 *
 * @author ftm
 * @since 2023-11-27
 */
public interface AppMapper extends BaseMapper<App> {
     IPage pagesAssociate(IPage page, @Param("param") Map<String, Object> params);


}