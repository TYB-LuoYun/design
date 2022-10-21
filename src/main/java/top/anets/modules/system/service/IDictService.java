package top.anets.modules.system.service;

import top.anets.modules.system.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ftm
 * @since 2022-10-11
 */
public interface IDictService extends IService<Dict> {


    IPage  pages(QueryWrapper querys, IPage page);



    /**
     * 关联查询
     */
    IPage pagesAssociate(Map<String, Object> params, IPage page);


}
