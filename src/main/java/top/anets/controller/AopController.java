package top.anets.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.anets.annotation.InsertParam;
import top.anets.annotation.ReflectTest;
import top.anets.base.PageQuery;

import java.util.Map;

/*****************************************************
 * @fileName: AopController
 * @program: design
 * @module: aop
 * @description: aop
 * @author: tanyangbo
 * @create: 2022-03-23 11:17
 * @checker:
 * @document:
 * @editLog:
 * editDate  editBy  description 
 *****************************************************/
@RequestMapping("aop")
@RestController
public class AopController {

    private String name="12";

    @RequestMapping("page")
    @ReflectTest
    public Integer page(   Integer pageNum, Integer pageSize, @InsertParam(value="ss") Vo vo){
        System.out.println(pageNum+"/"+pageSize);
        return 2;
    }


    @ApiOperation(value = "关联查询-分页")
    @RequestMapping("pagesAssociate")
    public IPage pagesAssociate(@RequestParam(required = false) Map<String,Object> params, PageQuery query){
       System.out.println(params);
       return null;
    }
}
