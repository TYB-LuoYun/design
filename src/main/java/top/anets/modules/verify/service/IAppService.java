package top.anets.modules.verify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import top.anets.modules.verify.entity.App;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ftm
 * @since 2023-11-27
 */
public interface IAppService extends IService<App> {


    App getApp(String organCode);

    App init(String organCode);
}
