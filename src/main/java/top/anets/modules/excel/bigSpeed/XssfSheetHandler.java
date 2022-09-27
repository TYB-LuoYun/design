//package top.anets.modules.excel.bigSpeed;
//
///**
// * @author ftm
// * @date 2022/9/26 0026 10:01
// */
//import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.xssf.eventusermodel.XSSFReader;
//import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
//import org.apache.poi.xssf.model.SharedStringsTable;
//import org.apache.poi.xssf.model.StylesTable;
//import org.xml.sax.InputSource;
//import org.xml.sax.XMLReader;
//import org.xml.sax.helpers.XMLReaderFactory;
//
//import java.io.InputStream;
//import java.util.function.Consumer;
//
///**
// * @Author: LiHuaZhi
// * @Date: 2022/1/16 14:57
// * @Description:
// **/
//public class XssfSheetHandler {
//    /**
//     * @param inputStream 数据流
//     * @param consumer    自定义回调
//     * @param entity      解析数据实体
//     * @param batchNum    批处理数量
//     * @param startRow    excel解析正文行号
//     * @throws Exception
//     */
//    public static void handlerData(InputStream inputStream, Consumer consumer, Class<?> entity, Integer batchNum, Integer startRow) throws Exception {
//        //1.根据excel报表获取OPCPackage
//        OPCPackage opcPackage = OPCPackage.open(inputStream);
//        //2.创建XSSFReader
//        XSSFReader reader = new XSSFReader(opcPackage);
//        //3.获取SharedStringTable对象
//        SharedStringsTable table = reader.getSharedStringsTable();
//        //4.获取styleTable对象
//        StylesTable stylesTable = reader.getStylesTable();
//        //5.创建Sax的xmlReader对象
//        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
//
//        XSSFSheetXMLHandler xmlHandler = new XSSFSheetXMLHandler(stylesTable, table, new SheetHandler(batchNum, startRow, consumer, entity), false);
//        xmlReader.setContentHandler(xmlHandler);
//        //7.逐行读取
//        XSSFReader.SheetIterator sheetIterator = (XSSFReader.SheetIterator) reader.getSheetsData();
//        while (sheetIterator.hasNext()) {
//            //每一个sheet的流数据
//            InputStream stream = sheetIterator.next();
//            InputSource is = new InputSource(stream);
//            xmlReader.parse(is);
//        }
//    }
//}
