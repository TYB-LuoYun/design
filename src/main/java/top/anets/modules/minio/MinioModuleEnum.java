package top.anets.modules.minio;

/**
 * minio文件夹枚举
 */
public enum MinioModuleEnum {

    //头像
    USER_AVATAR(1, "userAvatar"),
    //认证
    AUTHENTICATION(2, "authentication"),
    //分享报告图片
    SHARE_REPORT_IMG(3, "shareReportImg"),
    //疾病科普图片
    JBKP_IMG(4, "jbkpImg"),
    //IM消息图片
    ROAM_IMG(5,"roamImg"),
    //IM消息音频
    ROAM_AUDIO(6,"roamAudio"),
    //意见反馈图片
    YJFK_IMG(7,"yjfkImg"),
    //IM消息视频
    ROAM_VIDEO(8,"roamVideo"),
    //二维码图片
    QRCODE_IMG(9,"qrCodeImg"),
    //
    DIGITAL_IMG_ZIP(10,"digitalImgZip");


    private int code;

    private String name;

    MinioModuleEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(int code) {
        for (MinioModuleEnum value : values()) {
            if (value.getCode() == code) {
                return value.getName();
            }
        }
        return null;
    }


}
