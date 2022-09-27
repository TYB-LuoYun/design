package top.anets.modules.bigDataInsert.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ftm
 * @date 2022/9/19 0019 15:45
 */
@Data
public class GeneralTable implements Serializable {
    private String colValue;

    private String colAttr;

    private String colType;

    private String colFrom;

    private Long rowKey;

}