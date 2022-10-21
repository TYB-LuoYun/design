package top.anets.modules.system.controller;

import org.springframework.web.bind.annotation.*;
import top.anets.modules.system.service.IDictService;
import top.anets.modules.system.entity.Dict;
import top.anets.modules.system.vo.DictVo;
import top.anets.base.WrapperQuery;
import top.anets.base.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ftm
 * @since 2022-10-11
 */
@Api(tags = {""})
@RestController
@RequestMapping("/dict")
public class DictController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private IDictService dictService;


    @ApiOperation(value = "新增/更新")
    @PostMapping("saveOrUpdate")
    public void saveOrUpdate(@RequestBody Dict dict){
         dictService.saveOrUpdate(dict);
    }

    @ApiOperation(value = "新增/更新")
    @PostMapping("addOrModify")
    public void addOrModify(@RequestBody DictVo dictVo){
        //todo yourself
        dictService.saveOrUpdate(WrapperQuery.from(dictVo, Dict.class));
    }



    @ApiOperation(value = "删除")
    @RequestMapping("deletes")
    public void deletes(String... ids){
         dictService.removeByIds(Arrays.asList(ids));
    }

    @ApiOperation(value = "Id查询")
    @GetMapping("/detail/{id}")
    public Dict getById(@PathVariable Long id){
        return dictService.getById(id);
    }




    @ApiOperation(value = "查询-分页-查询和返回不处理")
    @RequestMapping("pages")
    public IPage pages(@RequestParam(required = false)  Map<String,Object> params, PageQuery query){
        return dictService.pages(WrapperQuery.query(params), query.Page());
    }


    @ApiOperation(value = "查询-分页-查询和返回新增字段或特殊处理")
    @RequestMapping("lists")
    public IPage lists(  DictVo dictVo, PageQuery query){
        IPage  pages = dictService.pages(WrapperQuery.query(dictVo), query.Page());
        WrapperQuery.ipage(pages,DictVo.class).getRecords().forEach(item->{
        //         todo    item.get...

        });
        return pages;
    }

   





    @ApiOperation(value = "关联查询-分页")
    @PostMapping("pagesAssociate")
    public IPage pagesAssociate(@RequestParam(required = false)  Map<String,Object> params, PageQuery query){
        return dictService.pagesAssociate(params, query.Page());
    }


 }
