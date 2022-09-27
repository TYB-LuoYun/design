package top.anets.modules.bigDataInsert.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ftm
 * @date 2022/9/19 0019 15:55
 */
public class BathUtil {

    public static <T>   List<List<T>> pagingList(List<T> list, int pageSize){
        int length = list.size();
        int num = (length+pageSize-1)/pageSize;//多少页
        List<List<T>> newList =  new ArrayList<>();
        for(int i=0;i<num;i++){
            int fromIndex = i*pageSize;
            int toIndex = (i+1)*pageSize<length?(i+1)*pageSize:length;
            newList.add(list.subList(fromIndex,toIndex));
        }
        return newList;
    }
}
