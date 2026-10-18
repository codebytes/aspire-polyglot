// UrlDisplayLocation.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** UrlDisplayLocation enum. */
@SuppressWarnings({"all", "unchecked", "serial"})
public enum UrlDisplayLocation implements WireValueEnum {
    SUMMARY_AND_DETAILS("SummaryAndDetails"),
    DETAILS_ONLY("DetailsOnly");

    private final String value;

    UrlDisplayLocation(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static UrlDisplayLocation fromValue(String value) {
        for (UrlDisplayLocation e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
