package top.anets.modules.verify;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import top.anets.modules.verify.entity.App;
import top.anets.modules.verify.service.IAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author ftm
 * @date 2023/6/29 0029 18:26
 */
@Service
public class VerifyService {


    private static String ProviderID = "123"; //厂商编码(由平台分配)

    private static String AccessSecret = "456"; //授权码（平台提供）
    private static String AseKeyPublic = "ejdhsgdhtyiojhrr"; //AES密钥信息，由平台放生成AES密钥信息进行下发

    @Autowired
    private IAppService appService;


    public App verifyIdentity(String providerID) {
        App byId = (App) appService.getById(providerID);
        if(byId!=null){

        }
        return byId;
    }

    public static App getCurrentApp(){
        return VerifyAop.contextHolder.get();
    }



}
