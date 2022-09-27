package top.anets.modules.excel.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ftm
 * @date 2022/9/26 0026 12:54
 */
@Data
public class EasyExcelTest implements Serializable {
    private static final long serialVersionUID = 8362987561243233425L;

    //@ExcelProperty(value ="类型",index = 0)     //文档中建议只用 index 或者只用 value 不要混用
    @ExcelProperty(index = 0)
    private String type;//开支类型 信用卡等

    @ExcelProperty(index =1)
    private String sum;

    @ExcelProperty(index =2)
    private String name;//开支来源  如：**银行信用卡

    @ExcelProperty(index =3)
    private String date;

    @ExcelProperty(index =4)
    private Integer status;

    @ExcelProperty(index =5)
    private String descr;

}
