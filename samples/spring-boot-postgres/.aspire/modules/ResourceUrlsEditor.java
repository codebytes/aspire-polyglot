// ResourceUrlsEditor.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceUrlsEditor. */
public class ResourceUrlsEditor extends HandleWrapperBase {
    ResourceUrlsEditor(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the execution context associated with this editor. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceUrlsEditor.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

    public void add(String url, String displayText) {
        add(AspireUnion.of(url), displayText);
    }

    public void add(ReferenceExpression url, String displayText) {
        add(AspireUnion.of(url), displayText);
    }

    public void add(AspireUnion url) {
        add(url, null);
    }

    /** Adds a displayed URL. */
    public void add(AspireUnion url, String displayText) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("url", AspireClient.serializeValue(url));
        if (displayText != null) {
            reqArgs.put("displayText", AspireClient.serializeValue(displayText));
        }
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceUrlsEditor.add", reqArgs);
    }

    public void addForEndpoint(EndpointReference endpoint, String url, String displayText) {
        addForEndpoint(endpoint, AspireUnion.of(url), displayText);
    }

    public void addForEndpoint(EndpointReference endpoint, ReferenceExpression url, String displayText) {
        addForEndpoint(endpoint, AspireUnion.of(url), displayText);
    }

    public void addForEndpoint(EndpointReference endpoint, AspireUnion url) {
        addForEndpoint(endpoint, url, null);
    }

    /** Adds a displayed URL for a specific endpoint. */
    public void addForEndpoint(EndpointReference endpoint, AspireUnion url, String displayText) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("endpoint", AspireClient.serializeValue(endpoint));
        reqArgs.put("url", AspireClient.serializeValue(url));
        if (displayText != null) {
            reqArgs.put("displayText", AspireClient.serializeValue(displayText));
        }
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceUrlsEditor.addForEndpoint", reqArgs);
    }

}
