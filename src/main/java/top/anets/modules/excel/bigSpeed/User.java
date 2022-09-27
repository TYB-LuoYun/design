package top.anets.modules.excel.bigSpeed;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ftm
 * @date 2022/9/26 0026 9:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User {
//    @Excel(name = "姓名",width = 20,height = 8)
    private String name;
//    @Excel(name = "年龄",width = 20,height = 8)
    private Integer agx;
//    @Excel(name = "性别",width = 20,height = 8)
    private String gender;
//    @Excel(name = "住址",width = 20,height = 8)
    private String address;
//    @Excel(name = "身高",width = 20,height = 8)
    private Double height;

    // 进行解析后的类型转换
    public User(List<Object> values) {
        this.name = values.get(0) == null ? "" : values.get(0).toString();
        this.agx = values.get(1) == null ? 0 : new Double(values.get(1).toString()).intValue();
        this.gender = values.get(2) == null ? "" : values.get(0).toString();
        this.address = values.get(3) == null ? "" : values.get(0).toString();
        this.height = values.get(4) == null ? 0 : Double.parseDouble(values.get(4).toString());
    }
}