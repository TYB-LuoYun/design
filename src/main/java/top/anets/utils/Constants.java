package top.anets.utils;

/**
 * 常量表
 *
 * @author nieyanbin
 */
public class Constants {
    /**
     * 异常信息统一头信息<br>
     * 非常遗憾的通知您,程序发生了异常
     */
    public static final String Exception_Head = "OH,MY GOD! SOME ERRORS OCCURED! AS FOLLOWS :";
    /**
     * 客户端语言
     */
    public static final String USERLANGUAGE = "userLanguage";
    /**
     * 客户端主题
     */
    public static final String WEBTHEME = "webTheme";
    /**
     * 当前用户信息
     */
    public static final String CURRENT_USER = "CURRENT_USER";
    /**
     * 在线用户数量
     */
    public static final String ALLUSER_NUMBER = "ALLUSER_NUMBER";
    /**
     * 登录用户数量
     */
    public static final String USER_NUMBER = "USER_NUMBER";
    /**
     * 上次请求地址
     */
    public static final String PREREQUEST = "PREREQUEST";
    /**
     * 上次请求时间
     */
    public static final String PREREQUEST_TIME = "PREREQUEST_TIME";
    /**
     * 非法请求次数
     */
    public static final String MALICIOUS_REQUEST_TIMES = "MALICIOUS_REQUEST_TIMES";
    /**
     * 缓存命名空间
     */
    public static final String CACHE_NAMESPACE = "webris:";
    /**
     * 收费员CODE
     */
    public final static String CODE_TYPE_COLL_CODE = "COLL_CODE";

    /**
     * 是否删除
     */
    public static final Integer DELETED_YES = 1;
    public static final Integer DELETED_NO = 0;
    /**
     * 启用状态
     */
    public static final Integer STATUS_ENABLE = 1;
    public static final Integer STATUS_DISABLE = 0;
    /**
     * 是否永久
     */
    public static final Integer STATUS_FOREVER_YES = 1;
    public static final Integer STATUS_FOREVER_NO = 0;
    /**
     * 是否学生
     */
    public static final Integer IS_STUDENT_YES = 1;
    public static final Integer IS_STUDENT_NO = 0;
    /**
     * 是否锁定
     */
    public static final Integer IS_LOCK_YES = 1;
    public static final Integer IS_LOCK_NO = 0;
    /**
     * 是否开启
     */
    public static final String OPEN = "ON";
    public static final String CLOSE = "OFF";


    /**
     * 是否总院标记
     */
    public static final Integer IS_TopHosptal_YES = 1;
    public static final Integer IS_TopHosptal_NO = 0;

    /**
     * 是否上级院区标记
     */
    public static final Integer IS_HighHosptal_YES = 1;
    public static final Integer IS_HighHosptal_NO = 0;

    /**
     * 是否实习
     */
    public static final Integer IS_PRACTICE_YES = 1;
    public static final Integer IS_PRACTICE_NO = 0;

    /**
     * 有无影像
     */
    public static final Integer EXIS_TIMAGE_YES = 1;
    public static final Integer EXIS_TIMAGE_NO = 0;

    /**
     * 是否默认值
     */
    public static final Integer IS_DEFAULT_YES = 1;
    public static final Integer IS_DEFAULT_NO = 0;

    /**
     * 退费状态 0：无退费；1：待退费；2：已退费
     */
    public static final Integer UN_RETURN_PREMIUM_FLAG = 0;
    public static final Integer WAIT_RETURN_PREMIUM_FLAG = 1;
    public static final Integer OK_RETURN_PREMIUM_FLAG = 2;

    /**
     * 编码类型
     */
    public final static String CODE_TYPE_CITY_CODE = "CITY_CODE";
    public final static String CODE_TYPE_AREA_CODE = "AREA_CODE";
    public final static String CODE_TYPE_STREET_CODE = "STREET_CODE";

    /**
     * 以分为单位的因子
     */
    public final static int TIME_FACTORY = 100;
    public final static double DIVISION_FACTORY = 100.00;
    public final static int TIME_FACTORY_HUN = 10000;

    /**
     * 常量
     */
    public final static Integer INT_ZERO = 0;
    public final static Integer INT_ONE = 1;
    public final static Integer INT_TWO = 2;
    public final static Integer INT_THREE = 3;
    public final static Integer INT_FOUR = 4;
    public final static Integer INT_FIVE = 5;
    public final static Integer INT_SIX = 6;
    public final static Integer INT_SEVEN = 7;
    public final static Integer INT_EIGHT = 8;
    public final static Integer INT_NINE = 9;
    public final static Integer INT_TEN = 10;
    public final static Integer INT_TWENTY = 20;
    public final static Integer INT_THIRTY = 30;
    public final static Integer INT_THIRTY_TWO = 32;
    public final static Integer ONEWEEK_MILLISECOND = 7 * 24 * 60 * 60 * 1000; // 7天毫秒数
    public final static Integer ONEDAY_MILLISECOND = 24 * 60 * 60 * 1000; // 1天毫秒数
    public final static Integer ONEHOUR_MILLISECOND = 60 * 60 * 1000; // 1小时毫秒数
    public final static Integer TWENTY_HOUR_SECOND = 20 * 60 * 60 ; // 20小时秒数
    public final static String STRING_ZERO = "0";
    public final static String STRING_ONE = "1";
    public final static String STRING_TWO = "2";
    public final static String STRING_THREE = "3";
    public final static String STRING_SIX = "6";
    public final static String STRING_SEVEN = "7";


