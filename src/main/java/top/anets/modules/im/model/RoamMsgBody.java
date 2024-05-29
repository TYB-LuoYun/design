package top.anets.modules.im.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

@Data
@Document("RoamMsgBody")
@Validated
public class RoamMsgBody {

    @JsonProperty("MsgType")
    private String msgType;

    @JsonProperty("MsgContent")
    private Object msgContent;
}

