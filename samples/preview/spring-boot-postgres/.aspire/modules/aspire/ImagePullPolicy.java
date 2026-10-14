// ImagePullPolicy.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** ImagePullPolicy enum. */
@SuppressWarnings({"all", "unchecked", "serial"})
public enum ImagePullPolicy implements WireValueEnum {
    DEFAULT("Default"),
    ALWAYS("Always"),
    MISSING("Missing"),
    NEVER("Never");

    private final String value;

    ImagePullPolicy(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ImagePullPolicy fromValue(String value) {
        for (ImagePullPolicy e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
