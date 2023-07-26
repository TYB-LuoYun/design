package top.anets.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static top.anets.base.WrapperQuery.upperCharToUnderLine;


/**
 * @author ftm
 * @date 2023/3/2 0002 15:35
 */
@Accessors(chain = true)
public class QueryMap extends LinkedHashMap<String,Object> implements Map<String,Object> {

    final private String LIKE = "$like";
    final private String IN = "$in";
    final private String NOTIN = "$notin";
    final private String GT = "$gt";
    final private String LT = "$lt";
    final private String GTE = "$gte";
    final private String LTE = "$lte";
    final private String DESC = "$desc";
    final private String ASC = "$asc";
    final private String ORDER = "$order";
    final private String OR = "$or";
    final private String AND = "$and";



    private Integer orPointer = 0;
    private Integer andPointer = 0;

    private String name;

    public static QueryMap build(){
        return new QueryMap();
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


    public <T> QueryMap or(){
        if(this.size()<=0){
            return this;
        }
        super.put(OR+orPointer,orPointer);
        orPointer=orPointer+1;
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
        List<Field> fieldList = new ArrayList<>();
        int i = 0;
        Class<?> clazz = obj.getClass();
        while (clazz != null && i < 2) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
            i++;
        }
        QueryMap queryMap = new QueryMap();
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









    public static void main(String[] args){
        QueryMap queryMap = new QueryMap();
        queryMap.puts("www", "3").puts("333", 3);
        System.out.println(queryMap);
    }



}
