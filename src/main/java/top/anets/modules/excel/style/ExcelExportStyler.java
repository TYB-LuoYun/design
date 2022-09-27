package top.anets.modules.excel.style;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

/**
 * @author ftm
 * @date 2022/9/16 0016 15:44
 */
class ExcelExportStatisticStyler extends ExcelExportStylerDefaultImpl {


    private CellStyle numberCellStyle;
    private CellStyle numberCellStyle1;

    public ExcelExportStatisticStyler(Workbook workbook) {
        super(workbook);
        createNumberCellStyler();
        createNumberCellStyler1();
    }
    private void createNumberCellStyler(){
        numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setAlignment(HorizontalAlignment.CENTER);
        numberCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        numberCellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0.00"));
        numberCellStyle.setWrapText(true);
//        numberCellStyle.setFont(getFont(workbook, (short)11, false));
//        // 背景色
//        numberCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        numberCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
    /**
     * 字体样式
     *
     * @param size   字体大小
     * @param isBold 是否加粗
     * @return
     */
    private Font getFont(Workbook workbook, short size, boolean isBold) {
        Font font = workbook.createFont();
        font.setFontName("宋体"); // 字体样式
        font.setBold(isBold);    // 是否加粗
        font.setFontHeightInPoints(size);   // 字体大小
        return font;
    }
    private void createNumberCellStyler1(){
        numberCellStyle1 = workbook.createCellStyle();
        numberCellStyle1.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        numberCellStyle1.setVerticalAlignment(VerticalAlignment.CENTER); // 上下居中
        numberCellStyle1.setDataFormat((short)BuiltinFormats.getBuiltinFormat("#,##0")); // 设置数据转换
        numberCellStyle1.setWrapText(true); // 设置自动换行

        numberCellStyle1.setBorderBottom(BorderStyle.THIN);   // 下边框
        numberCellStyle1.setBorderLeft(BorderStyle.THIN);     // 左边框
        numberCellStyle1.setBorderTop(BorderStyle.THIN);      // 上边框
        numberCellStyle1.setBorderRight(BorderStyle.THIN);    // 右边框

//        numberCellStyle1.setFont(getFont(workbook, (short)11, false));
//        // 背景色
//        numberCellStyle1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        numberCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
    /**
     * 数据行样式
     *
     * @param noneStyler 可以用来表示奇偶行
     * @param entity 数据内容
     * @return 样式
     */
    @Override
    public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity){
        numberCellStyle.setFont(getFont(workbook, (short)11, false));
        // 背景色
        numberCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        numberCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        numberCellStyle1.setFont(getFont(workbook, (short)11, false));
        // 背景色
        numberCellStyle1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        numberCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (entity != null
                && 10==entity.getType()) {
            if("type_1".equals(entity.getDict())){
                return numberCellStyle1;
            }
            return numberCellStyle;
        }
        return super.getStyles(noneStyler, entity);
    }

    /**
     * 获取样式方法
     *
     * @param dataRow 数据行
     * @param obj     对象
     * @param data    数据
     */
    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return super.getStyles(cell, dataRow, entity, obj, data);
    }
}



class ExcelExportStyler implements IExcelExportStyler {
    private static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");
    private static final short FONT_SIZE_TEN = 10;
    private static final short FONT_SIZE_ELEVEN = 11;
    private static final short FONT_SIZE_TWELVE = 12;
    /**
     * 大标题样式
     */
    private CellStyle headerStyle;
    /**
     * 每列标题样式
     */
    private CellStyle titleStyle;
    /**
     * 数据行样式
     */
    private CellStyle styles;

    public ExcelExportStyler(Workbook workbook) {
        this.init(workbook);
    }

    /**
     * 初始化样式
     *
     * @param workbook
     */
    private void init(Workbook workbook) {
        this.headerStyle = initHeaderStyle(workbook);
        this.titleStyle = initTitleStyle(workbook);
    }

    /**
     * 大标题样式
     *
     * @param color
     * @return
     */
    @Override
    public CellStyle getHeaderStyle(short color) {
        return headerStyle;
    }

    /**
     * 每列标题样式
     *
     * @param color
     * @return
     */
    @Override
    public CellStyle getTitleStyle(short color) {
        return titleStyle;
    }

    /**
     * 数据行样式
     *
     * @param parity 可以用来表示奇偶行
     * @param entity 数据内容
     * @return 样式
     */
    @Override
    public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
        return styles;
    }

    /**
     * 获取样式方法
     *
     * @param dataRow 数据行
     * @param obj     对象
     * @param data    数据
     */
    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return getStyles(true, entity);
    }

    /**
     * 模板使用的样式设置
     */
    @Override
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null;
    }

    /**
     * 初始化--大标题样式
     *
     * @param workbook
     * @return
     */
    private CellStyle initHeaderStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setFont(getFont(workbook, FONT_SIZE_TWELVE, true));
        return style;
    }

    /**
     * 初始化--每列标题样式
     *
     * @param workbook
     * @return
     */
    private CellStyle initTitleStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setFont(getFont(workbook, FONT_SIZE_ELEVEN, false));
        // 背景色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 基础样式
     *
     * @return
     */
    private CellStyle getBaseCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);   // 下边框
        style.setBorderLeft(BorderStyle.THIN);     // 左边框
        style.setBorderTop(BorderStyle.THIN);      // 上边框
        style.setBorderRight(BorderStyle.THIN);    // 右边框
        style.setAlignment(HorizontalAlignment.CENTER);         // 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);   // 上下居中
        style.setWrapText(true);    // 设置自动换行
        return style;
    }

    /**
     * 字体样式
     *
     * @param size   字体大小
     * @param isBold 是否加粗
     * @return
     */
    private Font getFont(Workbook workbook, short size, boolean isBold) {
        Font font = workbook.createFont();
        font.setFontName("宋体"); // 字体样式
        font.setBold(isBold);    // 是否加粗
        font.setFontHeightInPoints(size);   // 字体大小
        return font;
    }
}
