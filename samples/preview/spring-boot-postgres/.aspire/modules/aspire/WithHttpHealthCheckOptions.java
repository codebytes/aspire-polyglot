// WithHttpHealthCheckOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for WithHttpHealthCheck. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class WithHttpHealthCheckOptions {
    private String path;
    private Number statusCode;
    private String endpointName;

    public String getPath() { return path; }
    public WithHttpHealthCheckOptions path(String value) {
        this.path = value;
        return this;
    }

    public Number getStatusCode() { return statusCode; }
    public WithHttpHealthCheckOptions statusCode(Number value) {
        this.statusCode = value;
        return this;
    }

    public String getEndpointName() { return endpointName; }
    public WithHttpHealthCheckOptions endpointName(String value) {
        this.endpointName = value;
        return this;
    }

}