    //平台数据字典 --造影剂参数类型
    public final static String CONTRAST_AGENT_PARAM_TYPE = "contrastAgentParamType";
    //平台数据字典 --放射胶片尺寸
    public final static String RIS_FILMSIZE = "RISfilmsize";
    //平台数据字典 --放射胶片尺寸
    public final static String RIS_FILM_SPECIFICATION = "RISFilmSpecification";
    //平台系统参数——电子申请单接口是否存在 1存在  0不存在
    public final static String HAVE_ECASE = "HaveEcase";
    public final static String HAVE_ECASE_YES = "1";
    public final static String HAVE_ECASE_NO = "0";
    //平台系统参数——是否开启叫号 1开启 0关闭
    public final static String IS_CALLOUT = "IsCallout";
    public final static String IS_CALLOUT_YES = "1";
    public final static String IS_CALLOUT_NO = "0";
    //平台系统参数——HIS接口是否存在 1存在  0不存在
    public final static String HAVE_HIS = "HaveHis";
    public final static String HAVE_HIS_YES = "1";
    public final static String HAVE_HIS_NO = "0";
    //科室参数——病理系统接口 0：不存在；1：存在
    public final static String HAVE_PATHOLOGY_BASE = "HavePathologyBase";
    public final static String HAVE_PATHOLOGY_BASE_YES = "1";
    public final static String HAVE_PATHOLOGY_BASE_NO = "0";
    //科室参数——校验检查部位是否允许重复检查 如果配置为0或者""，则不进行控制，否则进行控制；
    public final static String REP_EXAM_HOURINTERVAL = "RepExamHourinterval";
    public final static String REP_EXAM_HOURINTERVAL_YES = "1";
    public final static String REP_EXAM_HOURINTERVAL_NO = "0";
    //科室参数——修改检查信息 0：都允许修改；1：QA结束之前允许修改；2：报告书写之前允许修改；3：报告审核之前允许修改
    public final static String REG_INFO_CAN_EDIT = "RegInfoCanEdit";
    //平台系统参数——患者编号生成规则 1等于病例号  2放射科独立编号
    public final static String PATIENT_ID_CREATE_RULE = "PatientIDCreateRule";
    public final static String PATIENT_ID_CREATE_RULE_YES = "2";
    public final static String PATIENT_ID_CREATE_RULE_NO = "1";
    public final static String MIRTH_URL = "MirthURL";
    public final static String REDIS_QUEUE_CURRENT_VALUE = "redis_queue_current_value:";
    //系统参数---是否开启ca签名
    public final static String CAFLAG = "CaFlag";
    //接口参数---北京ca接口地址
    public final static String BJCAWEBSERVICEENDPOINT = "BJCaWebServiceEndpoing";
    public final static String BJCAWEBSERVICEAPPNAME = "BJCaWebServiceAppName";
    public final static String BJCASIGNVERIFYURL = "BJCaSignVerifyUrl";
    public final static String HTTP = "http";
    public final static String HTTPS = "https";


    /**
     * 报告书写模板定义分类
     */
    public final static String OTHER_TEMPLATE_CLASS = "其他";
    /**
     * 书写模板挂接方式
     */
    public final static String WRITE_TEMPTYPE_ENABLE = "WriteTempTypeEnable";

    //检查参数
    /**
     * 挂起插入位置
     */
    public final static String RENEW_INSERT_POSITION = "RenewInsertPosition";
    /**
     * 推迟人次
     */
    public final static String POSTPONE_NUM = "PostponeNum";
    /**
     * 提前人次
     */
    public final static String ADVANCED_NUM = "AdvancedNum";
    //急诊不呼叫与屏显  0：不呼叫；1：呼叫
    public final static String EMERGENCYTREATMENT_CALLOUT = "EmergencytreatmentCallout";
    public final static String EMERGENCYTREATMENT_CALLOUT_YES = "1";
    public final static String EMERGENCYTREATMENT_CALLOUT_NO = "0";
    //挂起不呼叫与屏幕  0：不呼叫；1：呼叫
    public final static String HANGUP_CALLOUT = "hangupCallout";
    public final static String HANGUP_CALLOUT_YES = "1";
    public final static String HANGUP_CALLOUT_NO = "0";
    /**
     * 呼叫屏上显示的人数
     */
    public final static String WAIT_PATIENT_COUNT = "WaitPatientCount";


