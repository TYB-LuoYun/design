package top.anets.modules.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author ftm
 * @date 2023-12-12 9:42
 */

@Data
@Document(collection = "E_ExamSearchList")  //表名
public class ExamSearchList {
    @Id
    private String _id;
    /**
     * 报告id
     */
    private String ReportID;
    /**
     * 检查单id
     */
    private String OrderID;
    /**
     * 病人信息id
     */
    private String PatientInfoid;

    private String PatientInfoID;

    /**
     * 检查单号
     */
    private String AccessionNumber;
    /**
     * 姓名
     */
    private String PatientName;
    /**
     * 影像id
     */
    private String StudyUID;// "c7992b0f-acf1-4d18-a849-adc000f140c3",
    private String AIFlag;// "0",
    /**
     * 检查类型
     */
    private String ExamTypeName;// "DX",
    /**
     * 检查部位
     */
    private String ExamBodyName;// "胸部正侧位片(数字化摄影)",

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date VerifyDate;// ISODate("2021-10-13T09;//32;//56.000Z"),
    /**
     * 病历号
     */
    private String MedcaseNo;// "0000416216",
    /**
     * 床位号
     */
    private String BedNo;// "14",
    /**
     * 报告状态
     */
    private String ReportStatus2;// "4080",
    /**
     * 住院号
     */
    private String InPatientNo;// "ZY010000416216",
    /**
     * 门诊号
     */
    private String OutPatientNo;// "",
    /**
     * 身份证号
     */
    private String IdCardNo;// "511011200005038304",


    /**
     * 报告时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ReportDate;// ISODate("2021-10-13T09;//11;//31.000Z"),
    @JSONField(format = "yyyy-MM-dd")
    /**
     * 检查时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ExamDate;// ISODate("2021-10-13T06;//35;//53.000Z"),
    private String EnmergencyValue;// null,
    /**
     * 就诊类型
     */
    private String PatientType;// null,
    private String RequestOrganCode;// null,
    private String RequestDepatment;// "耳鼻咽喉头颈外科",
    private String RequestDr;// "谭华丽",
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    /**
     * 登记时间
     */
    private Date OrderDate;// ISODate("2021-10-13T06;//31;//26.000Z"),

//    @Field("patientID")
//    private String patientID;// "0000416216",

    /**
     * 检查机构
     */
    private String ExamOrganCode;// "558099",


    @Field("PatientID")
    private String PatientID;// "0000416216",


    /**
     * 检查所见
     */
    private String Findings;// "    胸廓对称，纵隔居中。双肺野透过度良好，肺纹理清晰，双肺门影不大。心脏大小形态及大血管走行未见异常。双侧膈肌光滑，双侧肋膈角锐利。",
    /**
     * 诊断
     */
    private String Diagnosis;// "胸部未见明显异常。",
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date CreateDateTime;// ISODate("2022-06-27T09;//56;//23.899Z"),
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date LastUpdateTime;// ISODate("2022-06-27T09;//56;//23.899Z")
    private String ModalityNumber;


    private String HospitalCode;
    private String HospitalName;
    private String PatientSex;
    private String PatientAge;


    private String total;

    /**
     * 阴性阳性
     */
    private String ReportResult;

    /**
     * 是否危机
     */
    private Integer iscritical;


    /**
     * 申请单号
     */
    private String RequestNO;

    private String ProjectCode;

    private int IsCharge;

//    兼容

    public void setPatientInfoID(String val) {
        if (StringUtils.isNotBlank(val)) {
            this.PatientInfoid = val;
        }
    }

    public void setPatientInfoid(String val) {
        this.PatientInfoid = val;
    }


    public String getPatientInfoID() {
        if (StringUtils.isNotBlank(this.PatientInfoID)) {
            return this.PatientInfoID;
        }
        return this.PatientInfoid;
    }

    public String getPatientInfoid() {
        if (StringUtils.isNotBlank(this.PatientInfoid)) {
            return this.PatientInfoid;
        }
        return this.PatientInfoID;
    }


    public String setPatientID() {
//        return StringUtils.isEmpty(patientID)?PatientID:patientID;
        return PatientID;
    }


    public String getPatientID() {
        if (StringUtils.isNotBlank(this.PatientID)) {
            return this.PatientID;
        }
//        return  this.patientID;
        return null;
    }

    public String getPatientid() {
//        if(StringUtils.isNotBlank(this.patientID)){
//            return  this.patientID ;
//        }
        return this.PatientID;
    }


    public String getSmartPatientId() {
//        return StringUtils.isEmpty(patientID)?PatientID:patientID;
        return PatientID;
    }





}