package top.anets.base;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.anets.modules.system.entity.SysMenu;

import java.lang.reflect.*;
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
public class WrapperQuery<S1> {
    private static List<String> Exclude = Arrays.asList("current", "size", "total", "serialVersionUID","serial_version_u_i_d");
    private static String TimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static QueryWrapper query(Object vo) {
        return query(objectToColumnMap(vo));
    }
    public static QueryWrapper query(Map<String, Object> map) {
        if (map == null) {
            return new QueryWrapper();
        }
        QueryWrapper wrapper =new QueryWrapper();
        setCriteria(wrapper,map );
        return wrapper;
    }





    public static   void setCriteria(QueryWrapper wrapper  , Map<String, Object> map){
        for (Map.Entry<String,Object> item: map.entrySet()) {
            String key = item.getKey();//字段名
            if (Exclude.contains(key)) {
                return;
            }
            if (map.get(key) == null || map.get(key) == "") {
                return;
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
            }else if (key.contains("$asc")) {
                wrapper.orderByAsc(fetchWord(map.get(key)));
            }  else if (key.contains("$order")) {
                wrapper.orderByAsc(fetchWord(map.get(key)));
            }else if(key.contains("$or")){
                wrapper.or();
            }else if(key.contains("$and")){
                wrapper.and(wrapperand->{
                    Map o = (Map) map.get(key);
                    setCriteria((QueryWrapper) wrapperand, o);
                });
            }else{
                wrapper.eq(key,map.get(key) );
            }
        }


    }

    public static IPage page(Map<String, Object> params) {
        Long current = (Long) params.get("current");
        Long size = (Long) params.get("size");
        if (current == null || size == null) {
            current = 1L;
            size = Long.MAX_VALUE;
        }
        IPage page = new Page<>(current, size);
        return page;
    }




    /**
     * 拷贝实体到vo
     *
     * @param source
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T from(Object source, Class<T> t) {
        if (source == null) {
            return null;
        }
        try {
            T o = t.newInstance();
            if (source instanceof Map) {
                Map<String, Object> sourceMap = (Map<String, Object>) source;
                T t1 = map2Obj(sourceMap, t);
                return t1;
            } else {
                BeanUtils.copyProperties(source, o);
            }
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public static void copyPropertiesToNullFields(Object source, Object target)  {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();
        try {
            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true);
                Object sourceValue = sourceField.get(source);
                for (Field targetField : targetFields) {
                    targetField.setAccessible(true);
                    if (sourceField.getName().equals(targetField.getName()) && ObjectUtils.isEmpty(targetField.get(target))) {
                        targetField.set(target, sourceValue);
                        break;
                    }
                }
            }
        }catch (Exception e){

        }
    }

    public static <T> QueryWrapper<T> parse(Map<String, Object> params, Class<T> classz) {
        T t = map2Obj(params, classz);
        QueryWrapper<T> query = WrapperQuery.query(t);
        return query;
    }



    public static Map<String, Object> objectToColumnMap(Object obj){
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = getAllDeclaredFields(obj.getClass());
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                TableField annotation = field.getAnnotation(TableField.class);
                if(annotation!=null){
                    map.put(annotation.value(), field.get(obj));
                }else{
                    map.put(upperCharToUnderLine(field.getName()), field.get(obj));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }




    public static String upperCharToUnderLine(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }


    public static String underline2Camel(String line, boolean... smallCamel) {
        if (org.apache.commons.lang3.StringUtils.isBlank(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        // 匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            // 当是true 或则是空的情况
            if ((smallCamel.length == 0 || smallCamel[0]) && matcher.start() == 0) {
                sb.append(Character.toLowerCase(word.charAt(0)));
            } else {
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    //java对象转map
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = getAllDeclaredFields(obj.getClass());
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


    //java对象转map
    public static Map<String, Object> objectToMap(Object... objs) {
        if(objs == null||objs.length<=0){
            return null;
        }
        Map<String, Object> mapAll = new HashMap<>();
        for(int i=0;i<objs.length;i++){
            Map<String, Object> map = objectToMap(objs[i]);
            if(map!=null){
                mapAll.putAll(map);
            }
        }
        return mapAll;
    }



    public static <T> T map2Obj(Map<String, Object> map, Class<T> clz) {
        try {
            T obj = clz.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                if(field.getType()  == String.class){
                    field.set(obj,String.valueOf(map.get(field.getName())));
                }else if(field.getType() == Integer.class){
                    field.set(obj, Convert.toInt(map.get(field.getName())));
                }else if(field.getType() == Long.class){
                    field.set(obj, Convert.toLong(map.get(field.getName())));
                }else if(field.getType() == Boolean.class){
                    field.set(obj, Convert.toBool(map.get(field.getName())));
                }else{
                    field.set(obj, map.get(field.getName()));
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> IPage<T> ipage(IPage pages, Class<T> t) {
        List<T> list = new LinkedList<>();
        pages.getRecords().forEach(item -> {
            list.add(WrapperQuery.from(item, t));
        });
        pages.setRecords(list);
        return pages;
    }


    /**
     * 分页查询页
     */
    private IPage<S1> iPage;


