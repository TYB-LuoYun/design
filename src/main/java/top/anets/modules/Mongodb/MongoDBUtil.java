package top.anets.modules.Mongodb;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class MongoDBUtil {
    private static final Query EMPTY_QUERY = new BasicQuery("{}");
    private static  MongoTemplate template;

    @Autowired
    private MongoTemplate mongoTemplate1;
    @PostConstruct
    private void  init(){
        MongoDBUtil.template = mongoTemplate1;
    }


    private static Query idEqQuery(Serializable id) {
        Criteria criteria = Criteria.where("id").is(id);
        return Query.query(criteria);
    }

    private static Query idInQuery(Collection<? extends Serializable> idList) {
        Criteria criteria = Criteria.where("id").in(idList);
        return Query.query(criteria);
    }

    private static Query eqQuery(Map<String, Object> data) {
        if (CollectionUtils.isEmpty(data)) {
            return EMPTY_QUERY;
        } else {
            Criteria criteria = new Criteria();
            data.forEach((k, v) -> criteria.and(k).is(v));
            return Query.query(criteria);
        }
    }

    private static <T> Serializable getIdValue(T entity) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return (Serializable) field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> Update getUpdate(T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                System.out.println(field.getName()+" "+field.get(entity));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> void save(T entity) {
        template.save(entity);
    }

    public static <T> void saveBatch(Collection<T> entityList) {
        template.insertAll(entityList);
    }

    public static void removeById(Serializable id, Class<?> clazz) {
        template.remove(idEqQuery(id), clazz);
    }

    public static void removeByMap(Map<String, Object> columnMap, Class<?> clazz) {
        template.remove(eqQuery(columnMap), clazz);
    }

    public static void removeByIds(Collection<? extends Serializable> idList, Class<?> clazz) {
        template.remove(idInQuery(idList), clazz);
    }

    public static void remove(Query query, Class<?> clazz) {
        template.remove(query, clazz);
    }

    public static <T> boolean updateById(T entity) {
        Assert.notNull(entity, "entity must not be null!");
        JSONObject obj = (JSONObject) JSONObject.toJSON(entity);
        DBObject update = new BasicDBObject();
        update.put("$set", obj);
        UpdateResult result = template.updateFirst(idEqQuery(getIdValue(entity)), new BasicUpdate(update.toString()), entity.getClass());
        return result.getModifiedCount() == 1L;
    }

    public static <T> void updateBatchById(Collection<T> entityList) {
        entityList.forEach(e -> updateById(e));
    }

    public static void update(Query query, Update update, Class<?> clazz) {
        template.updateMulti(query, update, clazz);
    }

    public static <T> void saveOrUpdate(T entity) {
        Assert.notNull(entity, "entity must not be null!");
        String key = JSONObject.toJSONString(entity);
        Update inc = new Update().inc(key, 1);
        template.upsert(idEqQuery(getIdValue(entity)), inc, entity.getClass());
    }

    public static <T> void saveOrUpdateBatch(Collection<T> entityList) {
        entityList.forEach(MongoDBUtil::saveOrUpdate);
    }

    public static <T> T getById(Serializable id, Class<T> clazz) {
        return template.findById(id, clazz);
    }

    public static <T> T getOne(Query query, Class<T> clazz) {
        return template.findOne(query, clazz);
    }

    public static <T> List<T> listByIds(Collection<? extends Serializable> idList, Class<T> clazz) {
        return template.find(idInQuery(idList), clazz);
    }

    public static <T> List<T> listByMap(Map<String, Object> columnMap, Class<T> clazz) {
        return template.find(eqQuery(columnMap), clazz);
    }

    public static <T> List<T> list(Class<T> clazz) {
        return template.findAll(clazz);
    }

    public static <T> List<T> list(Query query, Class<T> clazz) {
        return template.find(query, clazz);
    }

    public static <T> long count(Class<T> clazz) {
        return template.count(EMPTY_QUERY, clazz);
    }

    public static <T> long count(Query query, Class<T> clazz) {
        return template.count(query, clazz);
    }

    public static <T> IPage<T> page(IPage page, Class<T> clazz) {
        page.setTotal(count(clazz));
        Pageable pageable = PageRequest.of((int) (page.getCurrent()-1), (int) page.getSize());
        List<T> records = template.find(new Query().with( Sort.by(Sort.Direction.DESC, "id")).with(pageable), clazz);
        page.setPages(page.getPages());
        page.setRecords(records);
        return page;
    }

    public static <T> IPage<T> page(IPage page, Query query, Class<T> clazz) {
        page.setTotal(count(query, clazz));
        Pageable pageable = PageRequest.of((int) (page.getCurrent()-1), (int) page.getSize());
        List<T> records = template.find(query.with(pageable), clazz);
        page.setPages(page.getPages());
        page.setRecords(records);
        return page;
    }

}