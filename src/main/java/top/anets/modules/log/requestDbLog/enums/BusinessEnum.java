package top.anets.modules.log.requestDbLog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ftm
 * @date 2023-12-07 14:27
 */
@Getter
@AllArgsConstructor
public enum BusinessEnum {
    Custom("自定义" ),
    Hr("互认" ),
    Collect("采集任务" );


    private String name;
}
