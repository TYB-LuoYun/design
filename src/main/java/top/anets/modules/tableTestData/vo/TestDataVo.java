package top.anets.modules.tableTestData.vo;
import top.anets.modules.tableTestData.entity.TestData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class  TestDataVo extends TestData{

}