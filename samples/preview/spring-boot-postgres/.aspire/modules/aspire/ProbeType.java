// ProbeType.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** ProbeType enum. */
@SuppressWarnings({"all", "unchecked", "serial"})
public enum ProbeType implements WireValueEnum {
    STARTUP("Startup"),
    READINESS("Readiness"),
    LIVENESS("Liveness");

    private final String value;

    ProbeType(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ProbeType fromValue(String value) {
        for (ProbeType e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
