package top.anets.utils;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 用于封装接口统一响应结果
 */
@Data
@NoArgsConstructor // 无参构造方法
@AllArgsConstructor // 有参构造方法
public final class Result implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Result.class);

    private static final long serialVersionUID = 1L;


    private Boolean success;

    /**
     * 响应业务状态码
     */
    private String code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应中的数据
     */
    private Object data;

    public static Result ok() {
        return new Result(true,ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getDesc(), null);
    }

    public static Result ok(Object data) {
        return new Result(true,ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getDesc(), data);
    }

    public static Result ok(String message, Object data) {
        return new Result(true,ResultEnum.SUCCESS.getCode(), message, data);
    }

    public static Result success() {
        return new Result(true,ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getDesc(), null);
    }

    public static Result success(Object data) {
        
        return new Result(true,ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getDesc(), data);
    }

    public static Result success(String message, Object data) {
        return new Result(true,ResultEnum.SUCCESS.getCode(), message, data);
    }

    public static Result success(String message) {
        return new Result(true,ResultEnum.SUCCESS.getCode(), message, null);
    }

    public static Result error(String message, Object data) {
        return new Result(false,ResultEnum.ERROR.getCode(), message, data);
    }
    public static Result error(String code,String message, Object data) {
        return new Result(false,code, message, data);
    }


    public static Result error(String message) {
        logger.debug("返回错误：code={}, message={}", ResultEnum.ERROR.getCode(), message);
        return new Result(false,ResultEnum.ERROR.getCode(), message, null);
    }

    public static Result build(String code, String message) {
        logger.debug("返回结果：code={}, message={}", code, message);
        return new Result(null,code, message, null);
    }

    public static Result build(ResultEnum resultEnum) {
        logger.debug("返回结果：code={}, message={}", resultEnum.getCode(), resultEnum.getDesc());
        return new Result(null,resultEnum.getCode(), resultEnum.getDesc(), null);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}