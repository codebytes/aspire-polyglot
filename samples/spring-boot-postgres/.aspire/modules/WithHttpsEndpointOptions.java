// WithHttpsEndpointOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for WithHttpsEndpoint. */
public final class WithHttpsEndpointOptions {
    private Double port;
    private Double targetPort;
    private String name;
    private String env;
    private Boolean isProxied;

    public Double getPort() { return port; }
    public WithHttpsEndpointOptions port(Double value) {
        this.port = value;
        return this;
    }

    public Double getTargetPort() { return targetPort; }
    public WithHttpsEndpointOptions targetPort(Double value) {
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
