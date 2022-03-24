package top.anets.controller;

/*****************************************************
 * @fileName: Vo
 * @program: design
 * @module: vo
 * @description:
 * @author: tanyangbo
 * @create: 2022-03-23 13:03
 * @checker:
 * @document:
 * @editLog:
 * editDate  editBy  description 
 *****************************************************/
public class Vo {
    String name;
    Integer age;

    @Override
    public String toString() {
        return "Vo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
