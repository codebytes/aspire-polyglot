// WithHttpProbeOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for WithHttpProbe. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class WithHttpProbeOptions {
    private String path;
    private Number initialDelaySeconds;
    private Number periodSeconds;
    private Number timeoutSeconds;
    private Number failureThreshold;
    private Number successThreshold;
    private String endpointName;

    public String getPath() { return path; }
    public WithHttpProbeOptions path(String value) {
        this.path = value;
        return this;
    }

    public Number getInitialDelaySeconds() { return initialDelaySeconds; }
    public WithHttpProbeOptions initialDelaySeconds(Number value) {
        this.initialDelaySeconds = value;
        return this;
    }

    public Number getPeriodSeconds() { return periodSeconds; }
    public WithHttpProbeOptions periodSeconds(Number value) {
        this.periodSeconds = value;
        return this;
    }

    public Number getTimeoutSeconds() { return timeoutSeconds; }
    public WithHttpProbeOptions timeoutSeconds(Number value) {
        this.timeoutSeconds = value;
        return this;
    }

    public Number getFailureThreshold() { return failureThreshold; }
    public WithHttpProbeOptions failureThreshold(Number value) {
        this.failureThreshold = value;
        return this;
    }

    public Number getSuccessThreshold() { return successThreshold; }
    public WithHttpProbeOptions successThreshold(Number value) {
        this.successThreshold = value;
        return this;
    }

    public String getEndpointName() { return endpointName; }
    public WithHttpProbeOptions endpointName(String value) {
        this.endpointName = value;
        return this;
    }

}
