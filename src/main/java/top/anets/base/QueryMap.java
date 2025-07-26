package top.anets.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.base.WrapperQuery.fetchWord;
import static com.ruoyi.common.base.WrapperQuery.upperCharToUnderLine;


/**
 * @author ftm
 * @date 2023/3/2 0002 15:35
 */
@Accessors(chain = true)
public class  QueryMap extends LinkedHashMap<String,Object> implements Map<String,Object> {

    final private static String LIKE = "$like";
    final private  static String IN = "$in";
    final private  static String NOTIN = "$notin";
    final private  static String GT = "$gt";
    final private  static String LT = "$lt";
    final private  static String GTE = "$gte";
    final private  static String LTE = "$lte";
    final private  static String DESC = "$desc";
    final private  static String ASC = "$asc";
    final private  static String ORDER = "$order";
    final private  static String OR = "$or";
    final private  static String AND = "$and";
    final private static String GROUPBY = "$groupBy";
    final private static String SELECT = "$select";
    final private static String APPLY = "$apply";
    private Integer orPointer = 0;
    private Integer andPointer = 0;

    private String name;

    public static QueryMap build(){
        return new QueryMap();
    }

    @SafeVarargs
    public static <T> QueryMap pickResult(T source,  Fields.SFunction<T, ?>... getters) {
        if(source == null){
            return null;
        }
        QueryMap result = new QueryMap();
        for (Fields.SFunction<T, ?> getter : getters) {
            String fieldName = Fields.getFieldName(getter); // 提取字段名
            Object value = getter.apply(source);    // 执行 getter 拿值
            result.put(fieldName, value);
        }
        return result;
    }


    public <T> QueryWrapper<T> wrapper() {
        QueryWrapper <T> queryWrapper   = new QueryWrapper<T>();
        setCriteria(queryWrapper,this);
        return queryWrapper;
    }


    public static  <T> QueryWrapper  wrapper(T t) {
        return new QueryWrapper<T>(t);
    }

    public <T> QueryMap put(Fields.SFunction<T, ?> field,Object value){
        super.put( Fields.name(field), value);
        return this;
    }

    public <T> QueryMap puts(Fields.SFunction<T, ?> field,Object value){
        super.put( Fields.name(field), value);
        return this;
    }



    public <T> QueryMap puts(String field,Object value){
        super.put( field, value);
        return this;
    }

    public <T> QueryMap eq(Fields.SFunction<T, ?> field,Object value){
        super.put(Fields.name(field), value);
        return this;
    }
    public <T> QueryMap groupBy(Fields.SFunction<T, ?>  field) {
        super.put(GROUPBY, Fields.name(field));
        return this;
    }

    public <T> QueryMap groupBy(Fields.SFunction<T, ?>... fields) {
        String joinedFields = Arrays.stream(fields)
                .map(Fields::name)
                .collect(Collectors.joining(","));
        super.put(GROUPBY, joinedFields);
        return this;
    }

    public <T> QueryMap select(String select) {
        super.put(SELECT, select);
        return this;
    }

    public <T> QueryMap apply(String condition) {
        super.put(APPLY, condition);
        return this;
    }


    public <T> QueryMap select(Fields.SFunction<T, ?>... fields) {
        String joinedFields = Arrays.stream(fields)
                .map(Fields::name)
                .collect(Collectors.joining(","));
        super.put(SELECT, joinedFields);
        return this;
    }

    public <T> QueryMap select(Fields.SFunction<T, ?> field) {
        super.put(SELECT, Fields.name(field));
        return this;
    }


    public <T> QueryMap eq(String field,Object value){
        super.put(field, value);
        return this;
    }



    public <T>  QueryMap like(Fields.SFunction<T, ?> field,Object value){
        like(Fields.name(field),value );
        return this;
    }

    public <T>  QueryMap like(String field,Object value){
        super.put(field+LIKE, value);
        return this;
    }




    public <T>  QueryMap in(Fields.SFunction<T, ?> field){
        in(Fields.name(field));
        return this;
    }

    public <T>  QueryMap in(String field){
        removeAndSet( field, IN);
        return this;
    }


    public <T>  QueryMap notin(Fields.SFunction<T, ?> field) {
        notin(Fields.name(field));
        return this;
    }

    public <T>  QueryMap notin(String field) {
        removeAndSet( field,NOTIN);
        return this;
    }




    void removeAndSet(String field,String operation){
        Object value = super.get(field);
        if(value == null){
            return  ;
        }
        super.remove(field);
        super.put(field+operation, value);
    }


    public <T>  QueryMap like(Fields.SFunction<T, ?> field){
        like(Fields.name(field));
        return this;
    }

    public <T>  QueryMap like(String field){
        removeAndSet( field, LIKE);
        return this;
    }

    public <T> QueryMap notin(Fields.SFunction<T, ?> field,Object value) {
        return this.notin(Fields.name(field), value);
    }

