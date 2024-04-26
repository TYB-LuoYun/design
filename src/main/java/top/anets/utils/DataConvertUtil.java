package top.anets.utils;



import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ftm
 * @date 2024-03-14 13:26
 */
public class DataConvertUtil extends Convert{
    public  static <T> List<T> jsonArrayToList(JSONArray jsonArray, Class<T> classz){
        List list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T t = jsonObject.toJavaObject(classz);
            list.add(t);
        }
        return list;
    }
    public  static <T> List<T> jsonArrayToList(JSONArray jsonArray ){
        List list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i));
        }
        return list;
    }



    public static <T> T objectToObject(Object source, Class<T> t) {
        if (source == null) {
            return null;
        }
        try {
            T obj = t.newInstance();
            Field[] entityFields = obj.getClass().getDeclaredFields();
            if (source instanceof Map) {
                Map<String, Object> sourceMap = (Map<String, Object>) source;
                T t1 = map2Obj(sourceMap, t);
                return t1;
            }

            Field[] sfields = source.getClass().getDeclaredFields();
            for (Field field : entityFields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                for(Field  eve: sfields){
                    eve.setAccessible(true);
                    if(field.getName().equalsIgnoreCase(eve.getName())){
                        try {
                            if(eve.get(source)==null){
                                continue;
                            }
                            if(field.getType()  == String.class){
                                field.set(obj,String.valueOf(eve.get(source)));
                            }else if(field.getType() == Integer.class){
                                field.set(obj, Convert.toInt(eve.get(source)));
                            }else if(field.getType() == Long.class){
                                field.set(obj, Convert.toLong(eve.get(source)));
                            }else
                            if(field.getType() == Boolean.class){
                                field.set(obj, Convert.toBool(eve.get(source)));
                            }else{
                                field.set(obj, eve.get(source));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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




    public static <T> T map2Obj(Map<String, Object> map, Class<T> clz) {
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
}
