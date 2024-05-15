/**
 *
 */
package top.anets.exception;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.anets.utils.ExceptionUtil;
import top.anets.utils.ReflectUtil;
import top.anets.utils.RegexUtil;
import top.anets.utils.Result;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Administrator
 *
 */
@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Slf4j
public class GlobalExceptionHandler {
//	未知异常
	@ExceptionHandler(value = Exception.class)
    public Result doException(Exception e) {
		StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String trace = sw.toString();
        log.info(trace);
        log.error(trace);
        Throwable cause = this.getLastCause(e.getCause());
        if(cause!=null&&ServiceException.class == cause.getClass()){
            return this.doServiceException((ServiceException) cause);
        }
        return Result.error(ExceptionUtil.getErrorMsg(e), "||detail:"+trace);
    }

    private Throwable getLastCause(Throwable cause) {
	    if(cause == null){
	        return null;
        }
        Throwable causeCause = cause.getCause();
	    if(causeCause == null){
	        return cause;
        }
        return this.getLastCause(causeCause);
    }


    //	业务异常
    @ExceptionHandler(ServiceException.class)
    public Result doServiceException(ServiceException e) {
    	log.info(e.getMessage());
    	e.printStackTrace();
    	if(e.getCode() == null){
            return Result.error(e.getMessage(), null);
        }
    	return Result.error(e.getCode(), e.getMessage(), null);
    }


    /**
     * 处理所有RequestBody注解参数验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        /*注意：此处的BindException 是 Spring 框架抛出的Validation异常*/
        MethodArgumentNotValidException ex = (MethodArgumentNotValidException)e;

        FieldError fieldError = ex.getBindingResult().getFieldError();
        if(fieldError!=null) log.warn("必填校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());
        e.printStackTrace();
//        String errorMsg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return this.error("参数校验不通过:"+fieldError.getField()+fieldError.getDefaultMessage());
    }


    /**
     * 处理所有RequestParam注解数据验证异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if(fieldError!=null) {
            log.warn("必填校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());
            return this.error("参数校验不通过:"+fieldError.getDefaultMessage()+"("+fieldError.getField()+")");
        }
        ex.printStackTrace();
        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return this.error("参数校验不通过:"+defaultMessage);
    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public Result handleBadCredentialsException(BadCredentialsException ex) {
//        ex.printStackTrace();
//        return this.error("密码不匹配");
//    }
//
//    @ExceptionHandler(InternalAuthenticationServiceException.class)
//    public Result handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
//        ex.printStackTrace();
//        return this.error(ex.getMessage());
//    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public Result doMysqlDataTruncation(  Exception e) {
        e.printStackTrace();
        String msg = null;
        try {
            msg = e.getCause().getMessage();
        }catch (Exception e1){
        }

        try {
            String column = RegexUtil.findStrByLikeMatch("Data truncation: Data too long for column '", "' at", e.getCause().getMessage()).get(0);
            String filedName=  null;

//           尝试读取中文解释
            List<String> strByLikeMatch = RegexUtil.findStrByLikeMatch("The error may involve ", "Mapper", e.getMessage());
            Class<?> clazz = ReflectUtil.getClazz(strByLikeMatch.get(0) + "Mapper");
            Class<?> classT = ReflectUtil.getInterActualType(clazz);
            Field[] fields = classT.getDeclaredFields();
            for(Field fie : fields){
                if(!fie.isAccessible()){
                    fie.setAccessible(true);
                }
                TableField annotation = fie.getAnnotation(TableField.class);
                if(annotation!=null&&annotation.value().equals(column)){
//                   获取中文解释
                    ApiModelProperty annotation1 = fie.getAnnotation(ApiModelProperty.class);
                    if(annotation1!=null){
                        filedName = annotation1.value();
                    }
                }
            }
            if(StringUtils.isNotBlank(filedName)){
                msg = filedName+"("+column+")字段输入过长";
//                msg = filedName+"字段输入过长";
            }else{
                msg = column+"字段输入过长";
            }

        }catch (Exception e1){
        }

        if(msg!=null){
            return Result.error(msg);
        }else{
            return Result.error(e.getMessage());
        }
    }

    private Result error(String msg) {
        return Result.error(msg);
    }


}
