package top.anets.modules.verify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ftm
 * @date 2023-10-25 13:26
 */

@Getter
@AllArgsConstructor
public enum RMDResultEnum {
    SUCCESS(2000, "请求成功"),
    SYSTEM_ERROR(4000, "系统内部错误"),
    CUSTOM_ERROR(5000, "服务发生错误"),
    TOKEN_EXPIRE(2001, "Token不合法或已过期"),
    AUTH_FAIL(2002, "身份验证失败"),
    INVALID_PARAMS(3000, "请求参数不合法"),
    LACK_PARAMS(3001, "缺少请求参数"),
    JSON_ERROR(3002, "JSON串错误！"),
    INVALID_BODY(3003, "请求体内容校验不通过"),
    UN_ACTIVE(985,"未激活"),
    ;

    private Integer code;
    private String desc;
}
