package top.anets.modules.excel.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import lombok.Data;

import java.util.Date;

/**
 * @Author yjz
 * @Date 2022/1/14 22:45
 */
@Data
public class MsgClient {
    /**
     * id
     */
    private String id;
    // 电话号码(主键)
    @Excel(name = "电话号码")
    private String clientPhone = null;
    // 客户姓名
    @Excel(name = "姓名")
    private String clientName = null;
    // 所属分组
    @ExcelEntity
    private MsgClientGroup group = null;
    // 备注
    @Excel(name = "备注")
    private String remark = null;
    // 生日
    @Excel(name = "出生日期", format = "yyyy-MM-dd", width = 20)
    private Date birthday = null;
    // 创建人
    private String createBy = null;
}