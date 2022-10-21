package top.anets.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ftm
 * @date 2022/9/19 0019 16:42
 */

/**
 * 大集合切成小集合
 */
public class ListUtil {
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
