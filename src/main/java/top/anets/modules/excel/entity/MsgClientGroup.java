package top.anets.modules.excel.entity;

/**
 * @author ftm
 * @date 2022/9/16 0016 16:27
 */
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @Author yjz
 * @Date 2022/1/14 22:46
 */
@Data
public class MsgClientGroup {

    // 组名
    @Excel(name = "分组")
    private String groupName = null;
    /**
     * 创建人
     */
    private String createBy;
}