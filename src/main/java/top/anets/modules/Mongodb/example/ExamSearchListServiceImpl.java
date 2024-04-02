package top.anets.modules.Mongodb.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import top.anets.base.PageQuery;
import top.anets.base.WrapperQueryForMongo;
import top.anets.modules.Mongodb.MongoDBUtil;

import java.util.*;

@RefreshScope
@Slf4j
@Repository
public class ExamSearchListServiceImpl implements ExamSearchListService {


    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void save(ExamSearchList examSearchList) {
        mongoTemplate.save(examSearchList);
    }

    @Override
    public void removeById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.findAllAndRemove(query, ExamSearchList.class);
    }

    @Override
    public void update(ExamSearchList examSearchList) {
        Query query = new Query(Criteria.where("id").is(examSearchList.get_id()));

        Update update = new Update();

        mongoTemplate.updateFirst(query, update, ExamSearchList.class);
    }

    @Override
    public ExamSearchList findById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        ExamSearchList examSearchList = mongoTemplate.findOne(query, ExamSearchList.class);
        return examSearchList;
    }

    @Override
    public List<ExamSearchList> finds() {
        return mongoTemplate.findAll(ExamSearchList.class);
    }

    @Override
    public IPage<ExamSearchList> page(Map<String, Object> params, PageQuery pageQuery) {
        IPage<ExamSearchList> page = MongoDBUtil.page(pageQuery.Page(), WrapperQueryForMongo.query(params), ExamSearchList.class);

        return page;
    }


}
