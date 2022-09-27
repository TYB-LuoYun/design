package top.anets.modules.excel.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ftm
 * @date 2022/9/16 0016 15:47
 */
@Data
public class RateVo {

    @Excel(name = "本期（%）",width = 20,height = 8,type = 10)
    private BigDecimal currentRate;

    @Excel(name = "上期（%）",width = 20,height = 8,type = 10)
    private BigDecimal lastRate;
}