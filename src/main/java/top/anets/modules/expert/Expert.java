package top.anets.modules.expert;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author ftm
 * @date 2022/10/11 0011 15:06
 */
public class Expert {
    public static void main(String[] args){
        Consumer<Object> consumer =
                uploadData -> returnData(uploadData);


        consumer.accept("处理结果"); //这个方法可以回调


    }

    private static void returnData(Object uploadData) {
        System.out.println(uploadData);
    }
}
