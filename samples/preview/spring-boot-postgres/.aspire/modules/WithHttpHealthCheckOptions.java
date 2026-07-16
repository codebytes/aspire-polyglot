// WithHttpHealthCheckOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for WithHttpHealthCheck. */
public final class WithHttpHealthCheckOptions {
    private String path;
    private Double statusCode;
    private String endpointName;

    public String getPath() { return path; }
    public WithHttpHealthCheckOptions path(String value) {
        this.path = value;
        return this;
    }

    public Double getStatusCode() { return statusCode; }
    public WithHttpHealthCheckOptions statusCode(Double value) {
        this.statusCode = value;
        return this;
    }

    public String getEndpointName() { return endpointName; }
    public WithHttpHealthCheckOptions endpointName(String value) {
        this.endpointName = value;
        return this;
    }

}
