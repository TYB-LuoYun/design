package top.anets.base;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;

import javax.security.auth.callback.CallbackHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author ftm
 * @date 2022/10/11 0011 12:45
 */
@Data
public class Associates  {
    private List<Associates> associates;

    /**
//     * 关联结果
//     */
//    private Object result;
    /**
     * 结果字段
     */
    private String resultField;
    /**
     * 关联需要的目标字段
     */
    private String targetField;
    /**
     * 目标服务
     */
    private IService targetService;
    /**
     * 关联的对应关系
     */
    private List<AssociateFields> associateFields;
    private Consumer<Object> consumer;
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 是否自定义
     */
    private Boolean custom ;


    public static Associates build() {
        return new Associates();
    }

    public <T1,T2> Associates add(  Fields.SFunction<T1, ?>  currentField,Fields.SFunction<T2, ?>  targetField ) {
        associates.forEach(item->{
            if( item.id.equals(this.id)){
                List<AssociateFields> associateFields = item.getAssociateFields();
                if(associateFields==null){
                    associateFields= new ArrayList<>();
                }
                AssociateFields fields = new AssociateFields();
                fields.setCurrentField(Fields.name(currentField));
                fields.setTargetField(Fields.name(targetField));
                associateFields.add(fields);
                item.setAssociateFields(associateFields);
            }
        });
        return this;
    }


    public <T1,T2> Associates add(  String  currentField,String  targetField ) {
        associates.forEach(item->{
            if( item.id.equals(this.id)){
                List<AssociateFields> associateFields = item.getAssociateFields();
                if(associateFields==null){
                    associateFields= new ArrayList<>();
                }
                AssociateFields fields = new AssociateFields();
                fields.setCurrentField( (currentField));
                fields.setTargetField( (targetField));
                associateFields.add(fields);
                item.setAssociateFields(associateFields);
            }
        });
        return this;
    }

    public <T>  Associates associate(Fields.SFunction<T , ?> resultField, IService targetService) {
        String id = new Date().getTime()+"";
        Associates associate = new Associates();
        associate.setResultField(Fields.name(resultField));
        associate.setTargetService(targetService);
        associate.setId(id);
        if(associates==null){
            associates = new ArrayList<>();
        }
        this.associates.add(associate);
        this.setResultField(Fields.name(resultField));
        this.setTargetService(targetService);
        this.id = id;
        return this;
    }


    public <T,T3>  Associates associate(Fields.SFunction<T , ?> resultField, IService targetService,Fields.SFunction<T3 , ?> targetField) {
        String id = new Date().getTime()+"";
        Associates associate = new Associates();
        associate.setResultField(Fields.name(resultField));
        associate.setTargetService(targetService);
        associate.setTargetField(Fields.name(targetField));
        associate.setId(id);
        if(associates==null){
            associates = new ArrayList<>();
        }
        this.associates.add(associate);
        this.setResultField(Fields.name(resultField));
        this.setTargetService(targetService);
        this.setTargetField(Fields.name(targetField));
        this.id = id;
        return this;
    }




    public <T,T3>  Associates associate( IService targetService,Fields.SFunction<T3 , ?> targetField) {
        String id = new Date().getTime()+"";
        Associates associate = new Associates();
//        associate.setResultField(Fields.name(resultField));
        associate.setTargetService(targetService);
        associate.setTargetField(Fields.name(targetField));
        associate.setId(id);
        if(associates==null){
            associates = new ArrayList<>();
        }
        this.associates.add(associate);
//        this.setResultField(Fields.name(resultField));
        this.setTargetService(targetService);
        this.setTargetField(Fields.name(targetField));
        this.id = id;
        return this;
    }


    public <T,T3>  Associates associate( IService targetService ) {



        String id = new Date().getTime()+"";
        Associates associate = new Associates();
//        associate.setResultField(Fields.name(resultField));
        associate.setTargetService(targetService);
//        associate.setTargetField(Fields.name(targetField));
        associate.setId(id);
        associate.setCustom(true);
        if(associates==null){
            associates = new ArrayList<>();
        }
        this.associates.add(associate);
//        this.setResultField(Fields.name(resultField));
        this.setTargetService(targetService);
//        this.setTargetField(Fields.name(targetField));
        this.id = id;
        this.custom = true;



        return this;
    }


//    public <T,T3>  Associates associate(Object result, IService targetService,Fields.SFunction<T3 , ?> targetField) {
//
////      未来消费
//        Consumer<Object> consumer =
//                uploadData -> returnData(uploadData,result);
//        String id = new Date().getTime()+"";
//        Associates associate = new Associates();
//        associate.setResult(result);
//        associate.setTargetService(targetService);
//        associate.setTargetField(Fields.name(targetField));
//        associate.setId(id);
//        associate.setCustom(true);
//        associate.setConsumer(consumer);
//        if(associates==null){
//            associates = new ArrayList<>();
//        }
//        this.associates.add(associate);
//        this.setResult(result);
//        this.setCustom(true);
//        this.setTargetService(targetService);
//
//        this.consumer=consumer;
//        this.id = id;
//        return this;
//    }

    private void returnData(Object uploadData,Object result) {
       result =uploadData;
    }


    public List<Associates> toList() {
        return this.associates;
    }


}


@Data
class AssociateFields {
    private String  currentField;
    private String  targetField;
}