    /**
     * 默认每页数量
     */
    public final static String PAGESIZE = "100";
    public final static int PAGESIZE_INT = 100;

    // 权限顶级parentId
    public final static Long PERMISSION_ZERO = 0L;

    public final static Long LONG_ZERO = (long) 0;

    // 是否通用
    public final static Integer ISNOTCOMMON = 0;
    public final static Integer ISCOMMON = 1;

    // 是否公用
    public final static Integer IS_PUBLIC = 0;
    public final static Integer IS_NOT_PUBLIC = 1;

    // 是否包含下级
    public final static Integer CONTAINS_NO = 0;// 不包含
    public final static Integer CONTAINS_YES = 1;// 包含

    // 是否医技科室
    public final static Integer ISPACS_NO = 0;//否
    public final static Integer ISPACS_YES = 1;//是

    public final static String STAT_TIME_AREA_PARM = "00:00:00";// 统计时间区间，时间点，小时开始

    /* 是否超级管理员 */
    public final static Integer SUPER_ADMIN_YES = 1;// 超级管理员
    public final static Integer SUPER_ADMIN_NO = 0;// 非超级管理员

    /* 是否统计 */
    public final static Integer NEED_STAT_YES = 1;// 统计
    public final static Integer NEED_STAT_NO = 0;// 不统计

    /* 用户权限 */
    public final static String RIGHT_RESHOOT = "重新摄片";
    public final static String RIGHT_ADDSHOOT = "追加摄片";
    public final static String REPORT_UNLOCK = "报告解锁";
    public final static String RAPID_WRITER = "快速书写";
    public final static String RIGHT_AUDIT = "快速审核";
    public final static String RIGHT_SEE_DELETE = "查看删除检查";

    /* 用户菜单权限 */
    public final static String WRITE_MENU = "040202";//书写菜单
    public final static String AUDIO_MENU = "040203";//审核菜单

    /* 用户功能权限*/
    public final static String WRITE_RIGHT = "8";//书写功能
    public final static String AUDIO_RIGHT = "9";//审核功能

    // DataSourceType
    public final static Integer DATASOURCE_DATA = 0;// 数据同步
    public final static Integer DATASOURCE_WPACS = 1;// WPACS的数据传输

    /**
     * 缓存column值前缀
     */
    // 数据字典
    public final static String DICTIOINARY_CACHE = "DICTIOINARY_CACHE_";
    // 区域
    public final static String AREA_CACHE = "AREA_CACHE_";
    // 部门
    public final static String DEPT_CACHE = "DEPT_CACHE_";
    // 参数
    public final static String PARAMTER_CACHE = "PARAMTER_CACHE_";


    // 信息超时时间(毫秒)
    public static final long overtime = 60 * 1000;

    // Sensor Signal Type
    public static final int HANDLE_FAILURE = 0;// 处理失败
    public static final int HANDLE_SUCCESS = 1;// 处理成功

    // spring 分布式缓存值
    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public static final String SPRING_SESSION_ID = "spring:session:sessions:";
    public static final String SPRING_SESSION_USERVO = "spring_session_userVo:";
    public static final String SPRING_SESSION_USER_SELECTRIGHT = "spring_session_user_selectRight:";
    public static final String LOG = "spring:session:log:";

    public static final String REPORT_EXAMINE_LOCKED = "report_examine_locked:";

    public static final String PACS_IMAGE_DOWNLOAD_LOCKED = "pacs_image_download_locked:";

    public static final String REDIS_PACS_IMAGE_COUNT = "redis_pacs_image_count:";

    public static final String ADMIN = "admin";

    public static final String DJPACS001 = "DJPACS001";

    public static final String UTF8 = "UTF-8";

    public static final String PDF_TAIL = ".pdf";
    public static final String XLS_TAIL = ".xls";
    public static final String DOCX_TAIL = ".docx";
    public static final String FINGINGS = "findings";
    public static final String DIAGNOSIS = "diagnosis";
    public static final String DIAGNOSISFS = "diagnosisFs";
    public static final String FINGINGSFS = "findingsFs";

    public static final String PDF = ".PDF";


    /**
     * 省份
     */
    public static final String REDIS_PROVINCE_KEY = "redis_province_key";

    /**
     * 市区
     */
    public static final String REDIS_CITY_KEY = "redis_city_key";

    /**
     * 乡镇
     */
    public static final String REDIS_TOWN_KEY = "redis_town_key";

    /**
     * 平台地区管理左侧树形栏
     */
    public static final String REDIS_AREA_LEFT_MENU_KEY = "redis_area_left_menu_key";

