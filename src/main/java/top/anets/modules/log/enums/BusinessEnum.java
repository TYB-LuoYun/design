package top.anets.modules.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ftm
 * @date 2023-12-07 14:27
 */
@Getter
@AllArgsConstructor
public enum BusinessEnum {
    Custom("自定义",ServiceEnum.DEFAULT),
    Hr("互认",ServiceEnum.DEFAULT), 
    Collect("采集任务",ServiceEnum.DEFAULT);


    private String name;
    private ServiceEnum serviceEnum;

}
