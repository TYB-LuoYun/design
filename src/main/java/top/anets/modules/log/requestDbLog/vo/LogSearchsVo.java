package top.anets.modules.log.requestDbLog.vo;

import lombok.Data;

/**
 * @author ftm
 * @date 2024-04-18 15:55
 */
@Data
public class LogSearchsVo {
    /**
     * 时间选择值
     */

    private String timeBegin;


    private String timeEnd;

    private String classify;


    private String business;


    private String level;


}
