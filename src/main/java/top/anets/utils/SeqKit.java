package top.anets.utils;

/**
 * @author ftm
 * @date 2023/2/23 0023 11:56
 */
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
/*
 * 序列号生成 工具类
 * IdWorker 生成的id是通过Snowflake算法生成的，通常情况下可以保证生成的id唯一
 * 需要注意的是，如果在同一个毫秒内生成的ID数量超过了4096个，那么就有可能出现重复的情况。因此，在高并发场景下，需要根据实际情况调整机器id位和序列号位的长度来保证ID的唯一性
 *
 */
public class SeqKit {

    private static final AtomicLong PAY_ORDER_SEQ = new AtomicLong(0L);
    private static final AtomicLong REFUND_ORDER_SEQ = new AtomicLong(0L);
    private static final AtomicLong MHO_ORDER_SEQ = new AtomicLong(0L);
    private static final AtomicLong TRANSFER_ID_SEQ = new AtomicLong(0L);
    private static final AtomicLong DIVISION_BATCH_ID_SEQ = new AtomicLong(0L);

    private static final String PAY_ORDER_SEQ_PREFIX = "P";
    private static final String REFUND_ORDER_SEQ_PREFIX = "R";
    private static final String MHO_ORDER_SEQ_PREFIX = "M";
    private static final String TRANSFER_ID_SEQ_PREFIX = "T";
    private static final String DIVISION_BATCH_ID_SEQ_PREFIX = "D";

    public static String generationUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /** 是否使用MybatisPlus生成分布式ID **/
    private static final boolean IS_USE_MP_ID = true;

    /** 生成支付订单号 **/
    public static String genPayOrderId() {
        if(IS_USE_MP_ID) {
            return PAY_ORDER_SEQ_PREFIX + IdWorker.getIdStr();
        }
        return String.format("%s%s%04d",PAY_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) PAY_ORDER_SEQ.getAndIncrement() % 10000);
    }

    /** 生成退款订单号 **/
    public static String genRefundOrderId() {
        if(IS_USE_MP_ID) {
            return REFUND_ORDER_SEQ_PREFIX + IdWorker.getIdStr();
        }
        return String.format("%s%s%04d",REFUND_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) REFUND_ORDER_SEQ.getAndIncrement() % 10000);
    }


    /** 模拟生成商户订单号 **/
    public static String genMhoOrderId() {
        if(IS_USE_MP_ID) {
            return MHO_ORDER_SEQ_PREFIX + IdWorker.getIdStr();
        }
        return String.format("%s%s%04d", MHO_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) MHO_ORDER_SEQ.getAndIncrement() % 10000);
    }

    /** 模拟生成商户订单号 **/
    public static String genTransferId() {
        if(IS_USE_MP_ID) {
            return TRANSFER_ID_SEQ_PREFIX + IdWorker.getIdStr();
        }
        return String.format("%s%s%04d", TRANSFER_ID_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) TRANSFER_ID_SEQ.getAndIncrement() % 10000);
    }

    /** 模拟生成分账批次号 **/
    public static String genDivisionBatchId() {
        if(IS_USE_MP_ID) {
            return DIVISION_BATCH_ID_SEQ_PREFIX + IdWorker.getIdStr();
        }
        return String.format("%s%s%04d", DIVISION_BATCH_ID_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) DIVISION_BATCH_ID_SEQ.getAndIncrement() % 10000);
    }


    /**
     * 模拟生成对账批次号
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        System.out.println(genTransferId());
        System.out.println(genRefundOrderId());
        Thread.sleep(1000);
        System.out.println(genMhoOrderId());
        System.out.println(genTransferId());
        Thread.sleep(1000);
        System.out.println(genDivisionBatchId());
    }

    public static String genReconciliationNo() {
        if(IS_USE_MP_ID) {
            return DIVISION_BATCH_ID_SEQ_PREFIX + IdWorker.getIdStr();
        }
        return String.format("%s%s%04d", "R",
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) DIVISION_BATCH_ID_SEQ.getAndIncrement() % 10000);
    }

    public static String genOrderId() {
        if(IS_USE_MP_ID) {
            return "O" + IdWorker.getIdStr();
        }
        return String.format("%s%s%04d",PAY_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) PAY_ORDER_SEQ.getAndIncrement() % 10000);
    }

    public static String genSql(String prefix) {
        if(IS_USE_MP_ID) {
            return prefix+ IdWorker.getIdStr();
        }
        return String.format("%s%s%04d",PAY_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) PAY_ORDER_SEQ.getAndIncrement() % 10000);
    }
}

