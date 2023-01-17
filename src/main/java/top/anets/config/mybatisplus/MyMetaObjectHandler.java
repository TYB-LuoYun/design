package top.anets.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // 注入bean
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            this.setFieldValByName("update_time", new Date(), metaObject);
        }catch (Exception e){
//            e.printStackTrace();
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            this.setFieldValByName("update_time", new Date(), metaObject);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
