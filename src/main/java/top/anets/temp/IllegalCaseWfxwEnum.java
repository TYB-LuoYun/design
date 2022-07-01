package top.anets.temp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoYun
 * @since 2022/6/29 11:51
 */
public enum IllegalCaseWfxwEnum {
    LT_30("1353","驾驶货车载物超过核定载质量未达30%的"),
    GT_30_LT_100("16371","驾驶货车载物超过核定载质量30%以上未达100%的"),
    GT_100_LT_200("16372","驾驶货车载物超过核定载质量100%以上未达200%的"),
    GT_200("16373","驾驶货车载物超过核定载质量200%以上"),
    GT_30("16379","驾驶货车载物超过核定载质量30%以上"),
    ;

    private String value;
    private String  label;

    IllegalCaseWfxwEnum(String value , String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }
    public String getLabel() {
        return label;
    }

    public static List<IllegalCaseWfxwEnum> getEumValueList() {
        List<IllegalCaseWfxwEnum> list=new ArrayList<IllegalCaseWfxwEnum>();
        for(IllegalCaseWfxwEnum object:IllegalCaseWfxwEnum.values()) {
            list.add(object );
        }
        return list;
    }
}
