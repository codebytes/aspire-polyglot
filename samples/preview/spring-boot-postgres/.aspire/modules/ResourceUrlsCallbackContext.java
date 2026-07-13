// ResourceUrlsCallbackContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceUrlsCallbackContext. */
public class ResourceUrlsCallbackContext extends HandleWrapperBase {
    ResourceUrlsCallbackContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource this the URLs are associated with. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceUrlsCallbackContext.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the editor used to manipulate displayed URLs in polyglot callbacks. */
    public ResourceUrlsEditor urls() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceUrlsCallbackContext.urls", reqArgs);
        return (ResourceUrlsEditor) result;
    }

    /** Gets the logger facade used by polyglot callbacks. */
    public LogFacade log() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceUrlsCallbackContext.log", reqArgs);
        return (LogFacade) result;
    }

    /** Gets the execution context associated with this invocation of the AppHost. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceUrlsCallbackContext.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

    /** Gets an endpoint reference from the associated resource */
    public EndpointReference getEndpoint(String name) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/getEndpoint", reqArgs);
        return (EndpointReference) result;
    }

}
