package top.anets.base;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author ftm
 * @date 2024-04-11 16:15
 */
public interface BaseService<T> extends IService<T> {
    default boolean checkIsExist(T t, Fields.SFunction<T, ?>... fields) {
        List<Fields.SFunction<T, ?>> sFunctions = Arrays.asList(fields);
        QueryWrapper  query = new QueryWrapper();
        for(Fields.SFunction<T, ?> item: sFunctions){
            query.eq(Fields.name(item), ReflectUtil.getFieldValue(t,Fields.getField(item)));
        }
        long count = this.count(query);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

    default void checkIsExistThrow(T t, Fields.SFunction<T, ?>... fields) {
        if(this.checkIsExist(t,fields)){
            List<Fields.SFunction<T, ?>> sFunctions = Arrays.asList(fields);
            String tip = "";
            for(int i = 0 ; i<  sFunctions.size();i++){
                if(i==sFunctions.size()-1){
                    tip+=Fields.name(sFunctions.get(i));
                }else{
                    tip+=Fields.name(sFunctions.get(i))+"-";
                }
            }
            throw new RuntimeException(tip+"值不能重复");
        }
    }


    @SneakyThrows
    default void  checkForbidChange(T t, Fields.SFunction<T, ?>... fields){
        List<Fields.SFunction<T, ?>> sFunctions = Arrays.asList(fields);
        Field[] declaredFields = t.getClass().getDeclaredFields();
        T origin = null;
        for(Field item : declaredFields){
            item.setAccessible(true);
            TableId annotation = item.getAnnotation(TableId.class);
            if(annotation!=null){
                origin = this.getById((Serializable) item.get(t));
                break;
            }
        }
        if(origin==null){
            return;
        }

        for(Fields.SFunction<T, ?> item: sFunctions){
            Field field = Fields.getField(item);
            Object fieldValue = ReflectUtil.getFieldValue(t,field );
            if(fieldValue == null){
                continue;
            }
            Object fieldValueOld = ReflectUtil.getFieldValue(origin, field);
            if(!fieldValueOld.equals(fieldValue)){
                throw new RuntimeException(Fields.name(item)+"值禁止修改");
            }
        }
    }
}
