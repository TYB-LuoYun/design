package top.anets.modules.minio;

import java.math.BigDecimal;

/**
 * 存储单位换算
 *
 * @author Gltqe
 * @date 2022/7/3 0:31
 **/
public class StorageUnitUtil {

    public static final Long BIT = 1L;
    public static final Long BYTE = 8L;
    public static final Long KB = 8 * 1024L;
    public static final Long MB = 8 * 1024 * 1024L;
    public static final Long GB = 8 * 1024 * 1024 * 1024L;
    public static final Long TB = 8 * 1024 * 1024 * 1024L * 1024L;
    public static final Integer SCALE = 2;

    /**
     * 存储单位换算
     *
     * @param length      需要转换的存储大小
     * @param currentUnit 当前存储单位
     * @param targetUnit  转换目标存储单位
     * @param scale       小数点
     * @return java.math.BigDecimal
     * @author Gltqe
     * @date 2022/7/3 0:32
     **/
    public static BigDecimal convert(BigDecimal length, Long currentUnit, Long targetUnit, Integer scale) {
        if (scale == null) {
            scale = SCALE;
        }
        if (currentUnit < targetUnit) {
            BigDecimal b1 = new BigDecimal(targetUnit / currentUnit);
            BigDecimal decimal = length.divide(b1, scale, BigDecimal.ROUND_HALF_UP);
            return decimal;
        } else if (currentUnit > targetUnit) {
            BigDecimal b1 = new BigDecimal(currentUnit / targetUnit);
            BigDecimal decimal = length.multiply(b1).setScale(scale, BigDecimal.ROUND_HALF_UP);
            return decimal;
        } else {
            return length;
        }
    }

    public static void main(String[] args) {
        // 测试 : 1500KB 转为 MB // 结果: 1.465
        BigDecimal convert1 = convert(new BigDecimal("1500"), KB, MB, 3);
        System.out.println(convert1);
        // 测试 : 1500b 转为 byte // 结果: 187.500
        BigDecimal convert2 = convert(new BigDecimal("1500"), BIT,BYTE, 3);
        System.out.println(convert2);
    }
}