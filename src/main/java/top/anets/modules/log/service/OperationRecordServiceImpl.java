package top.anets.modules.log.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import top.anets.modules.Mongodb.MongoDBUtil;
import top.anets.modules.log.entity.OperationRecord;
import top.anets.support.mongodb.danamicdatasource.MongoDB;
import top.anets.support.mongodb.danamicdatasource.MongoDataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ftm
 * @date 2023-12-07 14:10
 */
@Service
@RefreshScope
@MongoDataSource(MongoDB.LOGDB)
public class OperationRecordServiceImpl implements OperationRecordService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${system.hrStateEnable:false}")
    private Boolean HrStateEnable;

    @Override
    public void record(OperationRecord operationRecord) {
        MongoDBUtil.save(operationRecord);
    }

}
