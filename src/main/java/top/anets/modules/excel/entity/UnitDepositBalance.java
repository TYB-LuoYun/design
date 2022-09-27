package top.anets.modules.excel.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

import java.util.List;

/**
 * @author ftm
 * @date 2022/9/16 0016 15:48
 */
@Data
public class UnitDepositBalance {

    // 正常字段   @Excel  needMerge合并单元格
    @Excel(name = "字段",width = 20,height = 8,needMerge = true)
    private String fields;

    @Excel(name = "名称",width = 20,height = 8)
    private String name;

    // 合并单元格字段
    @ExcelCollection(name="余额")
    private List<BalanceVo> balanceLists;

    @ExcelCollection(name="利率")
    private List<RateVo> rateVoList;
}