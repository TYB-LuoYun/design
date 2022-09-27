package top.anets.modules.excel.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author ftm
 * @date 2022/9/16 0016 15:26
 * name：Excel中的列名；
 * width：指定列的宽度；
 * needMerge：是否需要纵向合并单元格；
 * format：当属性为时间类型时，设置时间的导出导出格式；
 * desensitizationRule：数据脱敏处理，3_4表示只显示字符串的前3位和后4位，其他为*号；
 * replace：对属性进行替换；
 * suffix：对数据添加后缀。
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Member {
    @Excel(name = "ID", width = 10)
    private Long id;
    @Excel(name = "用户名", width = 20, needMerge = true)
    private String username;
    private String password;
    @Excel(name = "昵称", width = 20, needMerge = true)
    private String nickname;
    @Excel(name = "出生日期", width = 20, format = "yyyy-MM-dd")
    private Date birthday;
    @Excel(name = "手机号", width = 20, needMerge = true, desensitizationRule = "3_4")
    private String phone;
    private String icon;
    @Excel(name = "性别", width = 10, replace = {"男_0", "女_1"})
    private Integer gender;
}
