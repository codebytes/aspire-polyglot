// WithHttpEndpointOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for WithHttpEndpoint. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class WithHttpEndpointOptions {
    private Number port;
    private Number targetPort;
    private String name;
    private String env;
    private Boolean isProxied;

    public Number getPort() { return port; }
    public WithHttpEndpointOptions port(Number value) {
        this.port = value;
        return this;
    }

    public Number getTargetPort() { return targetPort; }
    public WithHttpEndpointOptions targetPort(Number value) {
        this.targetPort = value;
        return this;
    }

    public String getName() { return name; }
    public WithHttpEndpointOptions name(String value) {
        this.name = value;
        return this;
    }

    public String getEnv() { return env; }
    public WithHttpEndpointOptions env(String value) {
        this.env = value;
        return this;
    }

    public Boolean isProxied() { return isProxied; }
    public WithHttpEndpointOptions isProxied(Boolean value) {
        this.isProxied = value;
        return this;
    }

}
