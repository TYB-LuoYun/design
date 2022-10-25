package top.anets.base;

import java.util.List;

/**
 * @author ftm
 * @date 2022/10/21 0021 14:07
 */
public class Functions {
    @FunctionalInterface
    public interface FunctionZero<T>  {
        void  accept(T item);

    }

    @FunctionalInterface
    public interface FunctionOneResult<T,T1> {
        void accept(T item,T1  result1);
    }


    @FunctionalInterface
    public interface FunctionTwoResult<T,T1,T2> {
        void accept(T item,T1  result1,T2  result2);
    }

    @FunctionalInterface
    public interface FunctionThreeResult<T,T1,T2,T3> {
        void accept(T item,T1  result1,T2  result2,T3  result3);
    }


    @FunctionalInterface
    public interface FunctionFourResult<T,T1,T2,T3,T4> {
        void accept(T item,T1  result1,T2  result2,T3  result3,T4  result4);
    }

    @FunctionalInterface
    public interface FunctionFiveResult<T,T1,T2,T3,T4,T5> {
        void accept(T item,T1  result1,T2  result2,T3  result3,T4  result4,T5  result5);
    }

    @FunctionalInterface
    public interface FunctionResults<T,T1,T2,T3,T4,T5,T6>  {
        void accept(T item,T1  result1,T2  result2,T3  result3,T4  result4,T5  result5,T6  result6);
    }

}
