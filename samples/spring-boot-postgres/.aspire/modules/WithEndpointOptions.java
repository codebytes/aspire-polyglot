// WithEndpointOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for WithEndpoint. */
public final class WithEndpointOptions {
    private Double port;
    private Double targetPort;
    private String scheme;
    private String name;
    private String env;
    private Boolean isProxied;
    private Boolean isExternal;
    private ProtocolType protocol;

    public Double getPort() { return port; }
    public WithEndpointOptions port(Double value) {
        this.port = value;
        return this;
    }

    public Double getTargetPort() { return targetPort; }
    public WithEndpointOptions targetPort(Double value) {
        this.targetPort = value;
        return this;
    }

    public String getScheme() { return scheme; }
    public WithEndpointOptions scheme(String value) {
        this.scheme = value;
        return this;
    }

    public String getName() { return name; }
    public WithEndpointOptions name(String value) {
        this.name = value;
        return this;
    }

    public String getEnv() { return env; }
    public WithEndpointOptions env(String value) {
        this.env = value;
        return this;
    }

    public Boolean isProxied() { return isProxied; }
    public WithEndpointOptions isProxied(Boolean value) {
        this.isProxied = value;
        return this;
    }

    public Boolean isExternal() { return isExternal; }
    public WithEndpointOptions isExternal(Boolean value) {
        this.isExternal = value;
        return this;
    }

    public ProtocolType getProtocol() { return protocol; }
    public WithEndpointOptions protocol(ProtocolType value) {
        this.protocol = value;
        return this;
    }

}
