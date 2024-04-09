package top.anets.modules.log.service;

import top.anets.modules.log.entity.OperationRecord;
/**
 * @author ftm
 * @date 2023-12-07 14:05
 */
public interface OperationRecordService {
    public void record(OperationRecord operationRecord);
}
