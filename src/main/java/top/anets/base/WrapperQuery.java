package top.anets.base;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.anets.modules.system.vo.SysMenuVo;

import java.io.File;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ftm
 * @Date 2022-10-11 12:58:41
 * @Description Query条件构造器
 */
@Data
public class WrapperQuery<S1>   {


    private static List<String> Exclude = Arrays.asList("current","size","total","serialVersionUID");



    public  static  QueryWrapper   query(Object vo){
       return query(objectToMap(vo));
    }

    public  static  QueryWrapper   query(Map<String,Object> map){
    if(map==null){
    return new QueryWrapper();
    }
    QueryWrapper wrapper = new QueryWrapper();
    map.entrySet().forEach(item->{
    String key = item.getKey();//字段名
    if(Exclude.contains(key)){
    return;
    }
    if(map.get(key)==null || map.get(key)==""){
    return;
    }
    String column="";
    if(key.contains("$like")){
    column=key.replace("$like", "");
    wrapper.like(column, map.get(key));
    }else if(key.contains("$lt")){
    column=key.replace("$lt", "");
    wrapper.lt(column, map.get(key));
    }else if(key.contains("$gt")){
    column=key.replace("$gt", "");
    wrapper.gt(column, map.get(key));
    }else if(key.contains("$desc")){
    wrapper.orderByDesc(fetchWord( map.get(key)));
    }else if(key.contains("$notNull")){
    List<String> strings = fetchWord(map.get(key));
        if(strings == null){
        return;
        }
        strings.forEach(each->{
        wrapper.isNotNull(each);
        });
        }else if(key.contains("$isNull")){
        List<String> strings = fetchWord(map.get(key));
            if(strings == null){
            return;
            }
            strings.forEach(each->{
            wrapper.isNull(each);
            });
            }else if(key.contains("$")){
    //               特殊字段什么都不用做
    }else {
    wrapper.eq(map.get(key)!=null, key, map.get(key));
    }



    });
    return  wrapper;
    }

    public static IPage page(Map<String, Object> params) {
        Long current = (Long) params.get("current");
        Long size = (Long) params.get("size");
        if(current==null||size==null){
            current=1L;
            size=Long.MAX_VALUE;
        }
        IPage page = new Page<>(current, size);
        return page;
    }


