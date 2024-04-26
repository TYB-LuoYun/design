package top.anets.modules.verify.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ftm
 * @date 2023-10-25 13:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RMDResult {
    @JsonProperty("Result")
    private Integer Result;
    @JsonProperty("Message")
    private String Message;
    @JsonProperty("Data")
    private Object Data;

    public    RMDResult(RMDResultEnum rmdResultEnum , Object data){
       this.Result = rmdResultEnum.getCode();
       this.Message = rmdResultEnum.getDesc();
       this.Data  = data;
    }


    public static RMDResult success(){
        return new RMDResult(RMDResultEnum.SUCCESS,null);
    }


    public static RMDResult success(Object data){
        return new RMDResult(RMDResultEnum.SUCCESS,data);
    }

    public static RMDResult error(Object data){
        return new RMDResult(RMDResultEnum.SYSTEM_ERROR ,data );
    }
    public static RMDResult error(Integer code ,String msg){
        return new RMDResult(code,msg ,null );
    }
    public static RMDResult error(String msg  ){
        return new RMDResult(RMDResultEnum.CUSTOM_ERROR.getCode(),msg ,null );
    }

    public static RMDResult error(RMDResultEnum rmdResultEnum ,Object data){
        return new RMDResult(rmdResultEnum ,data );
    }
    public static RMDResult error(RMDResultEnum rmdResultEnum ){
        return new RMDResult(rmdResultEnum , null );
    }

}
