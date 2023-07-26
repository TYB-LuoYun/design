package top.anets.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ftm
 * @Date 2023-01-30 10:25:17
 * @Description Query分页构造器
 */

@Data
@NoArgsConstructor
public class PageQuery {


    Integer size = 20;
    Integer current = 1;
    public IPage Page(){
        //防止重写
        if(current==null){
            current = 1;
        }
        if(size==null){
            size = 20;
        }
        IPage page = new Page<>(current, size);
        return page;
    }

    public PageQuery(boolean isBig){
        if(isBig == true){
            size=999;
        }
    }

}