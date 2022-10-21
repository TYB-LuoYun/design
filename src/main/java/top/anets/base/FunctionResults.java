package top.anets.base;

import java.util.List;

/**
 * @author ftm
 * @date 2022/10/17 0017 11:02
 */
@FunctionalInterface
public interface FunctionResults<T> {
    void accept(T item, List<Object> result);
}