    /**
     * 平台院区左侧树形栏
     */
    public static final String REDIS_HOSPITAL_LEFT_MENU_KEY = "redis_hospital_left_menu_key";
    /**
     * 系统用户
     */
    public static final String REDIS_SYS_USER = "redis_sys_user_id:";

    /**
     * 系统用户的ca签名唯一标识
     */
    public static final String REDIS_SYS_USER_CA_NUMBER = "redis_sys_user_ca_number:";
    /**
     * 平台字典集合
     */
    public static final String REDIS_DATA_DIC = "redis_data_dic:";
    /**
     * 平台字典值
     */
    public static final String REDIS_DATA_DIC_VALUE = "redis_data_dic_value:";
    /**
     * 系统字典集合
     */
    public static final String REDIS_SYS_DATA_DIC = "redis_sys_data_dic:";
    /**
     * 系统字典值
     */
    public static final String REDIS_SYS_DATA_DIC_VALUE = "redis_sys_data_dic_value:";
    /**
     * 平台检查类型(BaseExamineType)
     */
    public static final String REDIS_EXAMINE_TYPE = "redis_examine_type";

    /**
     * 系统检查类型List
     */
    public static final String REDIS_SYS_EXAMINE_TYPE = "redis_sys_examine_type";

    /**
     * 系统检查类型
     */
    public static final String REDIS_SYSEXAMINETYPE = "redis_sys_examine_type:";

    /**
     * 检查类型运维初始化数据(SysExamineType)
     */
    public static final String REDIS_EXAMINE_TYPE_INIT = "redis_examine_type_init";

    /**
     * 检查部位运维初始化数据
     */
    public static final String REDIS_EXAMINE_BODY_PART_INIT = "redis_examine_body_part_init";

    /**
     * 检查部位系统数据
     */
    public static final String REDIS_EXAMINE_BODY_PART = "redis_examine_body_part";

    /**
     * 部位拆分运维初始化数据
     */
    public static final String REDIS_EXAMINE_PART_SPLIT_INIT = "redis_examine_part_split_init";

    /**
     * 部位拆分系统数据
     */
    public static final String REDIS_EXAMINE_PART_SPLIT = "redis_examine_part_split";

    /**
     * 造影剂信息运维初始化数据
     */
    public static final String REDIS_CONTRAST_AGENT_INFO_INIT = "redis_contrast_agent_info_init";

    /**
     * 造影剂信息系统数据
     */
    public static final String REDIS_CONTRAST_AGENT_INFO = "redis_contrast_agent_info";

    /**
     * 造影剂类型运维初始化数据
     */
    public static final String REDIS_CONTRAST_TYPE_INIT = "redis_contrast_type_init";

    /**
     * 造影剂类型系统数据
     */
    public static final String REDIS_CONTRAST_TYPE = "redis_contrast_type";

    /**
     * 质控模板
     */
    public static final String REDIS_QC_SCORE_TEMPLATE = "redis_qc_score_template";
    /**
     * 平台院区
     */
    public static final String REDIS_HOSPITAL = "redis_hospital";

    /**
     * 诊断医院
     */
    public static final String REDIS_DIANOSIS_HOSPITAL = "redis_dianosis_hospital";

    /**
     * 检查类型
     */
    public static final String REDIS_EXAMINETYPE = "redis_ExamineType";

    /**
     * 检查项目
     */
    public static final String REDIS_EXAMINEITEM = "redis_ExamineItem:";

    /**
     * 就诊类别list
     */
    public static final String REDIS_STAYINSU = "redis_StayInsu";

    /**
     * 就诊类别list
     */
    public static final String REDIS_STAY_INSU = "redis_stay_insu:";
    /**
     * 检查机房
     */
    public static final String REDIS_EXAMINE_ROOM = "redis_examine_room:";
    /**
     * 设备列表
     */
    public static final String REDIS_DEVICE_LIST = "redis_device_list:";
    /**
     * 平台系统参数
     */
    public static final String SYS_DEP_DATA_DIC_VALUE = "sys_dep_data_dic_value";

    /**
     * 科室
     */
    public static final String REDIS_DEPARTMENT = "redis_department";

    /**
     * workState数据缓存
     */
    public static final String REDIS_WORK_STATE = "redis_work_state";

    /**
     * mirth数据缓存 _userID_hisID
     */
    public static final String REDIS_MIRTH = "redis_mirth:";
    /**
     * 用户初始化密码
     */
    public static final String PASSWORD_INIT = "88888888";

    /**
     * 初始化数据的握手密码
     */
    public static final String PWD_INIT_DATA = "1314520dj";
    /**
     * 初始化数据 传输量
     */
    public static final Integer LIMIT_INIT_DATA = 10;
    /**
     * 初始化数据 传输最大数
     */
    public static final Integer INDEX_INIT_DATA = 10000;

