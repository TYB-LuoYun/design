package top.anets.modules.functions;

import top.anets.modules.serviceMonitor.server.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author ftm
 * @date 2022/10/14 0014 9:37
 */
public class JantouFunction  {
    public static void test(Function function){
        function.plus(1000, 2);
        function.plus(1200, 2);
        function.plus(1300, 2);
    }
    public static void test(Function2 function) {
    }

    public static void main(String[] args) {
        test( (item,j)->{
            System.out.println("开始循环");
            System.out.println(item);
        });
        List<Object> objects = new ArrayList<>();
        objects.stream().forEach(consumerWithIndex((item,index)->{

        }));
    }
//
//    @Override
//    public void  plus(Integer i, Integer j){
//        System.out.println("函数执行了");
//        i= 100+i;
//    }


    // 工具方法
    public static <T> Consumer<T> consumerWithIndex(BiConsumer<T, Integer> consumer) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            consumer.accept(t, index);
        };
    }

}




@FunctionalInterface
interface Function {
    void plus(Integer i, Integer j);
}

@FunctionalInterface
interface Function2 {
   void plus(Integer i);
}

