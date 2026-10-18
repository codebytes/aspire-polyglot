// ContainerLifetime.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** ContainerLifetime enum. */
@SuppressWarnings({"all", "unchecked", "serial"})
public enum ContainerLifetime implements WireValueEnum {
    SESSION("Session"),
    PERSISTENT("Persistent");

    private final String value;

    ContainerLifetime(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ContainerLifetime fromValue(String value) {
        for (ContainerLifetime e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
