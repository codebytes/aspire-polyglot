// WithHttpProbeOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for WithHttpProbe. */
public final class WithHttpProbeOptions {
    private String path;
    private Double initialDelaySeconds;
    private Double periodSeconds;
    private Double timeoutSeconds;
    private Double failureThreshold;
    private Double successThreshold;
    private String endpointName;

    public String getPath() { return path; }
    public WithHttpProbeOptions path(String value) {
        this.path = value;
        return this;
    }

    public Double getInitialDelaySeconds() { return initialDelaySeconds; }
    public WithHttpProbeOptions initialDelaySeconds(Double value) {
        this.initialDelaySeconds = value;
        return this;
    }

    public Double getPeriodSeconds() { return periodSeconds; }
    public WithHttpProbeOptions periodSeconds(Double value) {
        this.periodSeconds = value;
        return this;
    }

    public Double getTimeoutSeconds() { return timeoutSeconds; }
    public WithHttpProbeOptions timeoutSeconds(Double value) {
        this.timeoutSeconds = value;
        return this;
    }

    public Double getFailureThreshold() { return failureThreshold; }
    public WithHttpProbeOptions failureThreshold(Double value) {
        this.failureThreshold = value;
        return this;
    }

    public Double getSuccessThreshold() { return successThreshold; }
    public WithHttpProbeOptions successThreshold(Double value) {
        this.successThreshold = value;
        return this;
    }

    public String getEndpointName() { return endpointName; }
    public WithHttpProbeOptions endpointName(String value) {
        this.endpointName = value;
        return this;
    }

}
