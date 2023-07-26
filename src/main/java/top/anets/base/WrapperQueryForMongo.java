package top.anets.base;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
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

            if(key.contains("$sort")){
                orders.addAll(parseOrder((String) map.get(key))) ;
            }else if(key.contains("$desc")){
                orders.addAll(parseDesc( map.get(key))) ;
            } else if(key.contains("$asc")){
                orders.addAll(parseAsc( map.get(key))) ;
            }else if(key.equals("$or")){
                Object o = map.get(key);
                if(o instanceof List){
                    ((List)o).forEach(you->{
                        addOrCriterias(criterias,(Map)you);
                    });
                }else{
                    addOrCriterias(criterias,(Map) map.get(key));
                }

            } else if(key.contains("$notBlank")){
                List<String> strings = fetchWord(map.get(key));
                if(CollectionUtils.isEmpty(strings)){
                    return;
                }
                strings.forEach(each->{
                    criterias.add(Criteria.where(each).ne("").ne(null));
                });
            }else {
                Criteria criteria = getCriteria(key, map);
                if(criteria!=null){
                    criterias.add(criteria);
                }
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
        return query;
    }

    private static void addOrCriterias(List<Criteria> criterias, Map<String,Object> mapOr) {
        if(mapOr !=null &&mapOr.size()>0){
            List<Criteria> criteriasOr = new ArrayList<>();
            mapOr.entrySet().forEach(itemOr->{
                Criteria criteria = getCriteria(itemOr.getKey(), mapOr);
                if(criteria!=null){
                    criteriasOr.add(criteria);
                }
            });
            if(!criteriasOr.isEmpty()){
                criterias.add(new Criteria().orOperator(criteriasOr.toArray(new Criteria[criteriasOr.size()])));
            }
        }
    }

    private static Criteria getCriteria(String key, Map<String, Object> map) {
        if(ObjectUtils.isEmpty( map.get(key))){
           return null;
        }
        //            OrganName$like
        String column="";
        if(key.contains("$in")){
            column=key.replace("$in", "");
            List<String> strs = fetchWord(map.get(key));
            return Criteria.where(column).in(strs);
        }else if(key.contains("$notin")){
            column=key.replace("$notin", "");
            List<String> strs = fetchWord(map.get(key));
            return Criteria.where(column).nin(strs);
        }else
        if(key.contains("$like")){
            column=key.replace("$like", "");
            String pattern = ".*" + map.get(key) + ".*";
            return Criteria.where(column).regex(pattern);
        }else if(key.contains("$lte")){
            column=key.replace("$lte", "");
            return Criteria.where(column).lte(parseDateSmart(map.get(key)));
        }else if(key.contains("$gte")){
            column=key.replace("$gte", "");
            return Criteria.where(column).gte(parseDateSmart(map.get(key)));

        }else if(key.contains("$lt")){
            column=key.replace("$lt", "");
            return Criteria.where(column).lt(parseDateSmart(map.get(key)));
        }else if(key.contains("$gt")){
            column=key.replace("$gt", "");
            return Criteria.where(column).gt(parseDateSmart(map.get(key)));
        }else {
            return Criteria.where(key).is(map.get(key));
        }
    }

    private static Object parseDateSmart(Object time) {
        if(time instanceof String){
            Date parse = null;
            try {
                parse = new SimpleDateFormat(TimeFormat).parse((String) time);
            } catch (ParseException e) {
                return parse;
            }
            return parse;
        }else{
            return time;
        }
    }


    private static List<Sort.Order> parseDesc(Object ordersStr) {
        List<Sort.Order>  orders  = new ArrayList<>();
        if(ordersStr == null){
            return orders;
        }
        List<String> descs = fetchWord(ordersStr);
        if(CollectionUtils.isNotEmpty(descs)){
            descs.forEach(each->{
                orders.add(Sort.Order.desc(each));
            });
        }
        return orders;
    }


    private static List<Sort.Order> parseAsc(Object ordersStr) {
        List<Sort.Order>  orders  = new ArrayList<>();
        if(ordersStr == null){
            return orders;
        }
        List<String> descs = fetchWord(ordersStr);
        if(CollectionUtils.isNotEmpty(descs)){
            descs.forEach(each->{
                orders.add(Sort.Order.asc(each));
            });
        }
        return orders;
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
