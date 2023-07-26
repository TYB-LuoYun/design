package top.anets.base;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    /**
     * 是否只查一个
     * @return
     */
    private Boolean isOne =false;


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

    public  Associates add(  String  currentField,String  targetField ) {
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

    public <T>  Associates associate(Fields.SFunction<T , ?> resultField, IService targetService,boolean isOne) {

        String id = UUID.randomUUID().toString();
        Associates associate = new Associates();
        associate.setResultField(Fields.name(resultField));
        associate.setTargetService(targetService);
        associate.setId(id);
        if(associates==null){
            associates = new ArrayList<>();
        }
        associate.setIsOne(isOne);
        this.setIsOne(isOne);
        this.associates.add(associate);
        this.setResultField(Fields.name(resultField));
        this.setTargetService(targetService);
        this.id = id;
        return this;
    }


    public <T,T3>  Associates associate(Fields.SFunction<T , ?> resultField, IService targetService,Fields.SFunction<T3 , ?> targetField,boolean isOne) {
        String id = UUID.randomUUID().toString();
        Associates associate = new Associates();
        associate.setResultField(Fields.name(resultField));
        associate.setTargetService(targetService);
        associate.setTargetField(Fields.name(targetField));
        associate.setId(id);
        if(associates==null){
            associates = new ArrayList<>();
        }
        associate.setIsOne(isOne);
        this.setIsOne(isOne);
        this.associates.add(associate);
        this.setResultField(Fields.name(resultField));
        this.setTargetService(targetService);
        this.setTargetField(Fields.name(targetField));
        this.id = id;
        return this;
    }




    public <T,T3>  Associates associate( IService targetService,Fields.SFunction<T3 , ?> targetField,boolean isOne) {
        String id = UUID.randomUUID().toString();
        Associates associate = new Associates();
//        associate.setResultField(Fields.name(resultField));
        associate.setTargetService(targetService);
        associate.setTargetField(Fields.name(targetField));
        associate.setId(id);
        if(associates==null){
            associates = new ArrayList<>();
        }
        associate.setIsOne(isOne);
        this.setIsOne(isOne);
        this.associates.add(associate);
//        this.setResultField(Fields.name(resultField));
        this.setTargetService(targetService);
        this.setTargetField(Fields.name(targetField));
        this.id = id;
        return this;
    }


    public <T,T3>  Associates associate( IService targetService ,boolean isOne) {
        String id = UUID.randomUUID().toString();
        Associates associate = new Associates();
//        associate.setResultField(Fields.name(resultField));
        associate.setTargetService(targetService);
//        associate.setTargetField(Fields.name(targetField));
        associate.setId(id);
        associate.setCustom(true);
        if(associates==null){
            associates = new ArrayList<>();
        }
        associate.setIsOne(isOne);
        this.setIsOne(isOne);
        this.associates.add(associate);
//        this.setResultField(Fields.name(resultField));
        this.setTargetService(targetService);
//        this.setTargetField(Fields.name(targetField));
        this.id = id;
        this.custom = true;



        return this;
    }


    //  关联一个
    public <T>  Associates associateOne(Fields.SFunction<T , ?> resultField, IService targetService) {
        return  associate(resultField,targetService, true);
    }


    public <T,T3>  Associates associateOne(Fields.SFunction<T , ?> resultField, IService targetService,Fields.SFunction<T3 , ?> targetField) {
        return associate(resultField,targetService,targetField, true);
    }




    public <T,T3>  Associates associateOne( IService targetService,Fields.SFunction<T3 , ?> targetField) {
        return associate(targetService,targetField,true );
    }


    public <T,T3>  Associates associateOne( IService targetService ) {
        return associate(targetService, true );
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


@Data
class Entry {
    private String  key;
    private Object  value;
}