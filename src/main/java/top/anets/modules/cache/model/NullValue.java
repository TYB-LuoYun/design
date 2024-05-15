package top.anets.modules.cache.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ftm
 * @date 2024-05-13 14:10
 */
@Data
public class NullValue implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Object INSTANCE = new NullValue();

    Object value =  null;

    private NullValue() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj || obj == null);
    }

    @Override
    public int hashCode() {
        return NullValue.class.hashCode();
    }

    @Override
    public String toString() {
        return "NullValue";
    }
}