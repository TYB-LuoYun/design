package top.anets.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.*;
import top.anets.base.*;
import top.anets.modules.Mongodb.MongoDBUtil;
import top.anets.modules.Mongodb.example.ExamSearchList;
import top.anets.modules.serviceMonitor.server.Sys;
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

import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;
import top.anets.modules.threads.ThreadPool.ThreadPoolUtils;
import top.anets.utils.ListUtil;

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
public class SysMenuController  extends BaseController<SysMenu>{

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ISysMenuService sysMenuService;


    @Resource
    private IDictService dictService;




    @ApiOperation(value = "Id查询",notes = "公用方法")
    @RequestMapping("/detail/{id}")
    public SysMenu findById(@PathVariable Serializable id){
        return sysMenuService.getById(  id);
    }



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





    @ApiOperation(value = "查询-分页-查询和返回新增字段或特殊处理")
    @RequestMapping("lists")
    public IPage lists(  SysMenuVo sysMenuVo, PageQuery query){
        long start = System.currentTimeMillis();
        IPage  pages = sysMenuService.pages(WrapperQuery.query(sysMenuVo), query.Page());
        WrapperQuery.wpage(pages,SysMenuVo.class)
                .associate(SysMenuVo::getAssociate,dictService).add(SysMenu::getCode, Dict::getDescription).add(SysMenu::getId, Dict::getId)
                .fetch(SysMenu::getCode)
                ;
        long end = System.currentTimeMillis();
        System.out.println("消耗时间:"+(end-start));
        return pages;
    }


    @ApiOperation(value = "查询-分页-查询和返回新增字段或特殊处理")
    @RequestMapping("lists1")
    public IPage lists1(  SysMenuVo sysMenuVo, PageQuery query){
        long start = System.currentTimeMillis();
        QueryWrapper query1 = WrapperQuery.query(sysMenuVo);
        IPage  pages = sysMenuService.pages(query1, query.Page());
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




    @ApiOperation(value = "查询-分页-查询和返回新增字段或特殊处理")
    @RequestMapping("fake")
    public void test() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        List<Dict> objects = new ArrayList<>();
        for(int i=0;i<5000000;i++){
            Dict dict = new Dict();
            dict.setAttr1(i+"");
            dict.setDeleted(1);
            dict.setCode("code"+i);
            dict.setName("name"+i);
            objects.add(dict);
        }
        List<List<Dict>> lists = ListUtil.pagingList(objects, 10000);
        ArrayList<Future<Integer>>  futures = new ArrayList<>();
        for (int i=0;i<lists.size();i++) {
            List<Dict> item = lists.get(i);
            Future<Integer> submit = ThreadPoolUtils.submit(new Callable<Integer>() {
                @Override
                public Integer call() {
                    System.out.println("保存中："+item.size());
                    dictService.saveBatch(item);
                    System.out.println("成功保存："+item.size());
                    return null;
                }
            });
            futures.add(submit);
        }
        for (int i=0;i<lists.size();i++) {
            futures.get(i).get();
        }
        System.out.println("全部保存完毕");
    }






     @RequestMapping("queryMap")
     public void queryMap(){
         QueryMap map1 = new QueryMap();
         map1.eq(SysMenu::getCode,1 );


         QueryMap map = new QueryMap();
         map.eq(SysMenu::getSort,2 );
         map.in(SysMenu::getComponent,"2");
         map.or();
         map.eq(SysMenu::getJumpUrl,78 );


         map1.and(map);
         QueryWrapper query = WrapperQuery.query(map1);
         List list = sysMenuService.list(query);
         System.out.println(list.size());


     }



    @ApiOperation(value = "关联查询-分页")
    @PostMapping("pagesAssociate")
    public IPage pagesAssociate(@RequestParam(required = false)  Map<String,Object> params, PageQuery query){
        return sysMenuService.pagesAssociate(params, query.Page());
    }


    @ApiOperation(value = "getOne")
    @GetMapping("getOne")
    public Dict getOne( ){
        Dict one = dictService.getOne(new QueryWrapper<>(), false);
        return one;
    }


    @ApiOperation(value = "getOne1")
    @GetMapping("getOne1")
    public Dict getOne1( ){
        Dict dict = WrapperQuery.queryOne(dictService, Wrappers.<Dict>lambdaQuery().eq(Dict::getDeleted, "1"));
        return dict;
    }

    @GetMapping("mongo")
    public void mongo(){
        QueryMap or = new QueryMap();
        or.eq("InPatientNo", "3");
        or.or();
        or.eq("OutPatientNo", "4");

        QueryMap build = QueryMap.build();
        build.like("IdCardNo","12");
        build.and(or);
        IPage<ExamSearchList> page = MongoDBUtil.page(new PageQuery().Page(), WrapperQueryForMongo.query(build), ExamSearchList.class);
        System.out.println(page);
    }




    public static void  main(String[] args){

        QueryMap map = new QueryMap();
        map.eq(SysMenu::getCode,1 );
        map.or();
        map.eq(SysMenu::getId,2 );
        QueryWrapper query = WrapperQuery.query(map);
        System.out.println(query.getCustomSqlSegment());
        System.out.println(map.size());

    }

    public static void tyb(String up){

    }

}
