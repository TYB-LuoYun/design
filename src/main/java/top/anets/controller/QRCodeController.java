package top.anets.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.utils.MD5Utils;
import top.anets.utils.QRCodeUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
 * @author ftm
 * @date 2022/10/31 0031 18:09
 * 二维码
 */
@RestController
@RequestMapping("QRCode")
public class QRCodeController {
    @GetMapping("urlToQRCode")
    public void  urlToQRCode(@NotBlank String url, HttpServletResponse response) throws Exception {
        QRCodeUtil.generateQRCode(url,response , 350, 350);
        response.setContentType("image/"+ MD5Utils.MD5(url)+".png");

    }

}
