package top.anets.modules.log.requestDbLog.service;


import top.anets.modules.log.appender.Loginfo;
import top.anets.modules.log.requestDbLog.entity.OperationRecord;

/**
 * @author ftm
 * @date 2023-12-07 14:05
 */
public interface OperationRecordService {
    public void record(OperationRecord operationRecord);

    void save(Loginfo loginfo);
}
