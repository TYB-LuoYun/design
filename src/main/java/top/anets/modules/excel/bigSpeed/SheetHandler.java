//package top.anets.modules.excel.bigSpeed;
//
///**
// * @author ftm
// * @date 2022/9/26 0026 9:59
// */
//import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
//import org.apache.poi.xssf.usermodel.XSSFComment;
//
//import java.lang.reflect.Constructor;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
///**
// * 自定义的事件处理器
// * 处理每一行数据读取
// * 实现接口
// *
// * @author LiHuaZhi
// */
//public class SheetHandler<T> implements XSSFSheetXMLHandler.SheetContentsHandler {
//
//    /**
//     * 批量处理梳理
//     */
//    public Integer batchNum;
//
//    /**
//     * 开始解析的行号，第一行时，startRow为0
//     */
//    public Integer startRow;
//
//    /**
//     * 封装的entity对象
//     */
//    public Class<?> entity;
//
//    /**
//     * 当前解析的行号
//     */
//    private Integer currentRow;
//
//    /**
//     * 传入解析数据的service对象
//     */
//    public Consumer<List<T>> uploadService;
//
//    /**
//     * 临时解析对象的构造器
//     */
//    Constructor<?> constructor;
//    /**
//     * 接收解析对象值
//     */
//    public List<T> list = new ArrayList<>();
//
//    /**
//     * 解析头部数据
//     */
//    public List<String> headList = new ArrayList<>();
//    /**
//     * 解析单元格
//     */
//    public List<String> valueList = new ArrayList<>();
//
//    /**
//     * 解析的列号,默认0为第一列
//     */
//    private Integer cellNum = 0;
//
//    public SheetHandler(Integer batchNum, Integer startRow, Consumer<List<T>> uploadService, Class<?> entity) throws Exception {
//        constructor = entity.getDeclaredConstructor(new Class[]{List.class});
//        this.batchNum = batchNum == null ? 1000 : batchNum;
//        this.startRow = startRow;
//        this.uploadService = uploadService;
//        this.entity = entity;
//    }
//
//    /**
//     * 当开始解析某一行的时候触发
//     * i:行索引
//     */
//    @Override
//    public void startRow(int row) {
//        currentRow = row;
//    }
//
//    /**
//     * 当结束解析某一行的时候触发
//     * i:行索引
//     */
//    @Override
//    public void endRow(int row) {
//        try {
//            cellNum = 0;
//            if (headList.size() > 0) {
//                System.out.println("解析头部数据：" + headList);
//            }
//
//            if (valueList.size() > 0) {
//                System.out.println("解析表格数据：" + valueList);
//                T data = (T) constructor.newInstance(valueList);
//                list.add(data);
//            }
//
//            if (list.size() >= batchNum) {
//                // 回调接口，处理数据
//                saveData(list);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            headList.clear();
//            valueList.clear();
//        }
//    }
//
//    /**
//     * 对行中的每一个表格进行处理
//     * cellReference: 单元格名称
//     * value：数据
//     * xssfComment：批注
//     */
//    @Override
//    public void cell(String cellReference, String value, XSSFComment xssfComment) {
//        try {
//            if (currentRow < startRow) {
//                // 加载表头数据
//                headList.add(value);
//            } else {
//                // 获取表格数据
//                valueList.add(value);
//            }
//            cellNum++;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 解析完sheet，多个sheet会回调多次
//     */
//    @Override
//    public void endSheet() {
//        System.out.println("解析完成");
//        saveData(list);
//    }
//
//    private void saveData(List<T> dataList) {
//        if (dataList.size() > 0) {
//            uploadService.accept(dataList);
//            dataList.clear();
//        }
//    }
//}