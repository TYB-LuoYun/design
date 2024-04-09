package top.anets.modules.task.history.enums;

/**
 * @author ftm
 * @date 2024-04-07 11:39
 */
public enum OperateEnum {
    ADD(1,"添加"),
    UPDATE(2,"修改"),
    RUN_ONCE(3,"运行一次"),
    PAUSE(4,"暂停"),
    RESUME(5,"恢复"),
    DELETE(6,"删除");

    private Integer code;

    private String msg;

    OperateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
