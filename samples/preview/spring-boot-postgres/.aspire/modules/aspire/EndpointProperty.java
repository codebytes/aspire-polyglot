// EndpointProperty.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** EndpointProperty enum. */
@SuppressWarnings({"all", "unchecked", "serial"})
public enum EndpointProperty implements WireValueEnum {
    URL("Url"),
    HOST("Host"),
    IPV4_HOST("IPV4Host"),
    PORT("Port"),
    SCHEME("Scheme"),
    TARGET_PORT("TargetPort"),
    HOST_AND_PORT("HostAndPort"),
    TLS_ENABLED("TlsEnabled");

    private final String value;

    EndpointProperty(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static EndpointProperty fromValue(String value) {
        for (EndpointProperty e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
