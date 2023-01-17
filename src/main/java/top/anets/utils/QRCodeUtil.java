package top.anets.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ftm
 * @date 2022/10/31 0031 18:02
 *
 *         <dependency>
 *             <groupId>com.google.zxing</groupId>
 *             <artifactId>javase</artifactId>
 *             <version>3.3.0</version>
 *         </dependency>
 */
public class QRCodeUtil {
    private static final int width = 300;// 默认二维码宽度
    private static final int height = 300;// 默认二维码高度
    private static final String format = "png";// 默认二维码文件格式
    private static final Map<EncodeHintType, Object> hints = new HashMap();// 二维码参数

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        hints.put(EncodeHintType.MARGIN, 2);// 二维码与图片边距
    }


    public static void generateQRCode(String url, HttpServletResponse response) throws Exception {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "连接地址为空");
        generateQRCode(url,response,height,width);
    }

    public static void generateQRCode(String url, HttpServletResponse response, int height, int width) throws Exception {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "连接地址为空");
        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, height, width, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
    /**
     * 返回一个 BufferedImage 对象
     * @param content 二维码内容
     * @param width   宽
     * @param height  高
     */
    public static BufferedImage toBufferedImage(String content, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    /**
     * 将二维码图片输出到一个流中
     * @param content 二维码内容
     * @param stream  输出流
     * @param width   宽
     * @param height  高
     */
    public static void writeToStream(String content, OutputStream stream, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
    }
    /**
     * 生成二维码图片文件
     * @param content 二维码内容
     * @param path    文件保存路径
     * @param width   宽
     * @param height  高
     */
    public static void createQRCode(String content, String path, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        //toPath() 方法由 jdk1.7 及以上提供
        MatrixToImageWriter.writeToPath(bitMatrix, format, new File(path).toPath());
    }

    public static void main(String[] args) throws Exception {
        OutputStream outputStream=new FileOutputStream(new File("/home/icecrea/hello.png"));
        QRCodeUtil.writeToStream("www.baidu.com",outputStream,width,height);
        QRCodeUtil.createQRCode("www.qunar.com","/home/icecrea/qunar.png",width,height);
    }
}