    /**
     * 自动关联结果封装
     */
    private Map<Integer, List<Object>> associateMap;


    public static <T> WrapperQuery<T> wpage(IPage pages, Class<T> t) {
        List<T> list = new LinkedList<>();
        pages.getRecords().forEach(item -> {
            list.add(WrapperQuery.from(item, t));
        });
        pages.setRecords(list);
        WrapperQuery<T> wrapperQuery = new WrapperQuery();
        wrapperQuery.setIPage(pages);
        return wrapperQuery;
    }

    public static <T> List<T>  query(IService iService, Wrapper<T> queryWrapper){
        if(queryWrapper== null || queryWrapper.isEmptyOfWhere()){
//          避免内存溢出
            return null;
        }
        return  iService.list(queryWrapper);
    }

    public static <T> IPage<T>  queryPage(IService iService, Wrapper<T> queryWrapper,IPage iPage){
        if(queryWrapper== null || queryWrapper.isEmptyOfWhere()){
//          避免内存溢出
            return null;
        }
        return  iService.page(iPage,queryWrapper );
    }

    public static <T> T queryOne(IService iService, Wrapper<T> queryWrapper){
        return  queryOne(iService,queryWrapper ,false );
    }




    public static <T> T queryOne(IService iService, Wrapper queryWrapper, boolean throwEx) {
        if(queryWrapper== null || queryWrapper.isEmptyOfWhere()){
            //          避免全表扫描
            return null;
        }

        if(throwEx == true){
            List<T> list = iService.list(queryWrapper);
            if(list==null||list.size()==0){
                return null;
            }
            if(list.size()>1){
                throw new RuntimeException("get too many results:"+list.size());
            }
            return list.get(0);
        }else{
//          单个查询避免全表扫描，采用分页，此分页不用计算总数
            Page ipage = new Page<>(1, 1);
            ipage.setSearchCount(false);
            IPage<T> page = iService.page(ipage, queryWrapper);
            if(page.getRecords()==null||page.getRecords().size()<=0){
                return null;
            }
            return page.getRecords().get(0);
        }
    }




    public void forEach(Functions.FunctionZero<S1> item) {
        if (this.iPage.getRecords() == null) {
            return;
        }
        this.iPage.getRecords().forEach(item1 -> {
            item.accept(item1);
        });
    }


