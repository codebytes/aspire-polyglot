// WithHttpsEndpointOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for WithHttpsEndpoint. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class WithHttpsEndpointOptions {
    private Number port;
    private Number targetPort;
    private String name;
    private String env;
    private Boolean isProxied;

    public Number getPort() { return port; }
    public WithHttpsEndpointOptions port(Number value) {
        this.port = value;
        return this;
    }

    public Number getTargetPort() { return targetPort; }
    public WithHttpsEndpointOptions targetPort(Number value) {
        this.targetPort = value;
        return this;
    }

    public String getName() { return name; }
    public WithHttpsEndpointOptions name(String value) {
        this.name = value;
        return this;
    }

    public String getEnv() { return env; }
    public WithHttpsEndpointOptions env(String value) {
        this.env = value;
        return this;
    }

    public Boolean isProxied() { return isProxied; }
    public WithHttpsEndpointOptions isProxied(Boolean value) {
        this.isProxied = value;
        return this;
    }

}
