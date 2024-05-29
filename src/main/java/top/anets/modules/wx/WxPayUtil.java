package top.anets.modules.wx;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.UuidUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.anets.utils.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

/**
 * @author: rain
 * 微信支付相关业务工具类
 */
@RefreshScope
@Component
public class WxPayUtil {

    private static final Logger logger = LoggerFactory.getLogger(WxPayUtil.class);

    public static final String QUESTION = "?";

    private static String wechatPayMchId;
    private static String certificateSerialNo;
    private static String privateKeyLocation;

    //商户号
    @Value("${wechat.pay.mchId:}")
    public void setWechatPayMchId(String mchId) {
        wechatPayMchId = mchId;
    }

    //商户号
    @Value("${wechat.pay.certificateSerialNo:}")
    public void setCertificateSerialNo(String serialNo) {
        certificateSerialNo = serialNo;
    }

    //商户证书路径地址
    @Value("${wechat.pay.privateKeyLocation:}")
    public void setPrivateKeyLocation(String keyLocation) {
        privateKeyLocation = keyLocation;
    }


    /**
     * post方法获取微信签名串
     * rain
     * 2023/3/23 17:23
     */
    public static String getPostToken(String method, String url, String body) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String nonceStr = generateNonceStr();
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        String signature = sign(message.getBytes("utf-8"));

        return "mchid=\"" + wechatPayMchId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + certificateSerialNo + "\","
                + "signature=\"" + signature + "\"";
    }

    /**
     * get方法获取微信签名串
     * rain
     * 2023/3/23 17:23
     */
    public static String getGetToken(String method, String url, String paramJsonStr) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String nonceStr = generateNonceStr();
        long timestamp = System.currentTimeMillis() / 1000;
        if (ObjectUtils.isNotEmpty(paramJsonStr)) {
            JSONObject paramJson = (JSONObject) JSONObject.parse(paramJsonStr);
            //fastjson解析方法
            for (Map.Entry<String, Object> entry : paramJson.entrySet()) {
                if (url.contains("?")) {
                    url += "&" + entry.getKey() + "=" + entry.getValue();
                } else {
                    url += "?" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }
        String message = buildMessage(method, url, timestamp, nonceStr, null);
        String signature = sign(message.getBytes("utf-8"));

        return "mchid=\"" + wechatPayMchId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + certificateSerialNo + "\","
                + "signature=\"" + signature + "\"";
    }

    /**
     * 用微信私钥签名
     *
     * @param message
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws IOException
     * @throws InvalidKeyException
     * @author rain
     */
    public static String sign(byte[] message) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        //获取私钥
        PrivateKey privateKey = getPrivateKey(privateKeyLocation);
        sign.initSign(privateKey);
        sign.update(message);

        return Base64.getEncoder().encodeToString(sign.sign());
    }

    /**
     * 生成签名串
     *
     * @param method
     * @param url
     * @param timestamp
     * @param nonceStr
     * @param body
     * @return
     * @author rain
     */
    public static String buildMessage(String method, String url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = getCanonicalUrl(url);
        if (StringUtils.isEmpty(body)) {
            body = "";
        }
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }

    /**
     * 获取私钥。
     *
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     * @author rain
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException {

        String content = new String(Files.readAllBytes(Paths.get(filename)), "utf-8");
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * @param :null
     * @return 32位的随机数
     * 随机数生成算法
     * rain
     * 2023/3/27 10:41
     */
    public static String generateNonceStr() {
        return UuidUtils.generateUuid();
    }

    /**
     * @return 6-32位的商户退款单号
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     * rain
     * 2023/4/3
     */
    public static String generateOutRefundNo() {
        String uuid = UUID.randomUUID().toString();
        String outTradeNo = uuid.substring(Constants.INT_ZERO, Constants.INT_THIRTY_TWO);
        return outTradeNo;
    }

    /**
     * 去除域名的标准url
     *
     * @param url url
     * @return 去除域名的标准url
     * @author rain
     */
    public static String getCanonicalUrl(String url) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(url)) {
            return url;
        }
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String encodedQuery = uri.getQuery();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(encodedQuery)) {
                path += QUESTION.concat(encodedQuery);
            }
            return path;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("failure 去除域名的标准url失败");
        }

    }

    /**
     * @param :method         方法：分为GET POST
     * @param :url            请求微信端接口的url地址
     * @param :requestJsonStr 请求微信端接口的传参的json格式的字符串，如果为get请求，则传递空串即可
     * @return Header[]
     * 生成微信支付必传校验请求头
     * rain
     * 2023/4/3 15:35
     */
    public static Header[] getWxPayHttpHeader(String method, String url, String requestJsonStr) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String signature = null;
        if (Constants.POST_METHOD.equals(method)) {
            signature = getPostToken(method, url, requestJsonStr);
        } else if (Constants.GET_METHOD.equals(method)) {
            signature = getGetToken(method, url, requestJsonStr);
        }
        Header[] headers = {new BasicHeader("Accept", "application/json"), new BasicHeader("Authorization", "WECHATPAY2-SHA256-RSA2048 " + signature)};
        return headers;
    }
}