    public QueryMap notin(String field, Object value) {
        super.put(field+NOTIN, value);
        return this;
    }


    public <T> QueryMap in(Fields.SFunction<T, ?> field,Object value){
        super.put(Fields.name(field)+IN, value);
        return this;
    }

    public <T> QueryMap in(String field,Object value){
        super.put(field+IN, value);
        return this;
    }

    public <T> QueryMap gt(Fields.SFunction<T, ?> field,Object value){
        super.put(Fields.name(field)+GT, value);
        return this;
    }

    public <T> QueryMap gt(String field,Object value){
        super.put(field+GT, value);
        return this;
    }

    public <T> QueryMap lt(Fields.SFunction<T, ?> field,Object value){
        super.put(Fields.name(field)+LT, value);
        return this;
    }

    public <T> QueryMap lt(String field,Object value){
        super.put(field+LT, value);
        return this;
    }

    public <T> QueryMap gte(Fields.SFunction<T, ?> field,Object value){
        super.put(Fields.name(field)+GTE, value);
        return this;
    }


    public <T> QueryMap gte(String field,Object value){
        super.put(field+GTE, value);
        return this;
    }










    public <T> QueryMap lte(Fields.SFunction<T, ?> field,Object value){
        super.put(Fields.name(field)+LTE, value);
        return this;
    }

    public <T> QueryMap lte(String field,Object value){
        super.put(field+LTE, value);
        return this;
    }


    /**
     * 针对mysql的or操作
     * @param <T>
     * @return
     */
    public <T> QueryMap or(){
        if(this.size()<=0){
            return this;
        }
        super.put(OR+orPointer,orPointer);
        orPointer=orPointer+1;
        return this;
    }

    /**
     * 针对mongo的or操作
     * @param orList
     * @param <T>
     * @return
     */
    public <T> QueryMap or(QueryMap orList){
        Object o = super.get(OR);
        if(o== null){
            super.put(OR, orList);
            return this;
        }
        List<QueryMap> ors = null;
        if(o instanceof List){
            ors = (List)o;
            ors.add(orList);
        }else{
            ors = new ArrayList();
            ors.add(orList);
        }
        super.put(OR, ors);
        return this;
    }



    /**
     * 相当于括号  and  ()
     * @param
     * @param <T>
     * @return
     */
    public <T> QueryMap and(QueryMap map){
        super.put(AND+andPointer, map);
        return this;
    }



    public <T> QueryMap desc(Fields.SFunction<T, ?>... field){
        if(field==null||field.length<=0){
            return this;
        }
        List<Fields.SFunction<T, ?>> sFunctions = Arrays.asList(field);
        List<String> collect = sFunctions.stream().map(e -> {
            return Fields.name(e);
        }).collect(Collectors.toList());
        super.put(DESC, collect);
        return this;
    }

    public <T> QueryMap desc(String... field){
        if(field==null||field.length<=0){
            return this;
        }
        List<String> sFunctions = Arrays.asList(field);
        super.put(DESC, sFunctions);
        return this;
    }


    public <T> QueryMap asc(Fields.SFunction<T, ?>... field){
        if(field==null||field.length<=0){
            return this;
        }
        List<Fields.SFunction<T, ?>> sFunctions = Arrays.asList(field);
        List<String> collect = sFunctions.stream().map(e -> {
            return Fields.name(e);
        }).collect(Collectors.toList());
        super.put(ASC, collect);
        return this;
    }

    public <T> QueryMap asc(String... field){
        if(field==null||field.length<=0){
            return this;
        }
        List<String> sFunctions = Arrays.asList(field);
        super.put(ASC, sFunctions);
        return this;
    }



    public <T> QueryMap order(String orderRule){
        super.put(ORDER, orderRule);
        return this;
    }


    /**
     * 最好是实体，没有其他字段
     * @param entity
     * @return
     */
    public  static QueryMap from(Object entity){
        if (entity == null) {
            return null;
        }
        List<Field> fieldList = new ArrayList<>();
        int i = 0;
        Class<?> clazz = entity.getClass();
        while (clazz != null && i < 2) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
            i++;
        }
        Field[] fields = new Field[fieldList.size()];
        Field[] declaredFields = fieldList.toArray(fields);
        QueryMap queryMap = new QueryMap();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                if(field.get(entity)!=null){
                    queryMap.put(field.getName(), field.get(entity));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return queryMap;
    }


