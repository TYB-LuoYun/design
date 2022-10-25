package top.anets.base;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static top.anets.base.WrapperQuery.fetchWord;
import static top.anets.base.WrapperQuery.objectToMap;


/**
 * @author ftm
 * @date 2022/10/25 0025 16:25
 */
public class WrapperQueryForMongo {
    private static List<String> Exclude = Arrays.asList("current","size","total","serialVersionUID");
    private static String TimeFormat = "yyyy-MM-dd HH:mm:ss";

    public static Query query(Object map){
        return query(objectToMap(map));
    }

    public static Query query(Map<String, Object> map) {
        Query query = new Query();
        if(map==null){
            return query;
        }
        List<Sort.Order> orders = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        map.entrySet().forEach(item->{
            String key = item.getKey();//字段名
            if(Exclude.contains(key)){
                return;
            }
            if(map.get(key)==null || map.get(key)==""){
                return;
            }
//            OrganName$like
            String column="";

            if(key.contains("$in")){
                column=key.replace("$in", "");
                List<String> strs = fetchWord((String)map.get(key));
                criterias.add(Criteria.where(column).in(strs));
            }else
            if(key.contains("$like")){
                column=key.replace("$like", "");
                String pattern = ".*" + map.get(key) + ".*";
                criterias.add(Criteria.where(column).regex(pattern));
            }else if(key.contains("$lte")){
                column=key.replace("$lte", "");
                try {
                    Date parse = new SimpleDateFormat(TimeFormat).parse((String) map.get(key));
                    criterias.add(Criteria.where(column).lte( parse));
                } catch (ParseException e) {
                    criterias.add(Criteria.where(column).lte(map.get(key)));
                }
            }else if(key.contains("$gte")){
                column=key.replace("$gte", "");
                try {
                    Date parse = new SimpleDateFormat(TimeFormat).parse((String) map.get(key));
                    criterias.add(Criteria.where(column).gte( parse));
                } catch (ParseException e) {
                    criterias.add(Criteria.where(column).gte(map.get(key)));
                }

            }else if(key.contains("$lt")){
                column=key.replace("$lt", "");
                try {
                    Date parse = new SimpleDateFormat(TimeFormat).parse((String) map.get(key));
                    criterias.add(Criteria.where(column).lt( parse));
                } catch (ParseException e) {
                    criterias.add(Criteria.where(column).lt(map.get(key)));
                }
            }else if(key.contains("$gt")){
                column=key.replace("$gt", "");
                try {
                    Date parse = new SimpleDateFormat(TimeFormat).parse((String) map.get(key));
                    criterias.add(Criteria.where(column).gt( parse));
                } catch (ParseException e) {
                    criterias.add(Criteria.where(column).gt(map.get(key)));
                }
            }else if(key.contains("$notBlank")){
                List<String> strings = fetchWord(map.get(key));
                if(CollectionUtils.isEmpty(strings)){
                    return;
                }
                strings.forEach(each->{
                    criterias.add(Criteria.where(each).ne("").ne(null));
                });
            }else if(key.contains("$sort")){
                orders.addAll(parseOrder((String) map.get(key))) ;
            }
            else {
                criterias.add(Criteria.where(key).is(map.get(key)));
            }


        });
        Criteria criteria = new Criteria();
        if(CollectionUtils.isNotEmpty(criterias)){
            Criteria[] criteriasz = criterias.toArray(new Criteria[criterias.size()]);
            criteria.andOperator( criteriasz);
            query.addCriteria(criteria);
        }
        /**
         * 增加排序功能
         */
        if(CollectionUtils.isNotEmpty(orders)){
            query.with(Sort.by(orders));
        }
        return  query;
    }

    /**
     * 字段 desc,字段 asc
     * @return
     */
    private static List<Sort.Order> parseOrder(String ordersStr) {
        List<Sort.Order>  orders  = new ArrayList<>();
        if(StringUtils.isBlank(ordersStr)){
            return orders;
        }
        String[] split = ordersStr.split(",|，|；|;");
        List<String> strings = Arrays.asList(split);
        if(CollectionUtils.isEmpty(strings)){
            return orders;
        }
        strings.forEach(item->{
            if(item.contains("desc")){
                String desc = item.replace("desc", "");
                List<String> descs = fetchWord(desc);
                if(CollectionUtils.isNotEmpty(descs)){
                    descs.forEach(each->{
                        orders.add(Sort.Order.desc(each));
                    });
                }
            }else if(item.contains("asc")){
                String asc = item.replace("asc", "");
                List<String> ascs = fetchWord(asc);
                if(CollectionUtils.isNotEmpty(ascs)){
                    ascs.forEach(each->{
                        orders.add(Sort.Order.asc(each));
                    });
                }
            }else{
                //默认降序排列
                List<String> descs = fetchWord(item);
                if(CollectionUtils.isNotEmpty(descs)){
                    descs.forEach(each->{
                        orders.add(Sort.Order.desc(each));
                    });
                }
            }
        });
        return orders;
    }
}
