package top.anets.reflect;

import lombok.Data;

import java.util.List;

/*****************************************************
 * @fileName: MyReflect
 * @program: design
 * @module: reflect
 * @description: reflect
 * @author: tanyangbo
 * @create: 2022-03-24 19:01
 * @checker:
 * @document:
 * @editLog:
 * editDate  editBy  description 
 *****************************************************/
public class MyReflect {
    public Vo fun(Vo vo ,String name){
        System.out.println(vo);
        System.out.println(name);
        return vo;
    }

    public static void main(String[] args) {
        Class  aClass = new MyReflect().getClass();

    }
}

@Data
class  Vo{
    private Integer pageSize;
    private Integer pageNum;
    private List data;
}
