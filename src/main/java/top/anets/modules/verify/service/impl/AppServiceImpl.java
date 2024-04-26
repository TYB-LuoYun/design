package top.anets.modules.verify.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.anets.base.WrapperQuery;
import top.anets.modules.verify.entity.App;
import top.anets.modules.verify.mapper.AppMapper;
import top.anets.modules.verify.service.IAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ftm
 * @since 2023-11-27
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {
    @Value("${system.cdrUrl:http://cdr.cloud.dicomclub.com:81}")
    private String CdrUrl;
//    @Autowired
//    private SysHospitalManagerService organinfoService;


    @Override
    public App getApp(String organCode) {
        App app = WrapperQuery.queryOne(this, Wrappers.<App>lambdaQuery().eq(App::getOrganCode, organCode));
        return app;
    }

    @Override
    public App init(String organCode) {
//        SysHospitalManager organinfo = organinfoService.getByOrganCode(organCode);
//        if(organinfo == null){
//            throw new ServiceException("该医院不存在");
//        }
        App app = new App();
        app.setAppId("U"+ DateUtil.currentSeconds());
        app.setAppSecret(RandomUtil.randomString(30));
//        app.setHospitalId(organinfo.getHospitalID());
//        app.setAseKeyPublic(RandomUtil.randomString(16));
        app.setOrganCode(organCode);
//        app.setOrganName(organinfo.getHospitalName());
//        app.setCdrUrl(CdrUrl);
        this.save(app);
        return app;
    }
    public static void main(String[] args){
        System.out.println();
    }



}
