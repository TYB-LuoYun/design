package top.anets.modules.log.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import org.apache.commons.lang3.StringUtils;
import top.anets.exception.ServiceException;
import top.anets.modules.log.requestDbLog.service.OperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ftm
 * @date 2024-04-08 16:48
 */
@Component
public class MongoAppender<E> extends AppenderBase<LoggingEvent> {


    private static OperationRecordService operationRecordService;
    @Autowired
    private OperationRecordService operationRecordService1;

    @PostConstruct
    private void  init(){
        operationRecordService = operationRecordService1;
    }

    public static ConcurrentHashMap<String,String>  ServiceLevel =  new ConcurrentHashMap<String,String>();
    public static ConcurrentHashMap<String,Boolean>  SystemSql =  new ConcurrentHashMap<String,Boolean>();

    @Override
    protected void append(LoggingEvent loggingEvent) {
        NeedLog needLog = LogDbAop.NEED_LOG_THREAD_LOCAL.get();
        if(needLog!=null){
            String level = "DEBUG";
            if(needLog.getClassify()!=null&&ServiceLevel.get(needLog.getClassify())!=null){
                level = ServiceLevel.get(needLog.getClassify());
            }
            if(loggingEvent.getLevel().toInt() < Level.toLevel(level).toInt()){
                return;
            }
            Loginfo loginfo = new Loginfo();
            loginfo.setTime(new Date(loggingEvent.getTimeStamp()));
            loginfo.setBusiness(needLog.getBusiness());
            loginfo.setClassify(needLog.getClassify());
            loginfo.setMethod(needLog.getMethod());
            loginfo.setLevel(loggingEvent.getLevel().toString());
            loginfo.setContent(loggingEvent.getFormattedMessage());
            loginfo.setLoggerName(loggingEvent.getLoggerName());

            loginfo.setThreadName(loggingEvent.getThreadName());
            StackTraceElement[] callerData = loggingEvent.getCallerData();
            if(callerData!=null&&callerData.length>0){
                if(!callerData[0].getClassName().contains("com.ruoyi")&&(SystemSql.get(needLog.getClassify())==null||SystemSql.get(needLog.getClassify()) == false)){
//              关闭系统sql日志
                        return;
                }
                loginfo.setLocation(getSimpleClassName(callerData[0].getClassName())+"."+callerData[0].getMethodName());
                loginfo.setLineNumber(callerData[0].getLineNumber());
            }
            if(loggingEvent.getThrowableProxy()!=null){
                IThrowableProxy proxy = loggingEvent.getThrowableProxy();
                String attach  = null;
                Throwable throwable = ((ThrowableProxy) proxy).getThrowable();
                Throwable cause = getLastCause(throwable);
                if(throwable instanceof ServiceException || (cause!=null&&cause instanceof ServiceException)
//                  || throwable instanceof ServiceException || (cause!=null&&cause instanceof ServiceException)
                ){
                    attach = getErrorMsg(throwable,cause);
                }else{
                    attach  = getErrorMsg(throwable,cause)+"\n"+getErrorLocation(proxy);
                }
                if(loginfo.getContent()!=null){
                    loginfo.setContent(loginfo.getContent()+" -> "+attach);
                }else{
                    loginfo.setContent(attach);
                }

            }
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    operationRecordService.save(loginfo);
                }
            });
        }
    }

    private String getErrorMsg(Throwable throwable, Throwable cause) {
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



    private String getErrorLocation(IThrowableProxy throwableProxy) {
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

    private String getSimpleClassName(String className) {
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
}
