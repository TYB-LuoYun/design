package top.anets.modules.system.vo;
import top.anets.modules.system.entity.Dict;
import top.anets.modules.system.entity.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class  SysMenuVo extends SysMenu{
     private List<Dict> associate;
    private String associate2;
    private String associate3;
    private String associate4;


}