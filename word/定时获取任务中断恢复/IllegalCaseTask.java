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

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private static   String access_url =" ";
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
            SysParam sysJobParam = sysParamService.getByCategoryCode("external_application", "18");
            String name = sysJobParam.getName();
            if(StringUtil.isNotBlank(name)){
                access_url = name;
                log.info("??????????????????URL???"+name);
            }
            long now =  System.currentTimeMillis();
            this.fetchIllegalCaseRecordsReal(startTime,1,now);
            bladeRedis.del(illegalCaseTask_lock);
        } catch (Exception e) {
            bladeRedis.del(illegalCaseTask_lock);
            e.printStackTrace();
            log.error("???????????????????????????????????????"+e.getMessage()+"-"+e.getLocalizedMessage());
//            return R.fail("???????????????????????????????????????"+e.getMessage()+"-"+e.getLocalizedMessage());
        }finally {
            return ReturnT.SUCCESS;
        }
    }

    private R fetchIllegalCaseRecordsReal(String startTime,Integer resumeCount,long  originTime) {
        if(StringUtil.isNotBlank(startTime)){
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startTime);
            }catch (Exception e){
                log.error(startTime+"??????????????????????????????"+"yyyy-MM-dd HH:mm:ss.S");
                return R.fail(startTime+"??????????????????????????????"+"yyyy-MM-dd HH:mm:ss.S");
            }
        }
        //      ???????????????????????????????????????
        log.info("???????????????????????????========================");
//       ???????????????????????????
//       ?????????????????????????????????
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
//       ????????????????????????????????????
        instance.add(Calendar.HOUR,-1);

        String url =access_url+"?appKey="+"b358af5f90434aa287b1c0c815f7c733";
//      ????????????????????????
        Map<String, InterruptCaseRecord> interruptCaseRecords = interruptRecordGet();
        if(interruptCaseRecords != null){
            for(String wfxm : interruptCaseRecords.keySet()){
                Object item1 = interruptCaseRecords.get(wfxm);
                InterruptCaseRecord item = new InterruptCaseRecord();
                BeanUtils.copyProperties(item1,item);
//              ??????????????????
                interruptRecordClear(wfxm);
                List<IllegalCaseRecord> list = this.getPages(url,item.getWfxw(),item.getQueryTime(),item.getNowTime(),item.getPage() ,true);
                if(CollectionUtil.isNotEmpty(list)){
                    for (IllegalCaseRecord each: list) {
                        try {
                            this.save(each);
                        }catch (Exception e){
                            log.error("??????????????????????????????"+e.getMessage());
                        }
                    }
                }
            }
        }else{
//          ???????????????????????????????????????????????????????????????
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
                List<IllegalCaseRecord> list = this.getPages(url,item.getValue(),queryTimeType,nowTime,1,false );
                if(CollectionUtil.isNotEmpty(list)){
                    for (IllegalCaseRecord each: list) {
                        try {
                            this.save(each);
                        }catch (Exception e){
                            log.error("??????????????????????????????"+e.getMessage());
                        }
                    }
                }
                //      ????????????????????????
                String maxStorageTime = iIllegalCaseRecordService.getMaxStorageTime(item.getValue());
                if(StringUtil.isBlank(maxStorageTime)){
                    bladeRedis.hDel(last_update_time_key,item.getValue());
                }else{
                    bladeRedis.hSet(last_update_time_key,item.getValue(),maxStorageTime);
                }

            }
        }

//      ???????????????????????????
        Map<String, InterruptCaseRecord> map = interruptRecordGet();
        if(map != null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Long cha = (System.currentTimeMillis() - originTime)/1000;
            log.error("???????????????????????????????????????....???"+resumeCount+"???,????????????"+(cha));
            if(cha < 24*60*60){
                resumeCount++;
                this.fetchIllegalCaseRecordsReal(null,resumeCount,originTime);
            }else{
                log.info("?????????????????????????????????????????????????????????" );
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
            log.error("?????????????????????"+each.getForId()+"-"+each.getStorageTime()+"?????????");
        }
    }




    private List<IllegalCaseRecord> getPages(String baseUrl ,String wfxw,String queryTime ,String nowTime,int startPage ,boolean isRetry) {
        String url= baseUrl+"&DB_TIME>"+queryTime+"&DB_TIME<"+nowTime+ "&WFXW="+ wfxw ;
        int page = startPage;
        List<IllegalCaseRecord> objects = new ArrayList<>();
        Integer total = null;
        Integer size = null;
        Integer pages = null;
        while (true){
//          ????????????
            if(total !=null && size != null){
                pages =  (int) Math.ceil(BigDecimal.valueOf(total).divide(BigDecimal.valueOf(size),2, RoundingMode.HALF_UP).doubleValue());
            }
            String pageUrl = url + "&page="+page;
            log.info("????????????????????????url:"+pageUrl);
            String s = null;
            try {
                s = HttpClientUtil.get(pageUrl, null);
//                log.info("??????????????????????????????:"+s);
            }catch (Exception e){
                e.printStackTrace();
                log.error("????????????????????????:"+e.getMessage());
            }
            try {
                if(s == null){
                    //  ????????????
                    if(!(pages != null && page > pages)){
                        this.interruptRecord(wfxw,page,queryTime,nowTime);
                    }
                    break;
                }
                Map result = JSON.parseObject(s, Map.class);
                if(result == null){
                    //  ????????????
                    if(!(pages != null && page > pages)){
                        this.interruptRecord(wfxw,page,queryTime,nowTime);
                    }
                    break;
                }
                if(result.get("status")!=null && result.get("status").toString().equals("200")){
//                  ???????????????
                    total = (Integer) result.get("totalCount");
                    size = (Integer) result.get("pageSize");
                    JSONArray data = (JSONArray) result.get("data");
                    if(data == null || data.size()<=0){
                        //  ????????????
                        break;
                    }
                    List<IllegalCaseRecord> parse = this.fromJSONArray(data,page,size,total,isRetry);
                    objects.addAll(parse);
                    page =page+1;
                    continue;
                }else{
//          ????????????
                    //  ????????????
                    if(!(pages != null && page > pages)){
                        this.interruptRecord(wfxw,page,queryTime,nowTime);
                    }
                    log.error("?????????????????????-?????????????????????"+result.get("errorMsg"));
                    break;
                }
            }catch (Exception e){
                log.error("?????????????????????-?????????????????????"+e.getMessage()+"-"+e.getLocalizedMessage());
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
        log.error("??????????????????"+caseRecord);
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

    private List<IllegalCaseRecord> fromJSONArray(JSONArray data,Integer page,Integer size,Integer total,boolean isRetry) {
        List<IllegalCaseRecord> objects = new ArrayList<>();
        for(int i=0;i<data.size();i++){
            


//          ??????????????????????????????
            if(page!=null&&size!=null&&total!=null){
                int pointer = (page - 1) * size + (i + 1);
                String remark= pointer+"/"+total;
                if(isRetry){
                    remark+= "(??????????????????"+page+")";
                }
                record.setRemark(remark);
            }
            objects.add(record);
        }
        return objects;
    }


}
