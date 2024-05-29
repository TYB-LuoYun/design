package top.anets.modules.wx.model.gzh;

import lombok.Data;

@Data
public class DoctorWorkStatistics {
    /**
     * 医院名称
     */
    private String orgName;
    /**
     * 医生姓名
     */
    private String userName;
    /**
     * 医生头像
     */
    private String avatarUrl;
    /**
     * 报告书写量
     */
    private int writedCount = 0;
    /**
     * 比上周加或者减了多少份
     */
    private int diagnosisCountChange = 0;
    /**
     * 最早审核时间
     */
    private String auditedMinTime;
    /**
     * 最晚审核时间
     */
    private String auditedMaxTime;
    /**
     * 最早书写时间
     */
    private String writedMinTime;
    /**
     * 最晚书写时间
     */
    private String writedMaxTime;
    /**
     * 报告审核量
     */
    private int auditedCount = 0;
    /**
     * 本周累计完成
     */
    private int diagnosisCount = 0;
    /**
     * 上边的文案
     */
    private String topCopyWriting;
    /**
     * 下边的文案
     */
    private String bottomCopyWriting;
}