    public static final String SITE_WIDE_SECRET = "daijiang";
    /**
     * 查看影像的握手密码
     */
    public static final String PWD_DJPACS = "DJPACS";
    /**
     * 分隔符
     */
    public static final String SPLIT_REGEX = "\\^";
    public static final String LINE_FEED = "\n";
    /**
     * jrxml末尾命名
     */
    public static final String JRXML_TAIL = ".jrxml";
    /**
     * jasper末尾命名
     */
    public static final String JASPER_TAIL = ".jasper";
    /**
     * 图片末尾命名
     */
    public static final String IMAGE_TAIL = ".jpg";

    /**
     * 排队号规则前缀
     */
    public static final String QUEUE_NUMBER = "spring:session:queue_number:";
    /**
     * 号码配置前缀
     */
    public static final String CONFIG_NUMBER = "spring:session:config_number:";
    /**
     * 号码配置具体日期最大值
     */
    public static final String CONFIG_NUMBER_NO = "spring:session:config_number_no:";
    /**
     * 被锁定的号码
     */
    public static final String QUEUE_NUMBEER_LOCKED = "spring:session:queue_number_locked:";

    /**
     * IM访问秘钥
     */
    public static final String REDIS_IM_USER_SIG = "silence:im_user_sig:";

    /**
     * 已收费
     */
    public static final int IS_CHARGE = 1;

    /**
     * 未收费
     */
    public static final int IS_NOT_CHARGE = 0;

    //急诊
    public static final String EMERGENCY_CHINAME = "急诊";

    public static final String DATASOURCE = "redis_datasource";

    //院区id
    public static final String REDIS_HOSPITALID_SELF = "redis_hospitalID_self";

    public static final String DRIVERCLASS = "com.mysql.jdbc.Driver";

    public static final String EMITTER = "spring:session:emitter:";
    /*
    费用统计			FeesStat
    科室费用（申请科室费用）	ReqOfficeStat
    送检费用（申请医生费用）	ReqDoctorStat
    就诊类别费用		StayInsuStat
    零费用检查		ZeroFeesStat
     */
    public static final String FEES_STAT = "FeesStat";
    public static final String REQ_OFFICE_STAT = "ReqOfficeStat";
    public static final String REQ_DOCTOR_STAT = "ReqDoctorStat";
    public static final String STAY_INSU_STAT = "StayInsuStat";
    public static final String ZERO_FEES_STAT = "ZeroFeesStat";

    public static final String WORK_LOAD_SUMMARY = "WorkLoadSummary";
    public static final String EXAM_ROOM_WORK = "ExamineRoomStat";
    public static final String TECHNICIAN_WORK = "TechnicianWork";
    public static final String DEFAULT_START_DATE = "1970-07-01 00:00:00";

    //本地参数
    public static final String REDIS_LOCAL_PARAMETER = "redis_local_parameter:";
    //表格配置
    public static final String REDIS_CONFIG_TABLE = "redis_config_table:";
    //系统参数
    public static final String REDIS_DEP_PARA = "redis_dep_para:";
    //系统日志
    public static final String REDIS_LOG = "redis_log:";

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String ERROR = "error";
    public static final String EMPTY_IMAGE_FILE = "emptyImage.png";




    public static final String SMS_SEND_URL = "/api/SmsService/SendSmsCode";
    public static final String SMS_CHECK_URL = "/api/SmsService/CheckSmsCode";
    /**
     * 自定义短信内容发送接口
     */
    public static final String Send_Simple_Sms = "/api/SmsService/SendSimpleSms";

    public static final String ACCESS_TOKEN = "access_token";

    //疾病已读收藏 状态类型 1-已读 2-已收藏
    public static final int READED = 1;
    public static final int COLLECTED = 2;

    //分享有效期
    public static final int LONG_TIME = 0;
    public static final int ONE_DAY = 1;
    public static final int TWO_DAYS = 2;
    public static final int THREE_DAYS = 3;

    //分享途径
    public static final int SHORT_LINK = 1;
    public static final int QR_CODE = 2;

    //分享内容
    public static final int REPORT_AND_IMAGE = 1;
    public static final int ONLY_IMAGE = 2;

    //常量 filePath 文件路径
    public static final String FILE_PATH = "filePath";

    //常量 code 编码
    public static final String CODE = "code";

    /**
     * * 阅片链接供应商 WPACS
     */
    public static final String WPACS = "wpacs";

    /**
     * * 阅片链接供应商 Eunity
     */
    public static final String EUNITY = "eunity";

    /**
     * * 阅片链接供应商 Eunity的第一个接口的providerid
     */
    public static final String EUNITY_INTERFACE_ONE_PROVIDERID = "CIoLi6ErdDzDGoKN";

    /**
     * * 阅片链接供应商 WPACS的接口调用地址
     */
    public static final String WPACS_URL = "/Image/GetWPACSVIEWUrl";

