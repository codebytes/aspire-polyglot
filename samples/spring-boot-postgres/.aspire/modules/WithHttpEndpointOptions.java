// WithHttpEndpointOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for WithHttpEndpoint. */
public final class WithHttpEndpointOptions {
    private Double port;
    private Double targetPort;
    private String name;
    private String env;
    private Boolean isProxied;

    public Double getPort() { return port; }
    public WithHttpEndpointOptions port(Double value) {
        this.port = value;
        return this;
    }

    public Double getTargetPort() { return targetPort; }
    public WithHttpEndpointOptions targetPort(Double value) {
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
