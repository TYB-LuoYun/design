package top.anets.modules.excel.bigSpeed;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ftm
 * @date 2022/9/26 0026 12:00
 */
public class ExcelUtil {
    /**
     * 导出Excel
     * @param result
     * @param headArray 表头数组（有序）
     * @param dataArray 字段数组（有序）
     * @return
     */
    public static Workbook exportExcel(List<Map<String,Object>> result, String[] headArray, String[] dataArray){

        //表示SXSSFWorkbook只会保留100条数据在内存中，避免内存溢出
        Workbook wb = new SXSSFWorkbook(1000);

        //1页面
        Sheet sheet = wb.createSheet("Sheet1");
        //2行标题
        Row row = sheet.createRow(0);
        Cell cell = null;
        for (int i = 0; i < headArray.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headArray[i]);
        }

        Map<String,Object> map = null;
        for (int j = 0; j < result.size(); j++) {
            map = result.get(j);
            row = sheet.createRow(j + 1);

            for(int k = 0; k < headArray.length; k++ ){
                cell = row.createCell(k);
                cell.setCellValue(map.get(dataArray[k]) + "");

            }
        }

        return wb;
    }

    /**
     * 读取Excel文件（List）
     * @param is 输入流
     * @param flag 表头字段（中文）
     * @return
     * @throws Exception
     */
    public static List<List<String>> readExcelAsList(InputStream is, String[] flag) throws Exception{
        Workbook wb = WorkbookFactory.create(is);
        List<List<String>> result = new ArrayList<List<String>>();
        //对excel表的各个sheet进行遍历
        for(int numSheet = 0;numSheet<wb.getNumberOfSheets();numSheet++){
            Sheet Sheet = wb.getSheetAt(numSheet);
            if(Sheet==null){
                continue;
            }

            Row row = Sheet.getRow(0);
            if(null == row){
                continue;
            }
            int min = row.getFirstCellNum();
            int max = row.getLastCellNum();
            int[] no = new int [flag.length];
            for(int n = 0; n < flag.length; n++){
                no[n] = -1;
            }
            for(int i = min; i < max; i++){
                Cell cell = row.getCell(i);

                for(int a = 0; a < flag.length; a++){
                    if(cell.getStringCellValue().equals(flag[a]))
                        no[a]=i;
                }
            }

            for(int rowNum = 1; rowNum <= Sheet.getLastRowNum(); rowNum++){
                //判断一行数据是否为空
                int isBlank = 0;
                row = Sheet.getRow(rowNum);
                if (row != null){
                    List<String> rowList = new ArrayList<String>();
                    for(int a = 0; a < flag.length; a++){
                        if(no[a] != -1){
                            Cell cell = row.getCell(no[a]);
                            String cellValue = "";
                            if (null != cell) {
                                // 以下是判断数据的类型
                                switch (cell.getCellTypeEnum()) {
                                    case NUMERIC: // 数字
                                        if (HSSFDateUtil.isCellDateFormatted(cell)){
                                            Date d = cell.getDateCellValue();
                                            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                            cellValue = formater.format(d);
                                        }else {
                                            DecimalFormat df = new DecimalFormat("0");
                                            cellValue = df.format(cell.getNumericCellValue());
                                        }
                                        break;
                                    case STRING: // 字符串
                                        cellValue = cell.getStringCellValue();
                                        break;
                                    case BOOLEAN: // Boolean
                                        cellValue = cell.getBooleanCellValue() + "";
                                        break;
                                    case FORMULA: // 公式
                                        cellValue = cell.getCellFormula() + "";
                                        break;
                                    case BLANK: // 空值
                                        cellValue = "";
                                        break;
                                    case ERROR: // 故障
                                        cellValue = "非法字符";
                                        break;
                                    default:
                                        cellValue = "未知类型";
                                        break;
                                }
                            }
                            if (StringUtils.isBlank(cellValue)){
                                isBlank++;
                            }
                            rowList.add(cellValue);
                        }
                    }
                    if (isBlank != flag.length){
                        result.add(rowList);
                    }
                }

            }
        }
        return result;
    }


    /**
     * 读取Excel文件（Map）
     * @param is 输入流
     * @param flag 表头字段（中文）
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> readExcelAsMap(InputStream is, String[] flag) throws Exception{
        Workbook XssfWorkbook = WorkbookFactory.create(is);
        List<Map<String,Object>> result = new ArrayList<>();
        //对excel表的各个sheet进行遍历
        for(int numSheet = 0;numSheet<XssfWorkbook.getNumberOfSheets();numSheet++){
            Sheet XssfSheet = XssfWorkbook.getSheetAt(numSheet);
            if(XssfSheet==null){
                continue;
            }

            Row row = XssfSheet.getRow(0);
            if(row == null){
                continue;
            }
            int min = row.getFirstCellNum();
            int max = row.getLastCellNum();
            int[] no = new int [flag.length];
            for(int n = 0; n < flag.length; n++){
                no[n]=-1;
            }
            for(int i = min; i < max; i++){
                Cell cell = row.getCell(i);
                if(cell.getStringCellValue().equals("") || cell.getStringCellValue() == null){
                    max--;
                }

                for(int a = 0; a < flag.length; a++){
                    if(cell.getStringCellValue().equals(flag[a]))
                        no[a]=i;
                }
            }

            for(int rowNum = 1; rowNum <= XssfSheet.getLastRowNum(); rowNum++){
                row = XssfSheet.getRow(rowNum);

                Map<String,Object> rowMap = new HashMap<>();

                for(int a = 0; a < flag.length; a++){
                    if(no[a] != -1){
                        Cell cell = row.getCell(no[a]);
                        if(cell == null){
                            rowMap.put(flag[a],null);
                            continue;
                        }
                        String cellValue = "";
                        if (null != cell) {
                            // 以下是判断数据的类型
                            switch (cell.getCellTypeEnum()) {
                                case NUMERIC: // 数字
                                    if (HSSFDateUtil.isCellDateFormatted(cell)){
                                        Date d = cell.getDateCellValue();
                                        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                        cellValue = formater.format(d);
                                    }else {
                                        DecimalFormat df = new DecimalFormat("0");
                                        cellValue = df.format(cell.getNumericCellValue());
                                    }
                                    break;
                                case STRING: // 字符串
                                    cellValue = cell.getStringCellValue();
                                    break;
                                case BOOLEAN: // Boolean
                                    cellValue = cell.getBooleanCellValue() + "";
                                    break;
                                case FORMULA: // 公式
                                    cellValue = cell.getCellFormula() + "";
                                    break;
                                case BLANK: // 空值
                                    cellValue = "";
                                    break;
                                case ERROR: // 故障
                                    cellValue = "非法字符";
                                    break;
                                default:
                                    cellValue = "未知类型";
                                    break;
                            }
                        }

                        rowMap.put(flag[a], cellValue);

                    }
                }
                result.add(rowMap);
            }

        }

        return result;
    }
}