    /**
     * * 阅片链接供应商 EUNITY的GetImageUrlByAccNo接口的调用地址
     */
    public static final String EUNITY_ONE_URL = "/Image/GetImageUrlByAccNo";

    /**
     * * 阅片链接供应商 EUNITY的GetImageUrl接口的调用地址
     */
    public static final String EUNITY_TWO_URL = "/Image/GetImageUrl";


    /**
     * 判断阅片连接是否存在
     */
    public static final String IMAGE_IS_EXISTS = "/Image/ImageIsExistsWithDynamic";

    /**
     * DCM地址
     */
    public static final String GET_WPACSVIEW_URL = "/Image/GetWPACSVIEWUrl";
    /**
     * 非DCM地址
     */
    public static final String GET_IMAGE_URL_NOT_DICOM = "/Image/GetImageUrlNotDICOM";

    /**
     * * EUNITY第一个接口对应的要传递的参数 accessionNumber
     */
    public static final String ACCESSIONNUMBER = "accessionNumber";

    /**
     * * EUNITY第一个接口对应的要传递的参数 hospitalCode
     */
    public static final String HOSPITALCODE = "hospitalCode";

    /**
     * * EUNITY第一个接口对应的要传递的参数 examType
     */
    public static final String EXAMTYPE = "examType";

    /**
     * * EUNITY第二个接口对应的要传递的参数 studyIDs
     */
    public static final String STUDYUID = "studyUID";
    /**
     * 图片base64前缀
     */
    public static final String IMG_Base64_Prefix = "data:image/jpeg;base64,";


    /**
     * * 成功code-String类型
     */
    public static final String STRING_SUCCESS_CODE = "200";

    /**
     * * 结果
     */
    public static final String STRING_RESULT = "Result";

    /**
     * * 男
     */
    public static final String STRING_MAN = "男";

    /**
     * * 女
     */
    public static final String STRING_FEMALE = "女";

    /**
     * * 男缩写
     */
    public static final String STRING_MAN_SHORT = "M";

    /**
     * * 女缩写
     */
    public static final String STRING_FEMALE_SHORT = "F";


    /**
     * 检查量-根据科室名查询详情
     */
    public static final String GETCHECKBYREQUESTDRNAME = "/Statistics/GetCheckByRequestDRName";


    /**
     * 检查量-查询全部数据
     */
    public static final String GETCHECKALLCOUNTDATA = "/Statistics/GetCheckAllCountData";

    /**
     * 检查量-分页查询
     */
    public static final String GETCHECKCOUNTCOUNTDATABYPAGE = "/Statistics/GetCheckCountCountDataByPage";


    /**
     * 开单量-根据医生姓名查询详细
     */
    public static final String GETOPENORDERBYREQUESTDRNAME = "/Statistics/GetOpenOrderByRequestDRName";


    /**
     * 开单量-查询全部数据
     */
    public static final String GETOPENORDERALLCOUNTDATA = "/Statistics/GetOpenOrderAllCountData";


    /**
     * 开单量-分页查询
     */
    public static final String GETOPENORDERCOUNTDATABYPAGE = "/Statistics/GetOpenOrderCountDataByPage";

    /**
     * 修改认证信息文本
     */
    public static final String UPDATE_ECERTIFICATION_BODY_MSG = "该医生修改了认证信息,需二次审核";


    /**
     * 钉钉异常推送接口中MsgType之一(content)
     */
    public static final String CONTENT = "content";

    /**
     * 钉钉异常推送接口返回参数中的状态ERRCODE
     */
    public static final String ERRCODE = "errcode";

    /**
     * 钉钉异常推送接口返回参数中的异常描述errmsg
     */
    public static final String ERRMSG = "errmsg";


    /**
     * 登录异常文本
     */
    public static final String LOGIN_ERR_MSG = "您的账号已停用!";
    public static final String LOGIN_NULL_ERR_MSG = "登录失效!";

    /**
     * 授权异常
     */
    public static final String AUTHORIZE_NULL_ERR_MSG = "您的医院未申请授权该功能!";
    public static final String AUTHORIZE_ING_ERR_MSG = "该功能申请授权中!";
    public static final String AUTHORIZE_PAST_DUE_ERR_MSG = "该功能授权已过期!";
    public static final String ILLEGALITY_CODE_ERR_MSG = "非法授权码!";

    /**
     * 授权秘钥
     */
    public static final String AUTHORIZATION_KEY = "hangzhourongyugs";

    /**
     * 拉取数据特定账号
     */
    public static final String SPECIAL_USER_ACCOUNT = "拉取数据专用用户";
    public static final String SPECIAL_USER_PASSWORD = "hangzhourongyukejiyouxiangongsi";

    /**
     * 一天的秒数
     */
    public static final int ONE_DAY_SECONDS = 86400;

    /**
     * 医生端
     */
    public static final String PROGRAM_TYPE_DOCTOR = "Doc";

