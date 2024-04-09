package top.anets.modules.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 互认方式
 */
@Getter
@AllArgsConstructor
public enum HrMethodEnum {
    WebPage(0,"网页"),
    Client(1,"客户端");

    private Integer code;
    private String name;


    /**
     * 通过code获取name
     * @param code
     * @return
     */
    public static String getNameByCode(Integer code) {
        if(code == null){
            return null;
        }
        for(HrMethodEnum v : values()) {
            if(v.code == code) {
                return v.name;
            }
        }
        return null;
    }


}
