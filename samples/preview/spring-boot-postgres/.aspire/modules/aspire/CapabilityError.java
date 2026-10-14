// CapabilityError.java - GENERATED CODE - DO NOT EDIT

package aspire;

/**
 * CapabilityError represents an error from a capability invocation.
 */
@SuppressWarnings({"all", "unchecked", "serial"})
public class CapabilityError extends RuntimeException {
    private final String code;
    private final Object data;

    CapabilityError(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public String getCode() { return code; }
    public Object getData() { return data; }
}
