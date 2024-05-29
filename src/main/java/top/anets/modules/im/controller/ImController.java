package top.anets.modules.im.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.anets.modules.Mongodb.MongoDBUtil;
import top.anets.modules.im.model.MessageRecord;
import top.anets.modules.im.model.RoamMsg;
import top.anets.modules.im.model.TencentCloudImConstants;
import top.anets.modules.im.service.ImService;
import top.anets.utils.Constants;
import top.anets.utils.HttpClientUtil;
import top.anets.utils.Result;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @className: ImController
 * @author: cjl
 * @description: IM服务端相关接口控制器
 * @date: 2023/05/10 11:29
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/im")
public class ImController {
    private static final Logger logger = LoggerFactory.getLogger(ImController.class);
//
//    @Autowired
//    private SysParamsService sysParamsService;

    @Value("${im.sdkAppId:}")
    private String sdkAppId;

    @Value("${im.appManager:}")
    private String APP_MANAGER;

    @Value("${file.ROOT_PATH:}")
    private String ROOT_PATH;

    @Value("${im.expireTime:604800}")
    private Integer expireTime;
//
//    @Autowired
//    private TUserinfoService userinfoService;

    @Autowired
    private ImService imService;

    /**
     * IM生成秘钥接口
     *
     * @param userId
     * @return
     */
    @GetMapping("/genUserSig")
    public Result getUserSig(String userId) {
//        //先从redis中取
//        String userSig = (String) RedisUtil.get(Constants.REDIS_IM_USER_SIG + userId);
//        if (StringUtils.isEmpty(userSig)) {
//            userSig = GenerateUserSigUtil.genUserSig(userId);
//            //放入Redis中 KEY过期时间和秘钥过期时间一致
//            RedisUtil.set(Constants.REDIS_IM_USER_SIG + userId, userSig, expireTime);
//        }
//        Map<String, Object> result = new HashMap<>();
//        result.put("appId", sdkAppId);
//        result.put("secretKey", userSig);
//        return Result.success("", result);
        return null;
    }

    /**
     * IM查询账号是否已导入接口
     *
     * @param requestList
     * @return
     */
    @PostMapping("/accountCheck")
    public Result accountCheck(@RequestBody List<Map<String, Object>> requestList) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImConstants.ACCOUNT_CHECK, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CheckItem", requestList);
        logger.info("腾讯云im确认账号是否存在账号，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtil.doPost2(httpsUrl, jsonObject);
        logger.info("腾讯云im确认账号是否存在账号，返回结果：{}", result);
        Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
        if (TencentCloudImConstants.ACTION_STATUS_OK.equals(resultMap.get("ActionStatus"))) {
            return Result.success(TencentCloudImConstants.ACTION_STATUS_OK, resultMap.get("ResultItem"));
        } else {
            return Result.error(resultMap.get("ErrorInfo") + "");
        }
    }

