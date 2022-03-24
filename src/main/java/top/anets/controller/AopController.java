package top.anets.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.aop.MyAround;

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
    @RequestMapping("test")
    @MyAround
    public void testAop(Vo vo){
        System.out.println("ok================" +vo);
    }
}
