package cn.com.zcits.modules.illegalcase.task;

import cn.com.zcits.modules.illegalcase.entity.IllegalCaseRecord;
import cn.com.zcits.modules.illegalcase.enums.IllegalCaseWfxwEnum;
import cn.com.zcits.modules.illegalcase.service.IIllegalCaseRecordService;
import cn.com.zcits.modules.illegalcase.vo.InterruptCaseRecord;
import cn.com.zcits.modules.sysparam.entity.SysParam;
import cn.com.zcits.modules.sysparam.mapper.SysParamMapper;
import cn.com.zcits.modules.sysparam.service.ISysParamService;
import cn.com.zcits.modules.util.HttpClientUtil;
import cn.com.zcits.modules.util.Query;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.redis.cache.BladeRedis;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author LuoYun
 * @since 2022/6/29 10:34
 */
@Component
@Slf4j
public class IllegalCaseTask {
    @Autowired
    private IIllegalCaseRecordService iIllegalCaseRecordService;

    @Autowired
    private BladeRedis bladeRedis;

    private static   String access_url ="http://192.168.2.228:9008/cds/service/apply";
    private static final String last_update_time_key = "illegal_casetask_last_update_time_keys";
    private static final String illegalCaseTask_lock = "illegalCaseTask_lock";
    private static final String illegalCaseTask_interrupt = "illegalCaseTask_interrupt";

    @Autowired
    private ISysParamService sysParamService;

    @XxlJob("fetchIllegalCaseRecords")
    public ReturnT<String> fetchIllegalCaseRecords(String startTime){
        try {
            Object o = bladeRedis.get(illegalCaseTask_lock);
            if(o != null){
                return  ReturnT.SUCCESS;
            }
            bladeRedis.set(illegalCaseTask_lock,illegalCaseTask_lock);
            bladeRedis.expire(illegalCaseTask_lock,60*60*2);
            SysParam sysJobParam = sysParamService.getByCategoryCode("external_application", "18");
            String name = sysJobParam.getName();
            if(StringUtil.isNotBlank(name)){
                access_url = name;
                log.info("违法案件记录URL："+name);
            }
            long now =  System.currentTimeMillis();
            this.fetchIllegalCaseRecordsReal(startTime,1,now);
            bladeRedis.del(illegalCaseTask_lock);
        } catch (Exception e) {
            bladeRedis.del(illegalCaseTask_lock);
            e.printStackTrace();
            log.error("取违法记录案件开始异常——"+e.getMessage()+"-"+e.getLocalizedMessage());
//            return R.fail("取违法记录案件开始异常——"+e.getMessage()+"-"+e.getLocalizedMessage());
        }finally {

            return ReturnT.SUCCESS;
        }
    }

    private R fetchIllegalCaseRecordsReal(String startTime,Integer resumeCount,long  originTime) {
        if(StringUtil.isNotBlank(startTime)){
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startTime);
            }catch (Exception e){
                log.error(startTime+"解析错误，正确格式："+"yyyy-MM-dd HH:mm:ss.S");
                return R.fail(startTime+"解析错误，正确格式："+"yyyy-MM-dd HH:mm:ss.S");
            }
        }
        //      从数据库查询最大的入库时间
        log.info("取违法记录案件开始========================");
//       封装条件，请求数据
//       截止时间，当前查询时间
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
//       统计上一个小时，防止时差
        instance.add(Calendar.HOUR,-1);

        String url =access_url+"?appKey="+"b358af5f90434aa287b1c0c815f7c733";

//      从中断记录开始跑
        Map<String, InterruptCaseRecord> interruptCaseRecords = interruptRecordGet();
        if(interruptCaseRecords != null){
            for(String wfxm : interruptCaseRecords.keySet()){
                Object item1 = interruptCaseRecords.get(wfxm);
                InterruptCaseRecord item = new InterruptCaseRecord();
                BeanUtils.copyProperties(item1,item);
//              清理中断记录
                interruptRecordClear(wfxm);
                List<IllegalCaseRecord> list = this.getPages(url,item.getWfxw(),item.getQueryTime(),item.getNowTime(),item.getPage() );
                if(CollectionUtil.isNotEmpty(list)){
                    for (IllegalCaseRecord each: list) {
                        try {
                            this.save(each);
                        }catch (Exception e){
                            log.error("存违法记录案件错误："+e.getMessage());
                        }
                    }
                }
            }
        }else{
//          只有中断处理完了才能新查询，否则陷入死循环
            String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(instance.getTime());
            List<IllegalCaseWfxwEnum> eumValueList = IllegalCaseWfxwEnum.getEumValueList();
            for (IllegalCaseWfxwEnum item: eumValueList) {
                String queryTimeType = bladeRedis.hGet(last_update_time_key,item.getValue());
                if(StringUtil.isBlank(queryTimeType)){
                    queryTimeType ="2022-01-01 00:00:00.0";
                }
                if(StringUtil.isNotBlank(startTime)){
                    queryTimeType = startTime;
                }
                List<IllegalCaseRecord> list = this.getPages(url,item.getValue(),queryTimeType,nowTime,1 );
                if(CollectionUtil.isNotEmpty(list)){
                    for (IllegalCaseRecord each: list) {
                        try {
                            this.save(each);
                        }catch (Exception e){
                            log.error("存违法记录案件错误："+e.getMessage());
                        }
                    }
                }
                //      如果全部保存成功
                String maxStorageTime = iIllegalCaseRecordService.getMaxStorageTime(item.getValue());
                if(StringUtil.isBlank(maxStorageTime)){
                    bladeRedis.hDel(last_update_time_key,item.getValue());
                }else{
                    bladeRedis.hSet(last_update_time_key,item.getValue(),maxStorageTime);
                }

            }
        }

