package top.anets.modules.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.anets.modules.excel.entity.GetDataByDynamicInputDto;
import top.anets.modules.excel.entity.MsgClient;
import top.anets.modules.excel.entity.MsgClientGroup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ftm
 * @date 2022/9/16 0016 15:10
 */
@RestController
@Api(tags = "POI大数量导出")
@RequestMapping(value = "/bigExcel")
@Slf4j
public class BigExcelController {


    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(HttpServletResponse response, HttpServletRequest request) throws Exception {

        Workbook workbook = null;
        Date start = new Date();
        ExportParams params = new ExportParams("大数据测试", "测试");

        IExcelExportServer iExcelExportServer = new IExcelExportServer() {

            //这个方法会被循环调用 page每次+1
            @SneakyThrows
            @Override
            public List<Object> selectListForExcelExport(Object queryParams, int page) {
                log.info("当前查询第{}页数据", page);
                if (queryParams instanceof GetDataByDynamicInputDto) {
                    GetDataByDynamicInputDto params = (GetDataByDynamicInputDto) queryParams;
                    params.setPageNum(page++);
                    return (List<Object>) getPageData(params);
                }
                return null;
            }
        };
        GetDataByDynamicInputDto getDataByDynamicInputDto = new GetDataByDynamicInputDto();
        workbook = ExcelExportUtil.exportBigExcel(params, MsgClient.class, iExcelExportServer, getDataByDynamicInputDto);
        downLoadExcel("test" + System.currentTimeMillis() + ".xlsx", response, workbook);
        System.out.println(new Date().getTime() - start.getTime());
        workbook.close();
    }



    private Object getPageData(GetDataByDynamicInputDto params) {
        List<MsgClient> list = new ArrayList<MsgClient>();
        int pageNum = params.getPageNum();
        int pageSize = params.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        //查询页数限制
        if (pageNum > 200) {//每页1000条
            return list;
        }
        for (int i = offset; i < offset + pageSize; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            MsgClientGroup group = new MsgClientGroup();
            group.setGroupName("测试" + i);
            client.setGroup(group);
            list.add(client);
        }
        return list;
    }



    /**
     * Excel大文件导入功能
     */
//    @RequestMapping("ExcelDoctorImportBig")
//    public Result importsBig(MultipartFile file) throws IOException {
//        if(file == null){
//            throw new RuntimeException("没有选择文件");
//        }
//        long start = System.currentTimeMillis();
//        ImportParams params = new ImportParams();
//        //标题所在行数
//        params.setTitleRows(0);
//        //表头所在行数
//        params.setHeadRows(1);
//        List<DoctorExcel> doctorExcels = new ArrayList<>();
//        HashMap<String,DoctorExcel> hashMap = new HashMap<>();
//        //使用api获取到的List数据
//        ExcelImportUtil.importExcelBySax(file.getInputStream(), DoctorExcel.class, params, new IReadHandler() {
//            @Override
//            public void handler(Object o) {
//                DoctorExcel enterprisesEntity = (DoctorExcel) o;
//                if(enterprisesEntity == null){
//                    return;
//                }
//                if(StringUtils.isNotBlank(enterprisesEntity.getUserMobile())){
////                    doctorExcels.add( enterprisesEntity);
//                    hashMap.put(enterprisesEntity.getUserMobile(), enterprisesEntity);
//                }
//            }
//
//            @Override
//            public void doAfterAll() {
//            }
//        });
//        Collection<DoctorExcel> values = hashMap.values();
//        if(CollectionUtils.isNotEmpty(values)){
//            doctorExcels = Lists.newArrayList(values);
//        }
//        boolean a = false;
//        Integer integer = 0;
//        if (!doctorExcels.isEmpty()) {
//            List<List<DoctorExcel>> lists = ListUtil.pagingList(doctorExcels, 1000);
//            lists.forEach(item->{
//                ThreadPoolUtils.execute(new Runnable(){
//                    @Override
//                    public void run() {
//                        Integer integer = tUserinfoService.importDoctorSave(item);
//                    }
//                });
//            });
//        }
//        long end = System.currentTimeMillis();
//
//        return Result.success(a);
//    }


    private void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try (OutputStream out = response.getOutputStream();) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("content-Type", "application/vnd.ms-excel");
            workbook.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
