package top.anets.modules.excel.easyExcel;

import com.alibaba.excel.EasyExcel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ftm
 * @date 2022/9/26 0026 12:59
 */
@RestController
@RequestMapping("easyExcel")
public class EasyExcelController {
    @PostMapping("testEasyExcel")
    @ResponseBody
    String testEasyExcel(@RequestParam("file") MultipartFile excel) throws IOException {
        //这个EasyExcelListener里已经写入了监听保存的操作,所以外面不需要自己再处理了
        // 这里默认读取第一个sheet
        long start = System.currentTimeMillis();
        EasyExcel.read(excel.getInputStream(), EasyExcelTest.class, new EasyExcelListener()).sheet().doRead();
        long kill = System.currentTimeMillis() - start;
        System.out.println("耗时："+kill+"ms");
        return "耗时："+kill+"ms";
    }


    /**
     * 多行头
     *
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>3. 设置headRowNumber参数，然后读。 这里要注意headRowNumber如果不指定， 会根据你传入的class的{@link ExcelProperty#value()}里面的表头的数量来决定行数，
     * 如果不传入class则默认为1.当然你指定了headRowNumber不管是否传入class都是以你传入的为准。
     */
//    @Test
//    public void complexHeaderRead() {
//        String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
//        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
//        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet()
//                // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
//                .headRowNumber(1).doRead();
//    }
}
