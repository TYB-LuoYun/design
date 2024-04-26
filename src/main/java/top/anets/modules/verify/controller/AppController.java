package top.anets.modules.verify.controller;

import top.anets.exception.ServiceException;
import top.anets.modules.verify.entity.App;
import top.anets.modules.verify.service.IAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @author ftm
 * @date 2023-12-14 9:56
 */
@RestController
@RequestMapping("/app")
@Validated
public class AppController {
    @Autowired
    private IAppService appService;

    @GetMapping("/info")
    public App app(@NotBlank String organCode){
        App app =appService.getApp(organCode);
        return app;
    }

    /**
     * 生成秘钥信息
     */
    @GetMapping("/init")
    public App init(@NotBlank String organCode){
        App app =appService.getApp(organCode);
        if(app != null){
            throw new ServiceException("该医院已分配授权，请勿重复生成");
        }
        return appService.init(organCode);
    }


    /**
     * 更新信息
     */
    @PostMapping("/update")
    public void update(@RequestBody App app){
        if(app.getAppId()!=null){
            appService.updateById(app);
        }
    }
}
