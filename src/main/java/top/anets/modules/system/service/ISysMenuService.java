package top.anets.modules.system.service;

import top.anets.modules.system.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Map;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author ftm
 * @since 2022-10-11
 */
public interface ISysMenuService extends IService<SysMenu> {


    IPage  pages(QueryWrapper querys, IPage page);

    /**
     * 添加菜单信息表
     *
     * @param sysMenu 菜单信息表
     * @return int
     */
    int add(SysMenu sysMenu);

    /**
     * 删除菜单信息表
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改菜单信息表
     *
     * @param sysMenu 菜单信息表
     * @return int
     */
    int updateData(SysMenu sysMenu);

    /**
     * id查询数据
     *
     * @param id id
     * @return SysMenu
     */
    SysMenu findById(Long id);


    /**
     * 关联查询
     */
    IPage pagesAssociate(Map<String, Object> params, IPage page);


}
