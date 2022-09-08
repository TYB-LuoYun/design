package top.anets.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ftm
 * @Date 2022-09-01 15:16:00
 * @Description Query条件构造器
 */
public class WrapperQuery {

    private static List<String> Exclude = Arrays.asList("current","size","total");

    public  static QueryWrapper query(Object vo){
      Map<String,Object> map =objectToMap(vo);
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
        wrapper.eq(map.get(key)!=null, key, map.get(key));
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

    //java对象转map
    public static Map<String, Object> objectToMap(Object obj)   {
      if (obj == null) {
        return null;
      }
      Map<String, Object> map = new HashMap<String, Object>();
      Field[] declaredFields = obj.getClass().getDeclaredFields();
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

    public  static <T> T from(Object source ,Class<T>   t){
        try {
        T o = t.newInstance();
        BeanUtils.copyProperties(source, o);
        return  o;
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
     }

    }