    /**
     * 只封装实体有关的条件
     * @param condition
     * @param entity
     * @param <T>
     * @return
     */
    public static   <T> QueryMap from(Object condition,T entity){
        Object obj =condition;
        if (obj == null) {
            return null;
        }
        QueryMap queryMap = new QueryMap();
        Field[] entityFields = ((Class)entity).getDeclaredFields();
        if(condition instanceof Map){
            Map<String,Object> map = (Map) condition;
            for (Entry<String,Object>  eve: map.entrySet()) {
                for(Field entityField : entityFields){
                    if(entityField.getName().equalsIgnoreCase(eve.getKey())){
                        try {
                            if(eve.getValue()!=null){
                                queryMap.put(entityField.getName(),eve.getValue());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return queryMap;
        }
        List<Field> fieldList = new ArrayList<>();
        int i = 0;
        Class<?> clazz = obj.getClass();
        while (clazz != null && i < 2) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
            i++;
        }

        Field[] fields = new Field[fieldList.size()];
        Field[] declaredFields = fieldList.toArray(fields);
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                if(Modifier.isStatic(field.getModifiers())){
                    continue;
                }
                if(field.get(obj)!=null&&((Class)entity).getDeclaredField(field.getName())!=null){

                    TableField annotation = field.getAnnotation(TableField.class);
                    if(annotation!=null){
                        queryMap.put(annotation.value(), field.get(obj));
                    }else{
                        queryMap.put(upperCharToUnderLine(field.getName()), field.get(obj));
                    }

                }
            } catch (Exception e) {
            }
        }
        return queryMap;
    }


    public static   <T> QueryMap fromIgnoreCase(Object condition,T entity){
        Object obj =condition;
        if (obj == null) {
            return null;
        }
        QueryMap queryMap = new QueryMap();
        Field[] entityFields = ((Class)entity).getDeclaredFields();
        if(condition instanceof Map){
            Map<String,Object> map = (Map) condition;
            for (Entry<String,Object>  eve: map.entrySet()) {
                for(Field entityField : entityFields){
                    if(entityField.getName().equalsIgnoreCase(eve.getKey())){
                        try {
                            if(eve.getValue()!=null){
                                queryMap.put(entityField.getName(),eve.getValue());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return queryMap;
        }
        List<Field> fieldList = new ArrayList<>();
        int i = 0;
        Class<?> clazz = obj.getClass();
        while (clazz != null && i < 2) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
            i++;
        }

        Field[] fields = new Field[fieldList.size()];
        Field[] declaredFields = fieldList.toArray(fields);

        for (Field field : declaredFields) {
            for(Field entityField : entityFields){
                if(entityField.getName().equalsIgnoreCase(field.getName())){
                    field.setAccessible(true);
                    try {
                        if(field.get(obj)!=null){
                            queryMap.put(entityField.getName(), field.get(obj));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryMap;
    }






    public static void main(String[] args){
        QueryMap queryMap = new QueryMap();
        queryMap.puts("www", "3").puts("333", 3);
        System.out.println(queryMap);
    }




    public static   void setCriteria(QueryWrapper wrapper  , Map<String, Object> map){
        for (Entry<String,Object> item: map.entrySet()) {
            String key = item.getKey();//字段名

            if (map.get(key) == null || map.get(key) == "") {
                continue;
            }
            String column = "";
            if(key.contains("$in")){
                column=key.replace("$in", "");
                List<String> strs = fetchWord( map.get(key));
                wrapper.in(column,strs);
            }else if(key.contains("$notin")){
                column=key.replace("$notin", "");
                List<String> strs = fetchWord( map.get(key));
                wrapper.notIn(column,strs);
            }else if (key.contains("$like")) {
                column = key.replace("$like", "");
                wrapper.like(column, map.get(key));
            } else if (key.contains("$lte")) {
                column = key.replace("$lte", "");
                wrapper.le(column, map.get(key));
            } else if (key.contains("$gte")) {
                column = key.replace("$gte", "");
                wrapper.ge(column, map.get(key));
            } else if (key.contains("$lt")) {
                column = key.replace("$lt", "");
                wrapper.lt(column, map.get(key));
            } else if (key.contains("$gt")) {
                column = key.replace("$gt", "");
                wrapper.gt(column, map.get(key));
            }  else if (key.contains("$notNull")) {
                List<String> strings = fetchWord(map.get(key));
                if (strings == null) {
                    return;
                }
                strings.forEach(each -> {
                    wrapper.isNotNull(each);
                });
            } else if (key.contains("$isNull")) {
                List<String> strings = fetchWord(map.get(key));
                if (strings == null) {
                    return;
                }
                strings.forEach(each -> {
                    wrapper.isNull(each);
                });
            }else  if (key.contains("$desc")) {
                wrapper.orderByDesc(fetchWord(map.get(key)));
            }else if(key.contains("$or")){
                wrapper.or();
            }else if(key.contains("$and")){
                wrapper.and(wrapperand->{
                    Map o = (Map) map.get(key);
                    setCriteria((QueryWrapper) wrapperand, o);
                });
            }else if(key.contains(GROUPBY)){
                wrapper.groupBy(map.get(key));
            }else if(key.contains(SELECT)){
                wrapper.select((String) map.get(key));
            }else if(key.contains(APPLY)){
                wrapper.apply((String) map.get(key));
            }else{
                wrapper.eq(key,map.get(key) );
            }
        }
    }




}
