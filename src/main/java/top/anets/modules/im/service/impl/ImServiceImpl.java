package top.anets.modules.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.errors.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.anets.exception.ServiceException;
import top.anets.modules.im.mapper.ImMapper;
import top.anets.modules.im.model.MessageRecord;
import top.anets.modules.im.model.RoamMsg;
import top.anets.modules.im.model.RoamMsgBody;
import top.anets.modules.im.model.TencentCloudImConstants;
import top.anets.modules.im.service.ImService;
import top.anets.modules.minio.MinioConfig;
import top.anets.modules.minio.MinioUtil;
import top.anets.utils.Constants;
import top.anets.utils.HttpClientUtil;
import top.anets.utils.Result;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImServiceImpl extends ServiceImpl<ImMapper, MessageRecord> implements ImService {

    private static final Logger logger = LoggerFactory.getLogger(ImServiceImpl.class);

    @Value("${minio.domain:}")
    private String minioDomain;

    @Value("${file.ROOT_PATH:}")
    private String ROOT_PATH;

    @Value("${im.sdkAppId:}")
    private String sdkAppId;

    @Value("${im.appManager:}")
    private String APP_MANAGER;

    @Value("${im.expireTime:604800}")
    private Integer expireTime;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MinioConfig prop;

//    @Autowired
//    private SysAndPersonalMessageService messageService;
//
//    @Autowired
//    private SysParamsServiceImpl sysParamsService;

    @Override
    public Result saveMsg(MessageRecord messageRecord) {
        String patientId = messageRecord.getPatientId();
        String doctorId = messageRecord.getDoctorId();
        Date startTime = messageRecord.getStartTime();
        Date endTime = messageRecord.getEndTime();
        StringBuilder errorMsg = new StringBuilder();
        if (StringUtils.isEmpty(patientId) || StringUtils.isEmpty(doctorId)
                || ObjectUtils.isEmpty(startTime) || ObjectUtils.isEmpty(endTime)) {
            return Result.error("缺少必填参数");
        }
        try {
            this.saveOrUpdate(messageRecord);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException)
                return Result.success("本次消息漫游已下载结束");
            errorMsg.append(e.getMessage());
        }
        //转为秒级时间戳
        Long minTime = startTime.getTime() / 1000;
        Long maxTime = endTime.getTime() / 1000;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Operator_Account", patientId);
        jsonObject.put("Peer_Account", doctorId);
        jsonObject.put("MinTime", minTime);
        jsonObject.put("MaxTime", maxTime);
        //默认100
        jsonObject.put("MaxCnt", 100);
        List<RoamMsg> msgList = new ArrayList<>();
        List<RoamMsg> roamMsgs = new ArrayList<>();
        //下载消息记录
        try {
            //下载病人医生之间的消息
            downloadMsg(jsonObject, msgList);
        } catch (Exception e) {
            messageRecord.setIsSuccess(Constants.INT_ZERO);
            this.updateById(messageRecord);
            e.printStackTrace();
            logger.error(e.getMessage());
            errorMsg.append(e.getMessage());
        }
        //图片和音频消息 保存到oss服务器，替换地址
        msgList.forEach(roamMsg -> {
            roamMsg.setDoctorInquiryId(messageRecord.getDoctorInquiryId());
            List<RoamMsgBody> bodies = roamMsg.getMsgBody();
            //msgContent 结构会根据msgType变动 msgContent 类型为object
            bodies.forEach(roamMsgBody -> {
                //音频类型
                if (TencentCloudImConstants.MESSAGE_SOUND_TYPE.equals(roamMsgBody.getMsgType())) {
                    JSONObject object = JSONObject.parseObject(JSON.toJSONString(roamMsgBody.getMsgContent()));
                    String audioUrl = object.get("Url").toString();
                    String fileName = System.currentTimeMillis() + audioUrl.substring(audioUrl.lastIndexOf("."));
                    try {
                        String objectName = minioUtil.uploadByUrl(audioUrl, 6, fileName);
                        String ossUrl = minioDomain + "/" + prop.getBucketName() + "/" + objectName;
                        object.put("Url", ossUrl);
                        roamMsgBody.setMsgContent(object);
                    } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
                        log.error(e.getMessage());
                        errorMsg.append(e.getMessage());
                    }
                    //图片类型
                } else if (TencentCloudImConstants.MESSAGE_IMAGE_TYPE.equals(roamMsgBody.getMsgType())) {
                    JSONObject object = JSONObject.parseObject(JSON.toJSONString(roamMsgBody.getMsgContent()));
                    JSONArray array = (JSONArray) object.get("ImageInfoArray");
                    JSONArray jsonArray = new JSONArray();
                    array.forEach(o ->{
                        JSONObject object1 = JSONObject.parseObject(JSON.toJSONString(o));
                        String orginImgUrl = object1.get("URL").toString();
                        //im 图片地址不是以图片类型结尾 直接指定为.png
                        String fileName = System.currentTimeMillis() + ".png";
                        String objectName = null;
                        try {
                            objectName = minioUtil.uploadByUrl(orginImgUrl, 5, fileName);
                        } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
                            errorMsg.append(e.getMessage());
                            log.error(e.getMessage());
                        }
                        String ossUrl = minioDomain + "/" + prop.getBucketName() + "/" + objectName;
                        object1.put("URL", ossUrl);
                        jsonArray.add(object1);
                    });
                    object.put("ImageInfoArray", jsonArray);
                    roamMsgBody.setMsgContent(object);
                //视频类型
                }else if (TencentCloudImConstants.MESSAGE_VIDEO_TYPE.equals(roamMsgBody.getMsgType())){
                    com.alibaba.fastjson2.JSONObject object = com.alibaba.fastjson2.JSONObject.parseObject(JSON.toJSONString(roamMsgBody.getMsgContent()));
                    String videoUrl = object.get("VideoUrl").toString();
                    String imgUrl = object.getString("ThumbUrl");
                    String fileName = System.currentTimeMillis() + "." + object.get("VideoFormat").toString();
                    String imageFileName = System.currentTimeMillis() + ".png";
                    try {
                        String objectName = minioUtil.uploadByUrl(videoUrl,8,fileName);
                        String ossUrl = minioDomain + "/" + prop.getBucketName() + "/" + objectName;
                        String imgName = minioUtil.uploadByUrl(imgUrl,5,imageFileName);
                        String thumbUrl = minioDomain + "/" + prop.getBucketName() + "/" + imgName;
                        object.put("VideoUrl", ossUrl);
                        object.put("ThumbUrl", thumbUrl);
                        roamMsgBody.setMsgContent(object);
                    } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException |ErrorResponseException e) {
                        log.error(e.getMessage());
                        errorMsg.append(e.getMessage());
                    }
                }
            });
        });
