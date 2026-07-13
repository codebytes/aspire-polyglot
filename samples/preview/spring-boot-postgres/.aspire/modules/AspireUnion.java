// AspireUnion.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;

/**
 * Represents a runtime union value for generated Java APIs.
 */
public final class AspireUnion {
    private final Object value;

    private AspireUnion(Object value) {
        this.value = value;
    }

    static AspireUnion of(Object value) {
        return value instanceof AspireUnion union ? union : new AspireUnion(value);
    }

    static AspireUnion fromValue(Object value) {
        return of(value);
    }

    Object getValue() {
        return value;
    }

    boolean is(Class<?> type) {
        return value != null && type.isInstance(value);
    }

    <T> T getValueAs(Class<T> type) {
        if (value == null) {
            return null;
        }
        if (!type.isInstance(value)) {
            throw new IllegalStateException("Union value is of type " + value.getClass().getName() + ", not " + type.getName());
        }
        return type.cast(value);
    }

    @Override
    public String toString() {
        return "AspireUnion{" + value + "}";
    }
}