    public static Method getMethod(Class<?> objClass, String methodName) throws NoSuchMethodException {
        for (Method method : objClass.getDeclaredMethods()) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    public static Object invoke(Object obj, String methodName, Object... args) throws ReflectiveOperationException {
        Field overrideField = AccessibleObject.class.getDeclaredField("override");
        overrideField.setAccessible(true);
        Method targetMethod = getMethod(obj.getClass(), methodName);
        overrideField.set(targetMethod, true);
        return targetMethod.invoke(obj, args);
    }

    public static Class<?> getConsumerLambdaParameterType(Object consumer, int index) throws ReflectiveOperationException {
        Class<?> consumerClass = consumer.getClass();
        Object constantPool = invoke(consumerClass, "getConstantPool");
        for (int i = (int) invoke(constantPool, "getSize") - 1; i >= 0; --i) {
            try {
                Member member = (Member) invoke(constantPool, "getMethodAt", i);
                if (member instanceof Method && member.getDeclaringClass() != Object.class) {
                    return ((Method) member).getParameterTypes()[index];
                }
            } catch (Exception ignored) {
                // ignored
            }
        }
        throw new NoSuchMethodException();
    }


    public Type getLambdaParameterType(Object object, int index) {
        Type type = object.getClass().getGenericInterfaces()[0];
        Type r1Clazz;
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[1];
        } else if (object.getClass().isSynthetic()) {
            try {
                return getConsumerLambdaParameterType(object, index);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("not ParameterizedType");
        }
        return null;
    }


    public <R1> void forEach(Functions.FunctionOneResult<S1, R1> item) {
        if (this.iPage.getRecords() == null) {
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        this.iPage.getRecords().forEach(item1 -> {
            R1 result = null;
            result = (R1) this.getResultFromAssociateMap(item, index.intValue(), 1);
            item.accept(item1, result);
            index.incrementAndGet();
        });
    }


    public <R1, R2> void forEach(Functions.FunctionTwoResult<S1, R1, R2> item) {
        if (this.iPage.getRecords() == null) {
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        this.iPage.getRecords().forEach(item1 -> {
            R1 result1 = null;
            R2 result2 = null;
            result1 = (R1) this.getResultFromAssociateMap(item, index.intValue(), 1);
            result2 = (R2) this.getResultFromAssociateMap(item, index.intValue(), 2);
            item.accept(item1, result1, result2);
            index.incrementAndGet();
        });
    }


    public <R1, R2, R3> void forEach(Functions.FunctionThreeResult<S1, R1, R2, R3> item) {
        if (this.iPage.getRecords() == null) {
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        this.iPage.getRecords().forEach(item1 -> {
            R1 result1 = null;
            R2 result2 = null;
            R3 result3 = null;
            result1 = (R1) this.getResultFromAssociateMap(item, index.intValue(), 1);
            result2 = (R2) this.getResultFromAssociateMap(item, index.intValue(), 2);
            result3 = (R3) this.getResultFromAssociateMap(item, index.intValue(), 3);
            item.accept(item1, result1, result2, result3);
            index.incrementAndGet();
        });
    }


    public <R1, R2, R3, R4> void forEach(Functions.FunctionFourResult<S1, R1, R2, R3, R4> item) {
        if (this.iPage.getRecords() == null) {
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        this.iPage.getRecords().forEach(item1 -> {
            R1 result1 = null;
            R2 result2 = null;
            R3 result3 = null;
            R4 result4 = null;
            result1 = (R1) this.getResultFromAssociateMap(item, index.intValue(), 1);
            result2 = (R2) this.getResultFromAssociateMap(item, index.intValue(), 2);
            result3 = (R3) this.getResultFromAssociateMap(item, index.intValue(), 3);
            result4 = (R4) this.getResultFromAssociateMap(item, index.intValue(), 4);
            item.accept(item1, result1, result2, result3, result4);
            index.incrementAndGet();
        });
    }


    public <R1, R2, R3, R4, R5> void forEach(Functions.FunctionFiveResult<S1, R1, R2, R3, R4, R5> item) {
        if (this.iPage.getRecords() == null) {
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        this.iPage.getRecords().forEach(item1 -> {
            R1 result1 = null;
            R2 result2 = null;
            R3 result3 = null;
            R4 result4 = null;
            R5 result5 = null;
            result1 = (R1) this.getResultFromAssociateMap(item, index.intValue(), 1);
            result2 = (R2) this.getResultFromAssociateMap(item, index.intValue(), 2);
            result3 = (R3) this.getResultFromAssociateMap(item, index.intValue(), 3);
            result4 = (R4) this.getResultFromAssociateMap(item, index.intValue(), 4);
            result5 = (R5) this.getResultFromAssociateMap(item, index.intValue(), 5);
            item.accept(item1, result1, result2, result3, result4, result5);
            index.incrementAndGet();
        });
    }


    public <R1, R2, R3, R4, R5, R6> void forEach(Functions.FunctionResults<S1, R1, R2, R3, R4, R5, R6> item) {
        if (this.iPage.getRecords() == null) {
            return;
        }
        AtomicInteger index = new AtomicInteger(0);
        this.iPage.getRecords().forEach(item1 -> {
            R1 result1 = null;
            R2 result2 = null;
            R3 result3 = null;
            R4 result4 = null;
            R5 result5 = null;
            R6 result6 = null;
            result1 = (R1) this.getResultFromAssociateMap(item, index.intValue(), 1);
            result2 = (R2) this.getResultFromAssociateMap(item, index.intValue(), 2);
            result3 = (R3) this.getResultFromAssociateMap(item, index.intValue(), 3);
            result4 = (R4) this.getResultFromAssociateMap(item, index.intValue(), 4);
            result5 = (R5) this.getResultFromAssociateMap(item, index.intValue(), 5);
            result6 = (R6) this.associateMap.get(index.intValue()).get(0);
            item.accept(item1, result1, result2, result3, result4, result5, result6);
            index.incrementAndGet();
        });
    }


    private Object getResultFromAssociateMap(Object function, int dataIndex, int functionParamIndex) {
        /**
         * 判断接受类型是集合还是对象
         */
        Type r1Type = getLambdaParameterType(function, functionParamIndex);
        boolean isList = List.class.isAssignableFrom((Class<?>) r1Type);
        Object result = null;
        try {
            if (this.associateMap == null || this.associateMap.get(dataIndex) == null) {
                return null;
            }
            List<Object> o = (List<Object>) this.associateMap.get(dataIndex).get(0);
            if (isList == true) {
                result = o;
            } else {
                if (o == null || o.size() <= 0) {
                    result = null;
                } else {
                    result = o.get(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 1结论：对于索引字段or或者in的效率基本一致，非索引字段in的效率优于or
     * （1）or的效率为O(n)，
     * （2）in的效率为O(logn)，当n越大的时候效率相差越明显。
     * <p>
     * <p>
     * 2or 一次查询 还是 多次查询 哪个效率高
     * 当然是一条语句效率高
     * 因为一条语句 也就意味着只对该表中的数据执行一次从头到尾的查询
     * 三条语句的话，要对该表数据执行三次查询
     *
     *
     * 3in和or 不会破坏索引（百万测试），in比 union all效率高
     *
     * 4在查询1条的时候，
     * @param associate
     * @param associateMap
     * @param <T>
     */

    private static <T> void associateWrapper(List<T> records, Associates associate, Map<Integer, List<Object>> associateMap, boolean enableSmart,List<String> limitFields) {
        List<Associates> associates = associate.getAssociates();
        if (enableSmart == false) {
            AtomicInteger index = new AtomicInteger(0);
            records.forEach(item -> {
                associates.forEach(each -> {
                    QueryWrapper<Object> wrapper = new QueryWrapper<>();
                    List<AssociateFields> fields = each.getAssociateFields();
                    if (fields == null || fields.size() <= 0) {
                        return;
                    }
                    for (int i = 0; i < fields.size(); i++) {
                        AssociateFields one = fields.get(i);
                        Object fieldValue = getFieldValue(item, one.getCurrentField());
                        wrapper.eq(one.getTargetField(), fieldValue);
                    }
                    List result = listQuery(each,wrapper,null);
                    wrapperAssociateResult(item, each, result, associateMap, index.intValue());
                });
                index.incrementAndGet();
            });
        } else {
//          启用智能查询
            List<Map<String, Map<String, Object>>> listOr = new ArrayList<>();
////          封装多个查询条件
            records.forEach(item -> {
                Map<String, Map<String, Object>> mapAnd = new HashMap<>();
                associates.forEach(each -> {
                    List<AssociateFields> fields = each.getAssociateFields();
                    if (fields == null || fields.size() <= 0) {
                        return;
                    }
                    Map<String, Object> paramAssociateMaps = new HashMap<>();
                    if (mapAnd.get(each.getId()) != null) {
                        paramAssociateMaps = mapAnd.get(each.getId());
                    }
                    for (int i = 0; i < fields.size(); i++) {
                        AssociateFields one = fields.get(i);
//                        wrapper.eq(one.getTargetField(),  getFieldValue(item, one.getCurrentField()));
                        paramAssociateMaps.put(one.getTargetField(), getFieldValue(item, one.getCurrentField()));
                    }
                    mapAnd.put(each.getId(), paramAssociateMaps);
                });
                listOr.add(mapAnd);

            });

//          统一查询
            associates.forEach(each -> {
                QueryWrapper<Object> wrapper = new QueryWrapper<>();
                String fieldTargetOne = null;
                Set<Object> values = new HashSet<>();
                for (Map<String, Map<String, Object>> item : listOr) {
                    Map<String, Object> mapAnd = item.get(each.getId());

                    if (mapAnd == null || mapAnd.size() <= 0) {
                        return;
                    }
                    if (mapAnd.size() == 1) {
                        for (Map.Entry<String, Object> entry : mapAnd.entrySet()) {
                            fieldTargetOne = entry.getKey();
                            values.add(entry.getValue());
                        }
                    } else {
                        for (Map.Entry<String, Object> entry : mapAnd.entrySet()) {
                            String fieldTarget = entry.getKey();
                            Object value = entry.getValue();
                            if (value == null) {
//                                wrapper.isNull(fieldTarget);
                                wrapper.eq(fieldTarget, value);
                            } else {
                                wrapper.eq(fieldTarget, value);
                            }
                        }
//                      对于使用多字段关联，使用 or 查询 比 多次查询效率高
                        wrapper.or();
                    }
                }

                if (fieldTargetOne != null) {
//                  对于只有1个字段的关联，使用in查询效率比or查询高
                    wrapper.in(fieldTargetOne, values);
                }


//              拆解结果
                List<Object> result = listQuery(each,wrapper,limitFields);

                AtomicInteger index = new AtomicInteger(0);
//              从结果中判断它属于哪一结果
                records.forEach(item -> {
                    List<AssociateFields> fields = each.getAssociateFields();
                    if (fields == null || fields.size() <= 0) {
                        return;
                    }
                    /**
                     * 拆解 结果与行 对应
                     */
                    boolean isSame = true;
                    for (int i = 0; i < fields.size(); i++) {
                        AssociateFields one = fields.get(i);
                        for (Object eve : result) {
                            Map<String, Object> map = objectToMap(eve);
                            Object origin = getFieldValue(item, one.getCurrentField());
                            if (origin == null || !origin.equals(map.get(one.getTargetField()))) {
                                isSame = false;
                            }
                        }
                    }
                    if (isSame == true) {
                        wrapperAssociateResult(item, each, result, associateMap, index.intValue());
                    }
                    index.incrementAndGet();
                });


            });


        }


    }

    private static List  listQuery(Associates each, QueryWrapper<Object> wrapper,  List<String> limitFields) {
        if(CollectionUtils.isNotEmpty(limitFields)){
//        指定查询字段
            List<AssociateFields> fields = each.getAssociateFields();
            List<String> strings = new ArrayList<>();
            if(fields!=null){
                fields.forEach(item->{
                    String targetField = item.getTargetField();
                    if(StringUtils.isNotBlank(targetField)){
                        strings.add(targetField);
                    }
                });
            }
            strings.addAll( limitFields);
            String[] objects =  strings.toArray(new String[limitFields.size()]);
            wrapper.select(objects);
        }
        List result = null;
        if(each.getIsOne()==null||each.getIsOne() == false){
            result = each.getTargetService().list(wrapper);
        }else{
//          单个查询，避免全表扫描
            Page ipage = new Page<>(1, 1);
            ipage.setSearchCount(false);
            IPage page = each.getTargetService().page(ipage, wrapper);
            result = page.getRecords();
        }
        return result;
    }

    /**
     * 封装结果
     *
     * @param item         原结果集每一项
     * @param each         每个关联
     * @param result       关联结果
     * @param associateMap 关联结果封装的映射
     * @param index        原结果集每一项 索引
     * @param <T>
     */
    private static <T> void wrapperAssociateResult(T item, Associates each, List result, Map<Integer, List<Object>> associateMap, Integer index) {
        List<Object> results = new ArrayList<>();
        Object finalResult = null;
//                  封装结果
        String resultField = each.getResultField();
        String targetField = each.getTargetField();
        if (resultField == null || List.class == getFieldType(item, resultField)) {
            /**
             * 如果对象字段是list，那么直接把结果赋值给他
             */
            if (resultField != null) {
                setFieldValue(item, resultField, result);
            }
            /**
             * 缓存对象
             */
            if (true) {
                results.add(result);
                if (index != null) {
                    associateMap.put(index, results);
                }
            }
        } else {
            /**
             * 排除空数据
             */
            if (result == null || result.size() <= 0) {
                results.add(null);
                if (index != null) {
                    associateMap.put(index, results);
                }
                return;
            }
            /**
             * 给对象的某字段 封装结果（不是整个对象就是某个字段）
             */
            if (targetField == null) {
                finalResult = result.get(0);
            } else {
                finalResult = objectToMap(result.get(0)).get(targetField);
            }
            if (resultField != null) {
                setFieldValue(item, resultField, finalResult);
            }

            /**
             * 缓存结果
             */
            if (true) {
                results.add(finalResult);
                if (index != null) {
                    associateMap.put(index, results);
                }
            }
        }
    }


    /**
     * 判断list泛型是否一致
     *
     * @param
     * @return
     */
    private static boolean isListGenericTypeSame(List item1, List item2) {
        Type type = item1.getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];

        Type type2 = item2.getClass().getGenericSuperclass();
        Type trueType2 = ((ParameterizedType) type2).getActualTypeArguments()[0];

        if (trueType == trueType2) {
            return true;
        } else {
            return false;
        }

    }

    private static Class getFieldType(Object item, String field) {
        if (field == null || item == null) {
            return null;
        }
        try {
            Field f = item.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.getType();
        } catch (Exception e) {
            try {
                Field f = item.getClass().getSuperclass().getDeclaredField(field);
                f.setAccessible(true);
                return f.getType();
            } catch (Exception e1) {
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
            if (List.class == f.getType()) {
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
        } catch (Exception e) {

            try {
                Field f = item.getClass().getSuperclass().getDeclaredField(field);

                f.setAccessible(true);
                f.set(item, result);
            } catch (Exception e1) {
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
            } catch (Exception e1) {
                e1.printStackTrace();
                e.printStackTrace();
            }
        }
        return r;
    }


    public static List<String> fetchWord(Object str) {
        if (str instanceof String) {
            List<String> strs = new ArrayList<String>();
            Pattern p = Pattern.compile("[^\\s,，]+");
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
        int i = 0;
        while (clazz != null && i < 2) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
            i++;
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }





    Associates associates;

    public <T1, T2> WrapperQuery<S1> add(Fields.SFunction<T1, ?> currentField, Fields.SFunction<T2, ?> targetField) {
        this.associates = associates.add(Fields.name(currentField), Fields.name(targetField));
        return this;
    }


    public <T> WrapperQuery<S1> associate(Fields.SFunction<T, ?> resultField, IService targetService) {
        if (associates == null) {
            associates = Associates.build();
        }
        this.associates = associates.associate(resultField, targetService,false);
        return this;
    }


    public <T, T3> WrapperQuery<S1> associate(Fields.SFunction<T, ?> resultField, IService targetService, Fields.SFunction<T3, ?> targetField) {
        if (associates == null) {
            associates = Associates.build();
        }
        this.associates = associates.associate(resultField, targetService, targetField,false);
        return this;
    }


    public <T, T3> WrapperQuery<S1> associate(IService targetService, Fields.SFunction<T3, ?> targetField) {
        if (associates == null) {
            associates = Associates.build();
        }
        this.associates = associates.associate(targetService, targetField,false);
        return this;
    }


    public <T, T3> WrapperQuery<S1> associate(IService targetService) {
        if (associates == null) {
            associates = Associates.build();
        }
        this.associates = associates.associate(targetService,false);
        return this;

    }



    //    关联一个
    public <T> WrapperQuery<S1> associateOne(Fields.SFunction<T, ?> resultField, IService targetService) {
        if (associates == null) {
            associates = Associates.build();
        }
        this.associates = associates.associateOne(resultField, targetService);
        return this;
    }


    public <T, T3> WrapperQuery<S1> associateOne(Fields.SFunction<T, ?> resultField, IService targetService, Fields.SFunction<T3, ?> targetField) {
        if (associates == null) {
            associates = Associates.build();
        }
        this.associates = associates.associateOne(resultField, targetService, targetField);
        return this;
    }


    public <T, T3> WrapperQuery<S1> associateOne(IService targetService, Fields.SFunction<T3, ?> targetField) {
        if (associates == null) {
            associates = Associates.build();
        }
        this.associates = associates.associateOne(targetService, targetField);
        return this;
    }


    public <T, T3> WrapperQuery<S1> associateOne(IService targetService) {
        if (associates == null) {
            associates = Associates.build();
        }
        this.associates = associates.associateOne(targetService);
        return this;

    }


    public WrapperQuery<S1> fetch(boolean enableSmart) {
        Map<Integer, List<Object>> map = new HashMap<>();
        List<S1> records = this.iPage.getRecords();
        associateWrapper(records, this.associates, map, enableSmart,null);
        this.associateMap = map;
        return this;
    }

    /**
     * 开始关联查询
     *
     * @return
     */
    public WrapperQuery<S1> fetch() {
        return this.fetch(true);
    }

    /**
     * 指定字段关联查询
     */
    public <T2> WrapperQuery<S1> fetch(Fields.SFunction<T2, ?>... limitFields) {
        Map<Integer, List<Object>> map = new HashMap<>();
        List<S1> records = this.iPage.getRecords();
        List<Fields.SFunction<T2, ?>> sFunctions = null;
        if(limitFields!=null){
            sFunctions =  Arrays.asList(limitFields);
        }
        List<String> strings = new ArrayList<>();
        if(sFunctions!=null){
            sFunctions.forEach(each->{
                strings.add(Fields.name(each));
            });
        }
        associateWrapper(records, this.associates, map, true,strings);
        this.associateMap = map;
        return this;
    }
}
