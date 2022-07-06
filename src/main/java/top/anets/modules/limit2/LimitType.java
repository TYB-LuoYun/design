package top.anets.modules.limit2;

/**
 * @author LuoYun
 * @since 2022/7/6 14:06
 */
public enum LimitType {
    IP(0), CUSTOMER(1) ;

    private Integer value;

    LimitType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
