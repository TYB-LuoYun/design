package top.anets.modules.task.history.vo;

import lombok.Data;

/**
 * @author ftm
 * @date 2024-04-07 11:38
 */
@Data
public class OperateVo {
    private String jobName;

    private String jobGroup;

    private Integer operateType;
}
