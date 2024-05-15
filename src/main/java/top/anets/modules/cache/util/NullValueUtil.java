package top.anets.modules.cache.util;

import top.anets.modules.cache.model.NullValue;

/**
 * @author ftm
 * @date 2024-05-13 14:08
 */
public class NullValueUtil {
    /**
     * 转换为存储值
     */
    public static Object toStoreValue(Object userValue, boolean allowNullValues ) {
        if (userValue == null) {
            if (allowNullValues) {
                return NullValue.INSTANCE;
            }
            throw new IllegalArgumentException("Cache   is configured to not allow null values but null was provided");
        }
        return userValue;
    }

    /**
     * 从存储值解析为具体值
     */
    public static Object fromStoreValue(Object storeValue, boolean allowNullValues) {
        if (allowNullValues && storeValue instanceof NullValue) {
            return null;
        }
        return storeValue;
    }
}