//        SysAndPersonalMessage sysmessage = new SysAndPersonalMessage();
        if (msgList.size() > 0) {
//            Collection<RoamMsg> collection = BbsMongoDBUtil.saveBatch(msgList);
//            roamMsgs = new ArrayList<>(collection);
//            //按时间戳排序
//            roamMsgs = roamMsgs.stream().sorted(Comparator.comparing(RoamMsg::getMsgTimeStamp)).collect(Collectors.toList());
//            //取最新的一条数据
//            RoamMsg roamMsg = roamMsgs.get(roamMsgs.size() - 1);
//            sysmessage.setMessageId(roamMsg.get_id());
//            DoctorInquiry doctorInquiry = BbsMongoDBUtil.getById(messageRecord.getDoctorInquiryId(), DoctorInquiry.class);
//            sysmessage.setUserId(doctorId);
//            sysmessage.setTitle("订单消息");
//            sysmessage.setMsgType(Constants.INT_TWO);
//            sysmessage.setPlatForm(Constants.INT_ONE);
//            sysmessage.setIsRead(Constants.INT_ZERO);
//            if (StringUtil.isEmpty(doctorInquiry.getUserName())) {
//                sysmessage.setContent("您与患者的问诊已结束。");
//            } else sysmessage.setContent("您与【" + doctorInquiry.getUserName() + "】患者的问诊已结束。");
//            //医生端保存
//            messageService.saveOrUpdate(sysmessage);
        }
        if (Constants.INT_ZERO == errorMsg.length()) {
            return Result.success("本次消息漫游已下载结束");
        }
        return Result.error(errorMsg.toString());
    }


    /**
     * 下载消息方法
     *
     * @param jsonObject
     * @param returnList
     * @return
     */
    private List<RoamMsg> downloadMsg(JSONObject jsonObject, List<RoamMsg> returnList) {
//        SysParams sysParam = sysParamsService.getOne(Wrappers.<SysParams>lambdaQuery()
//                .eq(SysParams::getSysKey, Constants.IM_GET_ROAM_MSG)
//                .eq(SysParams::getIsOpen, Constants.INT_ONE)
//                .eq(SysParams::getPlatformCode, Constants.INT_ONE)
//                .eq(SysParams::getDeleteFlag, Constants.INT_ZERO), true);
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(null, random);
        logger.info("腾讯云im下载单聊历史消息，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtil.doPost2(httpsUrl, jsonObject);
        logger.info("腾讯云im下载单聊历史消息，返回结果：{}", result);
        Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
        //下载成功
        if ("OK".equals(resultMap.get("ActionStatus"))) {
            JSONArray array = (JSONArray) resultMap.get("MsgList");
            List<RoamMsg> msgList = JSON.parseArray(array.toJSONString(), RoamMsg.class);
            returnList.addAll(msgList);
            //未下完接着下载
            if (Constants.INT_ZERO == Integer.parseInt(resultMap.get("Complete") + "")) {
                jsonObject.put("MaxTime", resultMap.get("LastMsgTime"));
                jsonObject.put("LastMsgKey", resultMap.get("LastMsgKey"));
                downloadMsg(jsonObject, returnList);
            }
            //下载失败
        } else {
            throw new ServiceException(resultMap.get("ErrorInfo") + "");
        }
        return returnList;
    }

    /**
     * 拼接腾讯im请求路径
     *
     * @param imServiceApi
     * @param random
     * @return
     */
    private String getHttpsUrl(String imServiceApi, Integer random) {

//        SysParams sysParam = sysParamsService.getOne(Wrappers.<SysParams>lambdaQuery()
//                .eq(SysParams::getSysKey, Constants.IM_URL_PREFIX)
//                .eq(SysParams::getIsOpen, Constants.INT_ONE)
//                .eq(SysParams::getPlatformCode, Constants.INT_ONE)
//                .eq(SysParams::getDeleteFlag, Constants.INT_ZERO), true);
//        String imUrlPefix = sysParam.getSysValue();
        String imUrlPefix = null;
        String userSig = null;
//        String userSig = (String) RedisUtil.get(Constants.REDIS_IM_USER_SIG + APP_MANAGER);
//        if (StringUtils.isEmpty(userSig)) {
//            userSig = GenerateUserSigUtil.genUserSig(APP_MANAGER);
//            RedisUtil.set(Constants.REDIS_IM_USER_SIG + APP_MANAGER, userSig, expireTime);
//        }
        return String.format("%s%s?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json",
                imUrlPefix, imServiceApi, sdkAppId, APP_MANAGER, userSig, random);
    }


}
