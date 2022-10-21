package top.anets.utils;

import com.alibaba.fastjson.JSON;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ftm
 * @date 2022/10/20 0020 10:31
 */
public class MapUtils {
    /**
     * 按照红黑树（Red-Black tree）的 NavigableMap 实现
     * 按照字母大小排序
     */
    public static Map<String, Object> sort(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Map<String, Object> result = new TreeMap<>((Comparator<String>) (o1, o2) -> {
            return o1.compareTo(o2);
        });
        result.putAll(map);
        return result;
    }


    /**
     * 组合参数
     *
     * @param map
     * @return 如：key1Value1Key2Value2....
     */
    public static String groupStringParam(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> item : map.entrySet()) {
            if (item.getValue() != null) {
                sb.append(item.getKey());
                if (item.getValue() instanceof List) {
                    sb.append(JSON.toJSONString(item.getValue()));
                } else {
                    sb.append(item.getValue());
                }
            }
        }
        return sb.toString();
    }
}
