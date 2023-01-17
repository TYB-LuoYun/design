package top.anets.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ftm
 * @since 2022-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dict")
@ApiModel(value="Dict对象", description="")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "字典名")
    private String name;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "字典码")
    private String code;

    @ApiModelProperty(value = "字典值")
    private String value;

    private Integer sort;

    private String insetsUrl;

    private String imgUrl;

    @TableField("link_url")
    private String linkUrl;

    private Date updateTime;

    private String parentId;

    private Boolean isDir;

    private Boolean isLeaf;

    private Boolean state;

    private Integer level;

    private Integer deleted;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;


}
