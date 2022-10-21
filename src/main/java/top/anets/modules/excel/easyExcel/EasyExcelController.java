package top.anets.modules.excel.easyExcel;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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


    /**
     * 导出
     */
    @RequestMapping("testEasyExcel")
    void export(HttpServletResponse response) {
        try {
            List list =new ArrayList();
            String fileName = "名字";
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename=" +  URLEncoder.encode(fileName,"UTF-8") + ".xlsx");


            EasyExcel.write(response.getOutputStream(),EasyExcelTest.class)
                    .sheet("sheet1")
                    .doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多sheet导出
     */
    void exportSheets(HttpServletResponse response) throws IOException {
        String fileName = "名字";
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-disposition", "attachment;filename=" +  URLEncoder.encode(fileName,"UTF-8") + ".xlsx");


        List list =new ArrayList();
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        WriteSheet studentSheet =
                EasyExcel.writerSheet(0, "学生信息").head(EasyExcelTest.class).build();
        WriteSheet grateSheet =
                EasyExcel.writerSheet(1, "班级信息").head(EasyExcelTest.class).build();
        excelWriter.write(list, studentSheet)
                .write(list, grateSheet);
        excelWriter.finish();
    }


    /**
     * 多线程-多sheet导出
     */
    @RequestMapping("exports")
    public void exports(HttpServletResponse response) throws  Exception {

//      一次查询的数据量，此处设置为10000条
        Integer ROW_SIZE = 10000;
//        一个页签多少次查询，此处设置为10次；
        Integer ROW_PAGE = 10;
//       每个sheet 最大存数据量
        Integer rowCountOfSheet = ROW_SIZE*ROW_PAGE;

//       从数据库看下count
        Integer rowCount = 100000;
//       计算sheet数量
        int sheetCount = (rowCount/ rowCountOfSheet)+1;

        /**
         * 计数器-可以等待线程完成
         */
        CountDownLatch threadSignal = new CountDownLatch(sheetCount);
        String fileName ="测试";


        String[] paths = new String[sheetCount];
         InputStream[] ins = new ByteArrayInputStream[sheetCount];

        for(int i=0;i<sheetCount;i++) {
            int finalI = i;
            int finalRowCount = rowCount;



//           对于每个sheet,多线程处理
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    // excel文件流
                    ByteArrayOutputStream singleOutputStream = new ByteArrayOutputStream();
                    ExcelWriter excelWriter = EasyExcel.write(singleOutputStream).build();
                    // 单sheet页写入数(一个sheet需要多少次查询， 若是最后一页则是 ；否则就是一个页面10次查询)
                    int sheetThreadCount = finalI == (sheetCount-1) ? (finalRowCount - finalI *rowCountOfSheet)/ROW_SIZE+1 : ROW_PAGE;
                    CountDownLatch sheetThreadSignal = new CountDownLatch(sheetThreadCount);
                    for(int j=0;j<sheetThreadCount;j++) {
//                      对于一个sheet的每次查询来说
//                      当前页
                        int page = finalI *ROW_PAGE + j + 1;
//                      当前页大小,注意最后一页数据
                        int pageSize = j==(sheetThreadCount-1)&& finalI ==(sheetCount-1) ? finalRowCount %ROW_SIZE : ROW_SIZE;
//                      异步去
                        int finalJ = j;
                        ThreadPoolUtils.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
//                                  根据page, pageSize查询数据
                                    List list = new ArrayList();
                                    writeSheetOfExcel(list, finalJ,"sheet" + finalJ , excelWriter, EasyExcelTest.class);
                                    sheetThreadSignal.countDown();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    try {
                        // 等待当前sheet写完
                        sheetThreadSignal.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 当前sheet写完后关闭写入流
                    excelWriter.finish();
                    paths[finalI] = (finalI +1) + "-" + fileName + ".xlsx";
                    ins[finalI] = new ByteArrayInputStream(singleOutputStream.toByteArray());
                    // 单文件
                    if (sheetCount == 1){
                        // xlsx
                        // 将sign存入redis并设置过期时间
                    }
                    threadSignal.countDown();
                }
            });


        }


//      等待所有的sheet页写入完毕
        threadSignal.await();

        if (sheetCount != 1){
            ZipUtil.zip(response.getOutputStream() , paths, ins);
        }


    }

    /**
     * 往excel里写sheet
     * @param sheetlists
     * @param sheetName
     * @param excelWriter
     * @param clazz
     */
    private void writeSheetOfExcel(List sheetlists ,Integer sheetIndex,String sheetName ,ExcelWriter excelWriter,Class clazz){
        //数据查询
        synchronized (excelWriter) {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetIndex, sheetName)
                    // 这里放入动态头
                    .head(clazz)
                    // 当然这里数据也可以用 List<List<String>> 去传入
                    .build();
            excelWriter.write(sheetlists, writeSheet);
        }
    }


}
