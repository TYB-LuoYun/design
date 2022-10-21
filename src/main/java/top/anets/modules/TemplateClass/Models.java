package top.anets.modules.TemplateClass;

import com.alibaba.nacos.common.utils.Md5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * @author ftm
 * @date 2022/10/19 0019 11:11
 */
@Data
public class Models<T> {
    private List<T> list;

    private IPage<T> page;




    public static void main(String[] args){

    }


    public static Models  test(){
        return new Models<Integer>();
    }


    public  static  <T>    Models<T> test(Class<T> S1){
        return null;
    }
}




