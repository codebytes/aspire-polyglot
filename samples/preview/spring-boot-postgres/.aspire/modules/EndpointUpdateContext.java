// EndpointUpdateContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.EndpointUpdateContext. */
public class EndpointUpdateContext extends HandleWrapperBase {
    EndpointUpdateContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the endpoint name. */
    public String name() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.name", reqArgs);
        return (String) result;
    }

    /** Gets or sets the network protocol. */
    public ProtocolType protocol() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.protocol", reqArgs);
        return ProtocolType.fromValue((String) result);
    }

    /** Sets the Protocol property */
    public EndpointUpdateContext setProtocol(ProtocolType value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setProtocol", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets the desired host port. */
    public double port() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.port", reqArgs);
        return result == null ? null : ((Number) result).doubleValue();
    }

    /** Sets the Port property */
    public EndpointUpdateContext setPort(double value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setPort", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets the target port. */
    public double targetPort() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.targetPort", reqArgs);
        return result == null ? null : ((Number) result).doubleValue();
    }

    /** Sets the TargetPort property */
    public EndpointUpdateContext setTargetPort(double value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setTargetPort", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets the URI scheme. */
    public String uriScheme() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.uriScheme", reqArgs);
        return (String) result;
    }

    /** Sets the UriScheme property */
    public EndpointUpdateContext setUriScheme(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setUriScheme", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets the target host. */
    public String targetHost() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.targetHost", reqArgs);
        return (String) result;
    }

    /** Sets the TargetHost property */
    public EndpointUpdateContext setTargetHost(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setTargetHost", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets the transport. */
    public String transport() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.transport", reqArgs);
        return (String) result;
    }

    /** Sets the Transport property */
    public EndpointUpdateContext setTransport(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setTransport", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets a value indicating whether the endpoint is external. */
    public boolean isExternal() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.isExternal", reqArgs);
        return (Boolean) result;
    }

    /** Sets the IsExternal property */
    public EndpointUpdateContext setIsExternal(boolean value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setIsExternal", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets a value indicating whether the endpoint is proxied. */
    public boolean isProxied() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.isProxied", reqArgs);
        return result == null ? null : (Boolean) result;
    }

    /** Sets the IsProxied property */
    public EndpointUpdateContext setIsProxied(boolean value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setIsProxied", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets a value indicating whether the endpoint is excluded from the default reference set. */
    public boolean excludeReferenceEndpoint() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.excludeReferenceEndpoint", reqArgs);
        return (Boolean) result;
    }

    /** Sets the ExcludeReferenceEndpoint property */
    public EndpointUpdateContext setExcludeReferenceEndpoint(boolean value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setExcludeReferenceEndpoint", reqArgs);
        return (EndpointUpdateContext) result;
    }

    /** Gets or sets a value indicating whether TLS is enabled. */
    public boolean tlsEnabled() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.tlsEnabled", reqArgs);
        return (Boolean) result;
    }

    /** Sets the TlsEnabled property */
    public EndpointUpdateContext setTlsEnabled(boolean value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointUpdateContext.setTlsEnabled", reqArgs);
        return (EndpointUpdateContext) result;
    }

}