    /**
     * IM导入单个账号接口
     *
     * @param requestMap
     * @return
     */
    @PostMapping("/accountImport")
    public Result accountImport(@RequestBody Map<String, Object> requestMap) {
        String userId, userName, faceUrl;
        userId = requestMap.get("userId") + "";
        userName = requestMap.containsKey("userName") ? requestMap.get("userName") + "" : "";
        faceUrl = requestMap.containsKey("faceUrl") ? requestMap.get("faceUrl") + "" : "";
        if (StringUtils.isEmpty(userId)) {
            return Result.error("userId为空");
        }
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImConstants.ACCOUNT_IMPORT, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Identifier", userId);
        if (StringUtils.isNotEmpty(userName)) {
            jsonObject.put("Nick", userName);
        }
        //头像地址
        if (StringUtils.isNotEmpty(faceUrl)) {
            jsonObject.put("FaceUrl", faceUrl);
        }
        logger.info("腾讯云im导入单个账号，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtil.doPost2(httpsUrl, jsonObject);
        logger.info("腾讯云im导入单个账号，返回结果：{}", result);
        Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
        if (TencentCloudImConstants.ACTION_STATUS_OK.equals(resultMap.get("ActionStatus"))) {
//            TUserinfo userinfo = userinfoService.getById(userId);
//            userinfo.setImRegister(Constants.INT_ONE);
//            userinfoService.updateById(userinfo);
//            //更新缓存
//            userinfoService.uploadRedisUser(userinfo.getUserID());
            return Result.success(TencentCloudImConstants.ACTION_STATUS_OK);
        } else {
            return Result.error(resultMap.get("ErrorInfo") + "");
        }
    }

    /**
     * 查询账号登录状态接口
     *
     * @param requestMap
     * @return
     */
    @PostMapping("/queryOnlineStatus")
    public Result getQueryOnlineStatus(@RequestBody Map<String, Object> requestMap) {
        String userIDs = requestMap.get("userIds").toString();
        //是否需要详情明细标志
        Integer IsNeedDetail = (Integer) requestMap.get("detail");
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImConstants.ACCOUNT_QUERY_STATE, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("To_Account", Arrays.asList(userIDs.split(",")));
        if (Constants.INT_ONE == IsNeedDetail) {
            jsonObject.put("IsNeedDetail", IsNeedDetail);
        }
        logger.info("腾讯云im查询状态，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtil.doPost2(httpsUrl, jsonObject);
        logger.info("腾讯云im查询状态，返回结果：{}", result);
        Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
        if (TencentCloudImConstants.ACTION_STATUS_OK.equals(resultMap.get("ActionStatus"))) {
            return Result.success(TencentCloudImConstants.ACTION_STATUS_OK, resultMap.get("QueryResult"));
        } else {
            return Result.error(resultMap.get("ErrorInfo") + "");
        }
    }

    /**
     * 导入单聊历史数据
     *
     * @param orderId
     * @return
     */
    @GetMapping("/getImRoamMessage")
    public Result getImRoamMessage(@Validated @NotEmpty(message = "订单id不能为空") String orderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("doctorInquiryId").is(orderId));
        query.with(Sort.by(Sort.Direction.ASC, "msgTimeStamp", "msgSeq"));
        List<RoamMsg> roamMsgs = MongoDBUtil.list(query, RoamMsg.class);
        if (0 == roamMsgs.size()) {
            return Result.success(Lists.newArrayList());
        }
        return Result.success("", roamMsgs);
    }

    /**
     * 此接口改为记录专家问诊相关信息
     * 拉取单聊历史消息 （）
     * 通过应答中的 Complete 字段查看是否已拉取请求的全部消息。
     *
     * @param messageRecord
     * @return
     */
    @PostMapping("/saveMsg")
    public Result saveMsg(@RequestBody MessageRecord messageRecord) {
        Result result = imService.saveMsg(messageRecord);
        return result;
    }

    /**
     * 拼接腾讯im请求路径
     *
     * @param imServiceApi
     * @param random
     * @return
     */
    private String getHttpsUrl(String imServiceApi, Integer random) {
        return null;
//        SysParams params = sysParamsService.selectByKey(Constants.IM_URL_PREFIX);
//        String userSig = (String) RedisUtil.get(Constants.REDIS_IM_USER_SIG + APP_MANAGER);
//        if (StringUtils.isEmpty(userSig)) {
//            userSig = GenerateUserSigUtil.genUserSig(APP_MANAGER);
//            //放入Redis中 KEY过期时间和秘钥过期时间一致
//            RedisUtil.set(Constants.REDIS_IM_USER_SIG + APP_MANAGER, userSig, expireTime);
//        }
//        return String.format("%s%s?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json",
//                params.getSysValue(), imServiceApi, sdkAppId, APP_MANAGER, userSig, random);
    }

    /**
     * 获取Im页面上不同的服务类型对应的可用权限（即按钮）
     *
     * @param serveType
     * @return
     */
    @GetMapping("/getServeTypeAuthorityJSON")
    public Result getServeTypeAuthorityJSON(Integer serveType) {
//        SysParams sysParam = null;
//        //在线指导
//        if (Constants.INT_FOUR.equals(serveType)) {
//            sysParam = sysParamsService.selectByKey("ServeType_Authority_ZXZD");
//        }
//        //在线复诊
//        else if (Constants.INT_FIVE.equals(serveType)) {
//            sysParam = sysParamsService.selectByKey("ServeType_Authority_ZXFZ");
//        }
//        if (ObjectUtils.isNotEmpty(sysParam)) {
//            String sysValue = sysParam.getSysValue();
//            JSONArray jsonArray = JSONObject.parseArray(sysValue);
//            return Result.success(jsonArray);
//        }
        return null;
    }
}
