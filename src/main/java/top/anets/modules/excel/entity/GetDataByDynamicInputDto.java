package top.anets.modules.excel.entity;

import lombok.Data;

/**
 * @author ftm
 * @date 2022/9/16 0016 16:25
 */
@Data
public class GetDataByDynamicInputDto {

    private int pageSize;//这个是自定义的分页参数，与easypoi分页无关系

    private int pageNum;

    public GetDataByDynamicInputDto() {
        this.pageNum = 1;
        pageSize = 1000;
    }
}
