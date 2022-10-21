package top.anets.utils.dingding;

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
    public final static Integer ONEWEEK_MILLISECOND = 7 * 24 * 60 * 60 * 1000; // 7天毫秒数
    public final static Integer ONEDAY_MILLISECOND = 24 * 60 * 60 * 1000; // 1天毫秒数
    public final static Integer ONEHOUR_MILLISECOND = 60 * 60 * 1000; // 1小时毫秒数
    public final static String STRING_ZERO = "0";
    public final static String STRING_ONE = "1";
    public final static String STRING_TWO = "2";
    public final static String STRING_THREE = "3";
    public final static String STRING_FOUR = "4";


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
    //默认价格设置
    public static final double PAY_DEFAULT_PRICE = 10.0;
    //默认服务费
    public static final double PAY_SERVICEPRICE = 5.0;
    //默认远程服务费
    public static final double PAY_REMOTEPRICE = 2.0;

    public static final String DATA_SYS_INIT_URL = "sys/initData";

    public static final String PAY_ORDERE_URL = "pay/useCurrencyForDiagnosis";

    public static final String SYS_EXAMINE_ITEM_URL = "sys/getAllExamineItem";

    //图片URL
    public static final String DJSAAS_IMG_URL = "webrisImg/";
    public static final String DJSAAS_PDF_URL = "webrisPdf/";
    //Excel  URL
    public static final String DJSAAS_EXCEL_URL = "webrisExcel/";
    //Zip  URL
    public static final String DJSAAS_ZIP_URL = "webrisZip/";

    //报告图片URL
    public static final String DJSAAS_REPORT_PACSIMG_URL = "webrisReportPacsImg/";

    public static final String PAY_ACCOUNT = "redis_pay_account:";

    public static final String PAY_DISCOUNT = "pay_discount:";

    public static final String DJPACS_NAME = "得康云影像";
    //WPACS路由
    public static final String SHARE_column_URL = "webpacs/wpswsd.asmx/Sharecolumn";
    public static final String DELETE_DCM_URL = "webpacs/wpswsd.asmx/DeleteDcm";
    public static final String MODIFY_DCM_URL = "webpacs/wpswsd.asmx/ModifyDcm";
    public static final String ASPX_URL = "wpacs.aspx";
    public static final String WPSWADO_URL = "webpacs/wpswado.aspx";

    /**
     * 融合平台手机端扫码展示路由
     */
    public static final String FUSE_MOBILE_QR = "#/reportState";

    public static final String TABLE_INFO = "redis_table_info";

    /**
     * 展示平台院区
     */
    public static final String REDIS_DISPLAY_HOSPITAL = "redis_display_hospital";

    public static final String REDIS_GETREPORTS = "redis_GetReports";

    //未被抢的检查单子池
    public static final String REPORT_UNGRAB_POOL = "report_ungrab_pool:";
    //已抢未操作的检查单子池
    public static final String REPORT_GRAB_UNOPERATED_POOL = "report_grab_unoperated_pool:";
    //用户拒绝及未响应的单子池
    public static final String REPORT_DENY_POOL = "report_deny_pool:";

    public static final String DOCTOR_GROUP_ID = "hospital_33930";

    public static final String LOGIN_ERROR_TIME = "login_error_time:";
    public static final String USER_KEY = "user_key";
    public static final String COOKIE_HEADER = "set-cookie";

    public static String HOST = "api.deepwise.com";

    public interface OpenApiPaths {

        String LAUNCH_AI = "/dw/cloudApi/aiService/v1/launchAi";

        String QUERY_AI_STATUS = "/dw/cloudApi/aiService/v1/queryAiStatus";

        /**
         * 推荐通过queryAiStatus判断AI任务状态，判断AI任务计算完成后调用viewAi获取阅片链接
         */
        @Deprecated
        String QUERY_AI_RESULT = "/dw/cloudApi/aiService/v1/queryAiResult";

        String VIEW_AI = "/dw/cloudApi/aiService/v1/viewAi";
    }

    public interface FileTypes {

        String DCM = ".dcm";

        String ZIP = ".zip";
    }

    public static final String SMS_SEND_URL = "/api/SmsService/SendSmsCode";
    public static final String SMS_CHECK_URL = "/api/SmsService/CheckSmsCode";

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
    public static final String IMAGE_IS_EXISTS = "/Image/ImageIsExists";

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
     * * EUNITY第二个接口对应的要传递的参数 studyIDs
     */
    public static final String STUDYUID = "studyUID";


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
    public static final String UPDATE_ECERTIFICATION_BODY_MSG = "改医生修改了认证信息,需二次审核";




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

}
