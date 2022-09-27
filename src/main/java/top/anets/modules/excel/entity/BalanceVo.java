package top.anets.modules.excel.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ftm
 * @date 2022/9/16 0016 15:47
 */
@Data
public class BalanceVo {

    @Excel(name = "余额比重（%）",width = 20,height = 8)
    private BigDecimal balanceProp;

    // dict 和 type 在 ExcelExportStatisticStyler 中识别
    // isStatistics 是否需要被合计
    @Excel(name = "本期（万元）",isStatistics=true,width = 20,height = 8,type = 10)
    private BigDecimal currentPeriod;

    @Excel(name = "上期（万元）",isStatistics=true,width = 20,height = 8,type = 10)
    private BigDecimal lastPeriod;

    @Excel(name = "环比增减（%）",width = 20,height = 8)
    private BigDecimal momZJ;

    @Excel(name = "跨期校验（万元）",isStatistics=true,width = 20,height = 8)
    private BigDecimal kqProof;
}