//      查看是否有中断记录
        Map<String, InterruptCaseRecord> map = interruptRecordGet();
        if(map != null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Long cha = (System.currentTimeMillis() - originTime)/1000;
            log.error("获取案件记录中断，尝试恢复....第"+resumeCount+"次,恢复时间"+(cha));
            if(cha < 60*60){
                resumeCount++;
                this.fetchIllegalCaseRecordsReal(null,resumeCount,originTime);
            }else{
                log.info("尝试恢复未完成，时间超限，等待下次恢复" );
            }
        }
        return R.success("ok");

    }

    private void save(IllegalCaseRecord each) {
        QueryWrapper<IllegalCaseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("for_id", each.getForId());
        queryWrapper.eq("storage_time",each.getStorageTime());
        int count = iIllegalCaseRecordService.count(queryWrapper);
        if(count <=0){
            iIllegalCaseRecordService.save(each);
        }else{
            log.error("违法记录案件："+each.getForId()+"-"+each.getStorageTime()+"已存在");
        }
    }

    private List<IllegalCaseRecord> getPages(String baseUrl ,String wfxw,String queryTime ,String nowTime,int startPage ) {
        String url= baseUrl+"&DB_TIME>"+queryTime+"&DB_TIME<"+nowTime+ "&WFXW="+ wfxw ;
        int page = startPage;
        List<IllegalCaseRecord> objects = new ArrayList<>();
        while (true){
            String pageUrl = url + "&page="+page;
            log.info("违法案件记录请求url:"+pageUrl);
            String s = null;
            try {
                s = HttpClientUtil.get(pageUrl, null);
//                log.info("违法案件记录返回信息:"+s);
            }catch (Exception e){
                e.printStackTrace();
                log.error("违法案件请求异常:"+e.getMessage());
//              中断记录
                this.interruptRecord(wfxw,page,queryTime,nowTime);
            }
            try {
                if(s == null){
                    break;
                }
                Map result = JSON.parseObject(s, Map.class);
                if(result == null){
                    break;
                }
                if(result.get("status")!=null && result.get("status").toString().equals("200")){
                    JSONArray data = (JSONArray) result.get("data");
                    if(data == null || data.size()<=0){
                        break;
                    }
                    List<IllegalCaseRecord> parse = this.fromJSONArray(data);
                    objects.addAll(parse);
                    page =page+1;
                    continue;
                }else{
//          失败处理
                    log.error("取违法记录案件-返回错误信息："+result.get("errorMsg"));
                    break;
                }
            }catch (Exception e){
                log.error("取违法记录案件-遇到错误信息："+e.getMessage()+"-"+e.getLocalizedMessage());
                e.printStackTrace();
                break;
            }
        }
        return objects;
    }

    private void interruptRecord(String wfxw, int page, String queryTime, String nowTime) {
        InterruptCaseRecord caseRecord = new InterruptCaseRecord();
        caseRecord.setWfxw(wfxw);
        caseRecord.setPage(page);
        caseRecord.setQueryTime(queryTime);
        caseRecord.setNowTime(nowTime);
        log.error("存储中断记录"+caseRecord);
        bladeRedis.hSet(illegalCaseTask_interrupt,wfxw,caseRecord);
    }

    private Map<String,InterruptCaseRecord> interruptRecordGet() {
        Map<String,InterruptCaseRecord> map = bladeRedis.hGetAll(illegalCaseTask_interrupt);
        if(map ==null || map.size() <=0){
           return null;
        }else{
            return map;
        }
    }

    private void interruptRecordClear(String wfxm) {
         bladeRedis.hDel(illegalCaseTask_interrupt,wfxm);
    }

    private List<IllegalCaseRecord> fromJSONArray(JSONArray data) {
        List<IllegalCaseRecord> objects = new ArrayList<>();
        for(int i=0;i<data.size();i++){
            Map item = (Map) data.get(i);
            IllegalCaseRecord record = new IllegalCaseRecord();
            if(item.get("ID")!=null){
                record.setForId( (String) item.get("ID"));
            }
            if(item.get("SFZMHM")!=null) {record.setIdProof((String) item.get("SFZMHM"));}
            if(item.get("JDSBH")!=null) {record.setPublishBookNumber((String) item.get("JDSBH"));}
            if(item.get("XM")!=null) {record.setName((String) item.get("XM"));}
            if(item.get("HPHM")!=null) {record.setCarNo((String) item.get("HPHM"));;}
            if(item.get("HPZL")!=null) {record.setCarNoType((String) item.get("HPZL"));}
            if(item.get("WFXW")!=null) {record.setIllegalAct((String) item.get("WFXW"));}
            if(item.get("WFXW_CN")!=null) {record.setIllegalActDesc((String) item.get("WFXW_CN"));}
            if(item.get("WFDZ")!=null) {record.setIllegalActAddress((String) item.get("WFDZ"));}
            if(item.get("XZQH")!=null) {record.setArea((String) item.get("XZQH"));}
            if(item.get("CLZD")!=null) {record.setTotalWeight((String) item.get("CLZD"));}
            if(item.get("WFSJ")!=null){ record.setCheckTime((String) item.get("WFSJ"));}
            if(item.get("CLSJ")!=null) {record.setPublishTime((String) item.get("CLSJ"));}
            if(item.get("WFJFS")!=null) {record.setIllegalScore((String) item.get("WFJFS"));}
            if(item.get("DB_TIME")!=null) {record.setStorageTime((String) item.get("DB_TIME"));}
            objects.add(record);
        }
        return objects;
    }


}