    /**
     * 患者端
     */
    public static final String PROGRAM_TYPE_PATIENT = "Pat";

    /**
     * 十分钟秒数
     */
    public static final int TEN_MINUTES_SECONDS = 600;

    /**
     * 一分钟秒数
     */
    public static final int ONE_MINUTE_SECONDS = 60;

    /**
     * 三分钟秒数
     */
    public static final int THREE_MINUTES_SECONDS = 180;

    /**
     * 查询影像列表接口
     */
    public static final String SELECTLIST = "/image/selectList";
    /**
     * 查询患者列表接口
     */
    public static final String LISTPATIENT = "/spanHospital/listPatient";

    /**
     * 查询快速影像列表接口
     */
    public static final String FASTIMAGE = "/study/fastImage";


    /**
     * 查询影像详情接口
     */
    public static final String SELECTINFO = "/image/selectInfo";

    /**
     * 阅片
     */
    public static final String VIEWIMAGE = "/study/viewImage";
    /**
     * 检验分享密码验证接口
     */
    public static final String CHECKSHAREPWD = "/h5DigitalImage/checkSharePwd";

    /**
     * 获取相关数据
     */
    public static final String GETH5DETAIL = "/h5DigitalImage/getH5Detail";


    /**
     * 检验有效期接口
     */
    public static final String CHECKTIMESTAMP = "/h5DigitalImage/checkTimestamp";


    /**
     * 分享
     */
    public static final String SHARE = "/h5DigitalImage/share";

    /**
     * 终端查看pdf报告接口
     */
    public static final String GETPDFREPORT = "/h5DigitalImage/getPdfReport";

    /**
     * 查询PDF报告API_URL
     */
    public static final String PDF_API_URL = "/WxNoAuth/GetFilePath";

    public static final String PATIENTID = "patientId";
    /**
     * 体验版
     */

    public static final String TRIAL = "trial";
    /**
     * 开发版
     */

    public static final String DEVELOPER = "developer";

    /**
     * 正式版
     */
    public static final String FORMAL = "formal";

    /**
     * 用户id对应的微信code
     */
    public static final String USERID_BINDED_WXCODE = "userid_binded_wxcode";

    public final static Integer INT_ONE_HUNDRED = 100;

    /**
     * 文字安全识别枚举值（1 资料；2 评论；3 论坛；4 社交日志）
     */
    public static final int MSG_SEC_CHECK_SCENE_MSG = 2;

    /**
     * 图片安全识别枚举值（1 资料；2 评论；3 论坛；4 社交日志）
     */
    public static final int MSG_SEC_CHECK_SCENE_MEDIA = 3;

    /**
     * 文本内容安全识别
     */
    public static final String MSGSECCHECK_URL = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=";

    /**
     * 图片内容安全识别
     */
    public static final String MEDIACHECKASYNC_URL = "https://api.weixin.qq.com/wxa/media_check_async?access_token=";

    /**
     * 图片内容安全识别文件类型 1:音频;2:图片
     */
    public static final int MEDIA_TYPE = 2;

    /**
     * 用于验证消息是否来自微信服务器token
     */
    public static final String WECHAT_CHECKSIGNATURE_TOKEN = "Rytechnology888";

    /**
     * * 结果
     */
    public static final String STRING_RESULT_SMALL_LETTER = "result";

    /**
     * * trace_id
     */
    public static final String TRACE_ID = "trace_id";

    /**
     * * label
     */
    public static final String LABEL = "label";

    /**
     * * suggest
     */
    public static final String SUGGEST = "suggest";

    /**
     * * pass
     */
    public static final String PASS = "pass";

    /**
     * 微信模板id
     */
    public static final String PUSHTEMPLATEID = "pushTemplateId";
    /**
     * 微信秘钥
     */
    public static final String SECRET = "secret";
    /**
     * 图片base64前缀
     */
    public static final String IMG_BASE64_PREFIX = "data:image/jpeg;base64,";

    /**
     * * 区域相关检查
     */
    public static final String AREACHECKLIST = "/h5DigitalImage/areaCheckList";

    /**
     * * 业务参数配置的医院数据过滤key
     */
    public static final String HOSPITAL_DATA_FILTING = "HOSPITAL_DATA_FILTING";
    /**
     * 公众号业务参数key
     */
    public static final String GZHPAGE_HOSPITAL_GROP = "GZHPAGE_HOSPITAL_GROP";

    /**
     * * 医院数据过滤医院的key
     */
    public static final String DATA_FILTING_STR_KEY = "key";

    /**
     * * 医院数据过滤医院的value
     */
    public static final String DATA_FILTING_STR_Value = "value";

    /**
     * * 医院数据过滤医院的value
     */
    public static final String DATA_FILTING_STR_Operator = "operator";

