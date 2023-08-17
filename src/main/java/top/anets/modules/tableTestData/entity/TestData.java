package top.anets.modules.tableTestData.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.Version;
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
 * @since 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_test_data")
@ApiModel(value="TestData对象", description="")
public class TestData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private BigDecimal balance;


//    @Version
//    private int version; // 乐观锁版本号

}
