package top.anets.modules.system.controller;

import org.springframework.web.bind.annotation.*;
import top.anets.base.*;
import top.anets.modules.system.entity.Dict;
import top.anets.modules.system.service.IDictService;
import top.anets.modules.system.service.ISysMenuService;
import top.anets.modules.system.entity.SysMenu;
import top.anets.modules.system.service.impl.DictServiceImpl;
import top.anets.modules.system.vo.SysMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单信息表 前端控制器
 * </p>
 *
 * @author ftm
 * @since 2022-10-11
 */
@Api(tags = {"菜单信息表"})
@RestController
@RequestMapping("/sys-menu")
public class SysMenuController  {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ISysMenuService sysMenuService;


    @Resource
    private IDictService dictService;



    @ApiOperation(value = "新增/更新")
    @PostMapping("saveOrUpdate")
    public void saveOrUpdate(@RequestBody SysMenu sysMenu){
         sysMenuService.saveOrUpdate(sysMenu);
    }

    @ApiOperation(value = "新增/更新")
    @PostMapping("addOrModify")
    public void addOrModify(@RequestBody SysMenuVo sysMenuVo){
        //todo yourself
        sysMenuService.saveOrUpdate(WrapperQuery.from(sysMenuVo, SysMenu.class));
    }



    @ApiOperation(value = "删除")
    @RequestMapping("deletes")
    public void deletes(String... ids){
         sysMenuService.removeByIds(Arrays.asList(ids));
    }

    @ApiOperation(value = "Id查询")
    @GetMapping("/detail/{id}")
    public SysMenu findById(@PathVariable Long id){
        return sysMenuService.findById(id);
    }




    @ApiOperation(value = "查询-分页-查询和返回不处理")
    @RequestMapping("pages")
    public IPage pages(@RequestParam(required = false)  Map<String,Object> params, PageQuery query){
        return sysMenuService.pages(WrapperQuery.query(params), query.Page());
    }


    @ApiOperation(value = "查询-分页-查询和返回新增字段或特殊处理")
    @RequestMapping("lists")
    public IPage lists(  SysMenuVo sysMenuVo, PageQuery query){
        long start = System.currentTimeMillis();
        IPage  pages = sysMenuService.pages(WrapperQuery.query(sysMenuVo), query.Page());
        WrapperQuery.wpage(pages,SysMenuVo.class)
                .associate(dictService).add(SysMenu::getCode, Dict::getDescription).add(SysMenu::getId, Dict::getId)
                .fetch()
                .<List<Dict>>forEach((item,dicts)->{
                    item.setAssociate(dicts);
                 });
        long end = System.currentTimeMillis();
        System.out.println("消耗时间:"+(end-start));
        return pages;
    }


    @ApiOperation(value = "查询-分页-查询和返回新增字段或特殊处理")
    @RequestMapping("lists1")
    public IPage lists1(  SysMenuVo sysMenuVo, PageQuery query){
        long start = System.currentTimeMillis();
        IPage  pages = sysMenuService.pages(WrapperQuery.query(sysMenuVo), query.Page());
        WrapperQuery.wpage(pages,SysMenuVo.class)
                .associate(dictService).add(SysMenu::getCode, Dict::getDescription).add(SysMenu::getId, Dict::getId)
                .fetch(false)
                .<List<Dict>>forEach((item,dicts)->{
                    item.setAssociate(dicts);
                });
        long end = System.currentTimeMillis();
        System.out.println("消耗时间:"+(end-start));
        return pages;
    }

   





    @ApiOperation(value = "关联查询-分页")
    @PostMapping("pagesAssociate")
    public IPage pagesAssociate(@RequestParam(required = false)  Map<String,Object> params, PageQuery query){
        return sysMenuService.pagesAssociate(params, query.Page());
    }

    public static void  main(String[] args){
        Dict dict = new Dict();
        Object o =  (Object) dict;
        Class<?> aClass = o.getClass();


    }

    public static void tyb(String up){

    }

}
