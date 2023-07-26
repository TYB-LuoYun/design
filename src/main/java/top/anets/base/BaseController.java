package top.anets.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * @author ftm
 * @date 2022/11/8 0008 10:02
 */
public class BaseController<T> {
    @Autowired
    private IService<T> iService;

    @ApiOperation(value = "新增/更新",notes = "公用方法")
    @PostMapping("addOrModify")
    public void addOrModify(@RequestBody T t){
        iService.saveOrUpdate(t);
    }

    @ApiOperation(value = "删除",notes = "公用方法")
    @RequestMapping("deletes")
    public void deletes(String... ids){
        iService.removeByIds(Arrays.asList(ids));
    }

    @ApiOperation(value = "Id查询",notes = "公用方法")
    @RequestMapping("/detail/{id}")
    public T findById(@PathVariable Serializable id){
        return (T)  iService.getById(  id);
    }


    @ApiOperation(value = "分页查询",notes = "公用方法")
    @GetMapping("/pages")
    public IPage pages(@RequestParam(required = false) Map<String,Object> params, PageQuery query){
        return   iService.page( query.Page(), WrapperQuery.query(params));
    }

}

