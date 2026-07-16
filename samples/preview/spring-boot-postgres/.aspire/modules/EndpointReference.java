// EndpointReference.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.EndpointReference. */
public class EndpointReference extends HandleWrapperBase {
    EndpointReference(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource owner of the endpoint reference. */
    public IResourceWithEndpoints resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.resource", reqArgs);
        return (IResourceWithEndpoints) result;
    }

    /** Gets the name of the endpoint associated with the endpoint reference. */
    public String endpointName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.endpointName", reqArgs);
        return (String) result;
    }

    /** Gets or sets a custom error message to be thrown when the endpoint annotation is not found. */
    public String errorMessage() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.errorMessage", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Gets a value indicating whether the endpoint is allocated. */
    public boolean isAllocated() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.isAllocated", reqArgs);
        return (Boolean) result;
    }

    /** Gets a value indicating whether the endpoint exists. */
    public boolean exists() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.exists", reqArgs);
        return (Boolean) result;
    }

    /** Gets a value indicating whether the endpoint uses HTTP scheme. */
    public boolean isHttp() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.isHttp", reqArgs);
        return (Boolean) result;
    }

    /** Gets a value indicating whether the endpoint uses HTTPS scheme. */
    public boolean isHttps() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.isHttps", reqArgs);
        return (Boolean) result;
    }

    /** Gets a value indicating whether TLS is enabled for this endpoint. */
    public boolean tlsEnabled() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.tlsEnabled", reqArgs);
        return (Boolean) result;
    }

    /** Gets a value indicating whether the endpoint name is "http" or "https", ignoring case. This is a convention used to identify endpoints that will be resolved based on the scheme of the endpoint in service discovery rather than by the specific endpoint name. This is done to allow http endpoints that are dynamically updated to https to be mapped correctly despite the endpoint name no longer matching the scheme. */
    public boolean isHttpSchemeNamedEndpoint() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.isHttpSchemeNamedEndpoint", reqArgs);
        return (Boolean) result;
    }

    /** Gets a value indicating whether this endpoint is excluded from the default set when referencing the resource's endpoints. */
    public boolean excludeReferenceEndpoint() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.excludeReferenceEndpoint", reqArgs);
        return (Boolean) result;
    }

    /** Gets the port for this endpoint. */
    public double port() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.port", reqArgs);
        return ((Number) result).doubleValue();
    }

    /** Gets the target port for this endpoint. If the port is dynamically allocated, this will return `null`. */
    public double targetPort() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.targetPort", reqArgs);
        return result == null ? null : ((Number) result).doubleValue();
    }

    /** Gets the host for this endpoint. */
    public String host() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.host", reqArgs);
        return (String) result;
    }

    /** Gets the scheme for this endpoint. */
    public String scheme() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.scheme", reqArgs);
        return (String) result;
    }

    /** Gets the URL for this endpoint. */
    public String url() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.url", reqArgs);
        return (String) result;
    }

    public String getValueAsync() {
        return getValueAsync(null);
    }

    /** Gets the URL of the endpoint asynchronously. Waits for the endpoint to be allocated if necessary. */
    public String getValueAsync(CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.getValueAsync", reqArgs);
        return (String) result;
    }

    /** Gets the specified property expression of the endpoint. */
    public EndpointReferenceExpression property(EndpointProperty property) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("property", AspireClient.serializeValue(property));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.property", reqArgs);
        return (EndpointReferenceExpression) result;
    }

    /** Gets a conditional expression that resolves to the enabledValue when TLS is enabled on the endpoint, or to the disabledValue otherwise. */
    public ReferenceExpression getTlsValue(ReferenceExpression enabledValue, ReferenceExpression disabledValue) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("enabledValue", AspireClient.serializeValue(enabledValue));
        reqArgs.put("disabledValue", AspireClient.serializeValue(disabledValue));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReference.getTlsValue", reqArgs);
        return (ReferenceExpression) result;
    }

}
