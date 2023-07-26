package top.anets.support.mongodb.danamicdatasource;

/**
 * @author ftm
 * @date 2023/3/9 0009 12:34
 */
public enum MongoDB {
    DATACENTER("DataCenter", "mongodb_1数据源"),
    LOGDB("LogDB", "mongodb_2数据源");

    private String value;
    private String display;

    MongoDB(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}