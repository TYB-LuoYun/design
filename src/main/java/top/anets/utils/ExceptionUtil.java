package top.anets.utils;

import ch.qos.logback.classic.spi.IThrowableProxy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import top.anets.exception.ServiceException;

/**
 * @author ftm
 * @date 2024-05-09 16:06
 */
public class ExceptionUtil {

    public static String getErrorMsg(Throwable throwable) {
        return getErrorMsg(throwable,  ExceptionUtils.getRootCause(throwable));
    }


    public static String getErrorMsg(Throwable throwable, Throwable cause) {
        String msg = "";
        if(cause!=null){
            if(cause instanceof ServiceException
//                    || cause instanceof ServiceException
            ){
                return  cause.getMessage();
            }else if(cause == throwable&&cause.getMessage()!=null){
                return  cause.getMessage() + "["+cause.getClass().getSimpleName()+"]";
            }
            if(cause.getMessage()!=null){
                msg = cause.getMessage()+"\n";
            }
        }
        if(throwable.getMessage()!=null){
            msg+=throwable.getMessage();
        } else{
            msg+="["+throwable.getClass().getSimpleName()+"]";
        }
        return msg;
    }



    public static String getErrorLocation(IThrowableProxy throwableProxy) {
        String location ="";
        for(int i=0;i<throwableProxy.getStackTraceElementProxyArray().length;i++){
            StackTraceElement element = throwableProxy.getStackTraceElementProxyArray()[i].getStackTraceElement();
            if(element.getLineNumber()<0){
                continue;
            }
            if(element.getClassName().contains("com.ruoyi")&&!element.getClassName().contains("LogDbAop")){
                if(i != 0){
                    location+="<-";
                }
                location+= "("+element.getLineNumber()+")"+getSimpleClassName(element.getClassName())+"."+element.getMethodName();
            }
        }
        if(StringUtils.isBlank(location)){
            return "";
        }
        return "出错位置:"+location;
    }
    public static String getSimpleClassName(String className) {
        if(className == null){
            return null;
        }
        int lastIndex = className.lastIndexOf('.');
        if(lastIndex>0){
            return className.substring(lastIndex + 1);
        }else{
            return className;
        }
    }
}
