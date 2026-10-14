// AspireUnion.java - GENERATED CODE - DO NOT EDIT

package aspire;

/**
 * Represents a runtime union value for generated Java APIs.
 */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class AspireUnion {
    private final Object value;

    private AspireUnion(Object value) {
        this.value = value;
    }

    public static AspireUnion of(Object value) {
        return value instanceof AspireUnion union ? union : new AspireUnion(value);
    }

    public static AspireUnion fromValue(Object value) {
        return of(value);
    }

    public Object getValue() {
        return value;
    }

    public boolean is(Class<?> type) {
        return value != null && type.isInstance(value);
    }

    public <T> T getValueAs(Class<T> type) {
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
