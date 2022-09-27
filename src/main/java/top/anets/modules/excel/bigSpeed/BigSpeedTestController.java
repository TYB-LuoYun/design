package top.anets.modules.excel.bigSpeed;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author ftm
 * @date 2022/9/26 0026 9:55
 */
@RestController
@RequestMapping("excel")
public class BigSpeedTestController {
    /**
     * 数据导入 - 百万数据导入 - 大约20S，平均50000条/秒，当数据千条后使用会有更好的效果
     *
     * @param file
     * @return
     */
//    @PostMapping("upload/bw")
//    @ResponseBody
//    public String uploadBaiWan(@RequestParam("file") MultipartFile file) throws Exception {
//        long start = System.currentTimeMillis();
//        InputStream inputStream = file.getInputStream();
//        // 注册事件处理器
//        Consumer<List<User>> consumer =
//                uploadData -> importData(uploadData);
//        XssfSheetHandler.handlerData(inputStream, consumer, User.class, null, 1);
//        long kill = System.currentTimeMillis() - start;
//        System.out.println("耗时："+kill+"ms");
//
//
//        return "耗时："+kill+"ms";
//    }

    // 接收Handler回传的数据
    public void importData(List<User> list) {
        System.out.println("解析数量：" + list.size());
    }


    /**
     * 官方自带的大文件导入功能
     */

    @PostMapping("upload/origin")
    @ResponseBody
    public String uploadOrgin(@RequestParam("file") MultipartFile file) throws Exception {
        List<User2> doctorExcels = new ArrayList<>();
        ImportParams params = new ImportParams();
        //标题所在行数
        params.setTitleRows(0);
        //表头所在行数
        params.setHeadRows(1);
        long start = System.currentTimeMillis();
        ExcelImportUtil.importExcelBySax(file.getInputStream(), User2.class, params, new IReadHandler() {
            @Override
            public void handler(Object o) {
                User2 enterprisesEntity = (User2) o;
                if (enterprisesEntity == null) {
                    return;
                }
                doctorExcels.add(enterprisesEntity);

            }

            @Override
            public void doAfterAll() {
            }
        });
        long kill = System.currentTimeMillis() - start;
        System.out.println("耗时："+kill+"ms");


        return "耗时："+kill+"ms";
    }



    @PostMapping("upload/origin2")
    @ResponseBody
    public String uploadOrgin2(@RequestParam("file") MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        InputStream is = file.getInputStream();
        String[] flag = {"姓名","年龄","性别","住址","身高"};
        List<List<String>> resultList = ExcelUtil.readExcelAsList(is, flag);
//        System.out.println("resultList: " + resultList);
        long kill = System.currentTimeMillis() - start;
        System.out.println("耗时："+kill+"ms");


        return "耗时："+kill+"ms";
    }
}
