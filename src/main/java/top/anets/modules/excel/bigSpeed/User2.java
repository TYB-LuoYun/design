package top.anets.modules.excel.bigSpeed;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ftm
 * @date 2022/9/26 0026 9:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User2 {
    @Excel(name = "姓名",width = 20,height = 8)
    private String name;
    @Excel(name = "年龄",width = 20,height = 8)
    private Integer agx;
    @Excel(name = "性别",width = 20,height = 8)
    private String gender;
    @Excel(name = "住址",width = 20,height = 8)
    private String address;
    @Excel(name = "身高",width = 20,height = 8)
    private Double height;

}