package top.anets.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ftm
 * @date 2022/10/20 0020 10:31
 */
public class MapUtils {
//    /**
//     * 按照红黑树（Red-Black tree）的 NavigableMap 实现
//     * 按照字母大小排序
//     */
//    public static Map<String, Object> sort(Map<String, Object> map) {
//        if (map == null) {
//            return null;
//        }
//        Map<String, Object> result = new TreeMap<>((Comparator<String>) (o1, o2) -> {
//            return o1.compareTo(o2);
//        });
//        result.putAll(map);
//        return result;
//    }


    public static <T> T map2ObjIgnoreCase(Map<String, Object> map, Class<T> clz) {
        try {
            T obj = clz.newInstance();
            Field[] entityFields = obj.getClass().getDeclaredFields();
            for (Field field : entityFields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                for(Map.Entry<String,Object>  eve: map.entrySet()){
                    if(field.getName().equalsIgnoreCase(eve.getKey())){
                        try {
                            if(eve.getValue()==null){
                                continue;
                            }
                            if(field.getType()  == String.class){
                                field.set(obj,String.valueOf(eve.getValue()));
                            }else{
                                field.set(obj, eve.getValue());
                            }
                        } catch (Exception e) {
                            try {
                                if(field.getType() == Integer.class){
                                    field.set(obj, Convert.toInt(eve.getValue()));
                                }else
                                if(field.getType() == Long.class){
                                    field.set(obj, Convert.toLong(eve.getValue()));
                                }else
                                if(field.getType() == Boolean.class){
                                    field.set(obj, Convert.toBool(eve.getValue()));
                                }
                            }catch (Exception e1){

                            }
                        }
                    }
                }

            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 组合参数
     *
     * @param map
     * @return 如：key1Value1Key2Value2....
     */
    public static String groupStringParam(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> item : map.entrySet()) {
            if (item.getValue() != null) {
                sb.append(item.getKey());
                if (item.getValue() instanceof List) {
                    sb.append(JSON.toJSONString(item.getValue()));
                } else {
                    sb.append(item.getValue());
                }
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
    }
}
