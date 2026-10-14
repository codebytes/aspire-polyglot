// HttpCommandResultMode.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** HttpCommandResultMode enum. */
@SuppressWarnings({"all", "unchecked", "serial"})
public enum HttpCommandResultMode implements WireValueEnum {
    NONE("None"),
    AUTO("Auto"),
    JSON("Json"),
    TEXT("Text");

    private final String value;

    HttpCommandResultMode(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static HttpCommandResultMode fromValue(String value) {
        for (HttpCommandResultMode e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
