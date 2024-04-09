package top.anets.modules.log.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import top.anets.modules.Mongodb.MongoDBUtil;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ftm
 * @date 2024-04-08 16:48
 */
public class MongoAppender<E> extends AppenderBase<LoggingEvent> {


    public static ConcurrentHashMap<String,String>  ServiceLevel =  new ConcurrentHashMap<String,String>();


    @Override
    protected void append(LoggingEvent loggingEvent) {
        NeedLog needLog = LogDbAop.NEED_LOG_THREAD_LOCAL.get();
        if(needLog!=null){
            String level = "INFO";
            if(needLog.getService()!=null&&ServiceLevel.get(needLog.getService())!=null){
                level = ServiceLevel.get(needLog.getService());
            }
            if(loggingEvent.getLevel().toInt() < Level.toLevel(level).toInt()){
                return;
            }

            Loginfo loginfo = new Loginfo();
            loginfo.setTime(new Date(loggingEvent.getTimeStamp()));
            loginfo.setBusiness(needLog.getBusiness());
            loginfo.setService(needLog.getService());
            loginfo.setMethod(needLog.getMethod());
            loginfo.setLevel(loggingEvent.getLevel().toString());
            loginfo.setContent(loggingEvent.getFormattedMessage());
            loginfo.setLoggerName(loggingEvent.getLoggerName());
            loginfo.setThreadName(loggingEvent.getThreadName());
            StackTraceElement[] callerData = loggingEvent.getCallerData();
            if(callerData!=null&&callerData.length>0){
                loginfo.setLocation(callerData[0].getClassName()+"."+callerData[0].getMethodName());
                loginfo.setLineNumber(callerData[0].getLineNumber());
            }
            MongoDBUtil.save(loginfo);
        }

    }
}