    /**
     * * 正则表达式匹配字符regex模糊查询
     */
    public static final String REGEX = "regex";

    /**
     * * 正则表达式匹配字符相等
     */
    public static final String EQ = "eq";

    /**
     * * 正则表达式匹配字符大于
     */
    public static final String GT = "gt";

    /**
     * * 正则表达式匹配字符小于
     */
    public static final String LT = "lt";

    /**
     * * 正则表达式匹配字符in
     */
    public static final String IN = "in";

    /**
     * * 业务参数配置-医共体名称
     */
    public static final String GROUP_NAME = "GZHPAGE_HOSPITAL_GROP";
    /**
     * 是否开放跨院查询
     */
    public static final String OPENSPANHOSPITALSELECT = "OpenSpanHospitalSelect";

    /**
     * * 微信支付-预支付交易会话标识
     */
    public static final String WECHAT_PAY_PREPAY_ID = "WECHAT_PAY_PREPAY_ID";

    /**
     * 系统控制页面功能模块显隐性key,模糊查询开头
     */
    public static final String SHOW_STATUS_KEY_MODULEABLE = "Moduleable_";

    /**
     * 报告分享获取长链接接口的url
     */
    public static final String REPORTSHARE_QRURL = "api/Qr/GetQrUrl";

    /**
     * 报告分享获取短链接接口的url
     */
    public static final String REPORTSHARE_SHORTURL = "api/ShortLink/GetShortLink";

    /**
     * 根据生成的长链接中的参数p的值和providerId解析到相应的参数的解析接口url
     */
    public static final String REPORTSHARE_DECRYPTPARASURL = "api/Qr/DecryptParas";

    /**
     * redis医生的粉丝列表的key
     */
    public static final String REDIS_FANS_OF = "FansOf";

    /**
     * 医生问诊总量redis的key
     */
    public static final String REDIS_KEY_COUNT_DIAGNOSIS = "hospitalCode:doctors:countDiagnosis";

    /**
     * POST方式
     */
    public static final String POST_METHOD = "POST";
    /**
     * GET方式
     */
    public static final String GET_METHOD = "GET";
    /**
     * IM 访问域名前缀
     */
    public static final String IM_URL_PREFIX = "IM_URL_PREFIX";

    /**
     * 首页统计-医生粉丝数量
     */
    public static final String FANS_NUM = "fansNum";
    /**
     * 首页统计-医生服务数量
     */
    public static final String SERVE_NUM = "serveNum";
    /**
     * 首页统计-医生好评率
     */
    public static final String GOODREPUTATION_NUM = "goodReputationNum";

    public static final String IM_GET_ROAM_MSG = "IM_GET_ROAM_MSG";
    /**
     * 退款
     */
    public static final String PATIENTEND_WXPAY_REFUNDS = "/wxPay/refunds";
    /**
     * 专家问诊医生分账
     */
    public static final String MODOCTORAPI_PROFITSHARE = "/moDoctorApi/profitShare";
    /**
     * 获取家庭档案 地址
     */
    public static final String USER_RELA = "/userRela/getUserRelaById";

    /**
     * 书写文案
     */
    public static final String[] SHUXIE_ARR = {"砥砺前行&每一滴汗水都为你浇灌","实干家&体会一次次全力以赴","坚定&你会变成自己喜欢的模样","相信&你的坚持努力终将使你更好","梦想家&进步努一寸有一寸的欢喜"};
    /**
     * 审核文案
     */
    public static final String[] SHENHE_ARR = {"爱所爱&凡是过去，皆为序幕","跃起&难度就是价值的所在","改变&欲变世界，先变自己","强者&自我控制是最强者的本能","仰望&山谷的低点就是山的起点"};
    /**
     * 最早文案
     */
    public static final String[] ZAO_ARR = {"一路向阳&前方的朝阳，就是我的方向","人生无限&纵有疾风起，人生不言弃","自律&美好会在路途不期而遇","步履不停&迎着阳光，继续努力","不等待&没有比当下更好的时间"};
    /**
     * 最晚文案
     */
    public static final String[] WAN_ARR = {"如诗如歌&每一颗星星都为你照亮","时间&总会告诉你一个答案","做自己&寻找自己最舒适的节奏","揽月&即使坠落在群星之间","加油&你在为自己而努力拼搏"};

    /**
     * 灵动屏请求来源标识
     */
    public static final String SSO_MARK = "sso";
    /**
     * 灵动屏token验证路径
     */
    public static final String SSO_TOKEN_VALIDATE = "/login/validate";
    /**
     * 通知龙哥打包图像地址
     */
    public static final String H5_IMAGES_DOWNLOAD = "/api/open-platform/compress-image-task";
    /**
     * 打包结束回调地址
     */
    public static final String H5_NOTIFYURL = "/h5imagesDownload/inform";

    public static final String H5_VIDEODOWNPAGE = "/videoDownPage";

}