    /**
     * 拷贝实体到vo
     * @param source
     * @param t
     * @param <T>
     * @return
     */
    public  static <T> T from(Object source ,Class<T>   t){
        if(source == null){
            return null;
        }
        try {
            T o = t.newInstance();
            if(source instanceof Map){
                Map<String,Object> sourceMap = (Map<String, Object>) source;
                T t1 = map2Obj(sourceMap, t);
                return t1;
            }else{
                BeanUtils.copyProperties(source, o);
            }
            return  o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> QueryWrapper<T> parse(Map<String, Object> params, Class<T> classz) {
        T t = map2Obj(params, classz);
        QueryWrapper<T> query =  WrapperQuery.query(t);
        return  query;
    }



    //java对象转map
    public static Map<String, Object> objectToMap(Object obj)   {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields =getAllDeclaredFields(obj.getClass());
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    public static <T> T map2Obj(Map<String,Object> map,Class<T> clz)  {
        try {
            T obj = clz.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for(Field field:declaredFields){
                int mod = field.getModifiers();
                if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
            return obj;
        }catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }


     public static <T>  IPage<T> ipage(IPage pages,Class<T> t) {
                        List<T> list = new LinkedList<>();
                        pages.getRecords().forEach(item->{
                            list.add(WrapperQuery.from(item, t));
                        });
                        pages.setRecords(list);
                        return pages;
      }



    public static  <T>  IPage<T> ipage(IPage   pages,Class<T> t, Associates  associate) {
        List<T> list = new LinkedList<>();
        List<List<Object>> array=new ArrayList<>();
        pages.getRecords().forEach(item1->{
            T item = associateWrapper(item1,t,associate,null,null);
            list.add(item);
        });
        pages.setRecords(list);
        return pages;
    }


    private   IPage<S1>  pages;



    private  Map<Integer,List<Object>> associateMap;

    public  static   < S1>  WrapperQuery<S1>  wpage(IPage   pages,Class<S1> m, Associates  associate) {
        List<S1> list = new LinkedList<>();
        List<List<Object>> array=new ArrayList<>();
        Map<Integer, List<Object>> map = new HashMap<>();
        AtomicInteger index = new AtomicInteger(0);
        pages.getRecords().forEach(item1->{
            S1 item = associateWrapper(item1,m,associate,map,index.intValue());
            list.add(item);
            index.incrementAndGet();
        });
        pages.setRecords(list);
        WrapperQuery<S1> wrapperQuery = new WrapperQuery();
        wrapperQuery.setPages(pages);
        wrapperQuery.associateMap = map;
        return  wrapperQuery  ;
    }

    public static  < T>  WrapperQuery<T>  wpage(IPage pages, Class<T> t) {
        List<T> list = new LinkedList<>();
        pages.getRecords().forEach(item->{
            list.add(WrapperQuery.from(item, t));
        });
        pages.setRecords(list);
        WrapperQuery<T> wrapperQuery = new WrapperQuery();
        wrapperQuery.setPages(pages);
        return  wrapperQuery  ;
    }


    public   void  forEach(FunctionZero<S1> item){
         if(this.pages.getRecords()==null){
             return;
         }
        this.pages.getRecords().forEach(item1->{
        item.accept(item1);
    });
    }



    public void forEach(FunctionResults<S1> item) {
        if(this.pages.getRecords()==null){
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        this.pages.getRecords().forEach(item1->{
            List<Object> result = null;
            if(this.associateMap!=null){
                result = this.associateMap.get(index.intValue());
            }
            item.accept(item1,result);

        });
    }





    private static <T> T associateWrapper(Object item1, Class<T> t, Associates associate,Map<Integer, List<Object>> associateMap,Integer index) {
        List<Associates> associates = associate.getAssociates();
        T item = WrapperQuery.from(item1, t);
//            Map<String, Object> item = objectToMap(item1);

        List<Object> results = new ArrayList<>();
        associates.forEach(each->{
            QueryWrapper<Object> wrapper = new QueryWrapper<>();
            List<AssociateFields> fields = each.getAssociateFields();
            if(fields == null){
//                    for (int i=0;i<fields.size();i++ ) {
//                        AssociateFields one = fields.get(i);
//                        Object o = item.get(one.getCurrentField());
//                        if(array.size()<=i){
//                            array.add(new ArrayList<>());
//                        }
//                        array.get(i).add(o);
//                    }
            }else{
                for (int i=0;i<fields.size();i++ ) {
                    AssociateFields one = fields.get(i);
                    wrapper.eq(one.getTargetField(),  getFieldValue(item, one.getCurrentField()));
                }
                List result = each.getTargetService().list(wrapper);
                Object finalResult = null;
//                  封装结果
                String resultField = each.getResultField();
//                Object resultObject = each.getResult();
                String targetField = each.getTargetField();
//                    Object o = getFieldValue(item,resultField );
                if(java.util.List.class == getFieldType(item,resultField )){
//                        item.put(resultField, result);
                    setFieldValue(item,resultField,result);
                }else{
                    if(result==null||result.size()<=0){
                        return;
                    }
                    if(targetField==null){
                        finalResult = result.get(0);
                    }else{
                        finalResult = objectToMap(result.get(0)).get(targetField);
                    }
                    if(true){

//                        each.setResult(finalResult);
                        results.add(finalResult);
                        if(index!=null){
                            associateMap.put(index, results);
                        }
//                        each.setRecord(item1);
//                        each.getConsumer().accept(finalResult);
                    }
                    if(resultField!=null){
//                           item.put(resultField, finalResult);
                        setFieldValue(item,resultField ,finalResult );
                    }
                }

            }

        });
        return item;

    }


    /**
     * 判断list泛型是否一致
     * @param
     * @return
     */
    private static  boolean isListGenericTypeSame(List item1, List item2) {
        Type type = item1.getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];

        Type type2 = item2.getClass().getGenericSuperclass();
        Type trueType2 = ((ParameterizedType) type2).getActualTypeArguments()[0];

        if(trueType == trueType2){
            return true;
        }else{
            return false;
        }

    }

    private static  Class getFieldType(Object item, String field) {
        if(field==null||item==null){
            return null;
        }
        try {
            Field f = item.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.getType() ;
        }catch (Exception e){
            try {
                Field f = item.getClass().getSuperclass().getDeclaredField(field);
                f.setAccessible(true);
                return f.getType();
            }catch (Exception e1){
                e.printStackTrace();
                e1.printStackTrace();
            }
        }
        return null;
    }

    private static void setFieldValue(Object item, String field, Object result) {
       try {
           Field f = item.getClass().getDeclaredField(field);

//         集合的话判断类型是否一致
           if(java.util.List.class == f.getType()){
//               Type genericType = f.getGenericType();
               // 如果是泛型参数的类型
//               if(genericType instanceof ParameterizedType){
//                   ParameterizedType pt = (ParameterizedType) genericType;
//                   //得到泛型里的class类型对象
//                   Class<?> genericClazz = (Class<?>)pt.getActualTypeArguments()[0];
//                   System.out.println(genericClazz);
//               }
//
//
//               Type genericSuperclass = result.getClass();
//
//               // 如果是泛型参数的类型
//               if(genericSuperclass instanceof ParameterizedType){
//                   ParameterizedType pt1 = (ParameterizedType) genericSuperclass;
//                   //得到泛型里的class类型对象
//                   Class<?> genericClazz1 = (Class<?>)pt1.getActualTypeArguments()[0];
//                   System.out.println(genericClazz1);
//               }
           }

           f.setAccessible(true);
           f.set(item, result);
       }catch (Exception e){

           try {
               Field f = item.getClass().getSuperclass().getDeclaredField(field);

               f.setAccessible(true);
               f.set(item, result);
           }catch (Exception e1){
               e.printStackTrace();
               e1.printStackTrace();
           }
       }

    }


    // 静态工具方法 获取对象的属性值
    public static Object getFieldValue(Object entity, String field) {
        Object r = null;
        try {
            // 类对象反射，获取到指定字段对象反射对象
            Field f = entity.getClass().getDeclaredField(field);
            // true表示反射的对象应禁止Java语言访问使用时进行检查。值false表示所反射的对象应强制执行Java语言访问检查。
            f.setAccessible(true);
            r = f.get(entity);
        } catch (Exception e) {
            try {
                Field f = entity.getClass().getSuperclass().getDeclaredField(field);
                f.setAccessible(true);
                r = f.get(entity);
            }catch (Exception e1){
                e1.printStackTrace();
                e.printStackTrace();
            }
        }
        return r;
    }



    public static List<String>  fetchWord(Object str){
                                if(str instanceof  String){
                                List<String> strs = new ArrayList<String>();
                                    Pattern p = Pattern.compile("[a-zA-Z0-9\\u4e00-\\u9fa5]+");
                                    Matcher m = p.matcher((String)str);
                                    while(m.find()) {
                                    strs.add(m.group());
                                    }
                                    return strs;
                                    }
                                    return (List<String>) str;
                                        }




                                        private static Field[] getAllDeclaredFields(Class<?> clazz) {

                                            List<Field> fieldList = new ArrayList<>();
                                            //      最多只遍历2层
                                            int i=0;
                                            while (clazz != null&&i<2){
                                               fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
                                                clazz = clazz.getSuperclass();
                                                i++;
                                            }
                                            Field[] fields = new Field[fieldList.size()];
                                            return fieldList.toArray(fields);
                                            }



      public static void main(String[] args){

      }




    Associates associates ;
    public <T1,T2> WrapperQuery add(  Fields.SFunction<T1, ?>  currentField,Fields.SFunction<T2, ?>  targetField ) {
        this.associates = associates.add(Fields.name(currentField), Fields.name(targetField));
        return this;
    }

    public <T>  WrapperQuery associate(Fields.SFunction<T , ?> resultField, IService targetService) {
        if(associates == null){
            associates=Associates.build();
        }
        this.associates = associates.associate(resultField, targetService);
        return this;
    }


    public <T,T3>  WrapperQuery associate(Fields.SFunction<T , ?> resultField, IService targetService,Fields.SFunction<T3 , ?> targetField) {
        if(associates == null){
            associates=Associates.build();
        }
        this.associates=associates.associate(resultField,targetService,targetField );
        return this;
    }




    public <T,T3>  WrapperQuery associate( IService targetService,Fields.SFunction<T3 , ?> targetField) {
        if(associates == null){
            associates=Associates.build();
        }
        this.associates=associates.associate( targetService,targetField );
        return this;
    }


    public <T,T3>  WrapperQuery associate( IService targetService ) {
        if(associates == null){
            associates=Associates.build();
        }
        this.associates=associates.associate( targetService);
        return this;

    }





}
