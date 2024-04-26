package top.anets.modules.log.requestDbLog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import top.anets.modules.Mongodb.MongoDBUtil;
import top.anets.modules.log.appender.Loginfo;
import top.anets.modules.log.requestDbLog.entity.OperationRecord;
import top.anets.support.mongodb.danamicdatasource.MongoDB;
import top.anets.support.mongodb.danamicdatasource.MongoDataSource;

/**
 * @author ftm
 * @date 2023-12-07 14:10
 */
@Service
//@RefreshScope
@MongoDataSource(MongoDB.LOGDB)
public class
OperationRecordServiceImpl implements OperationRecordService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${system.hrStateEnable:false}")
    private Boolean HrStateEnable;

    @Override
    public void record(OperationRecord operationRecord) {
        MongoDBUtil.save(operationRecord);
    }

    @Override
    public void save(Loginfo loginfo) {
        MongoDBUtil.save(loginfo);
    }

}
