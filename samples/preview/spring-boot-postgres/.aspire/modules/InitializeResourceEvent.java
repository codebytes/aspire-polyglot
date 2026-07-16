// InitializeResourceEvent.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.InitializeResourceEvent. */
public class InitializeResourceEvent extends HandleWrapperBase {
    InitializeResourceEvent(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the Resource property */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/InitializeResourceEvent.resource", reqArgs);
        return (IResource) result;
    }

    /** The `IDistributedApplicationEventing` service for the app host. */
    public IDistributedApplicationEventing eventing() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/InitializeResourceEvent.eventing", reqArgs);
        return (IDistributedApplicationEventing) result;
    }

    /** An instance of `ILogger` that can be used to log messages for the resource. */
    public ILogger logger() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/InitializeResourceEvent.logger", reqArgs);
        return (ILogger) result;
    }

    /** The `ResourceNotificationService` for the app host. */
    public ResourceNotificationService notifications() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/InitializeResourceEvent.notifications", reqArgs);
        return (ResourceNotificationService) result;
    }

    /** The `IServiceProvider` for the app host. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/InitializeResourceEvent.services", reqArgs);
        return (IServiceProvider) result;
    }

}
