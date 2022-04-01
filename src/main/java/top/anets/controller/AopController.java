package top.anets.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.annotation.InsertParam;
import top.anets.annotation.ReflectTest;

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
}
