// ResourceNotificationService.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceNotificationService. */
public class ResourceNotificationService extends HandleWrapperBase {
    ResourceNotificationService(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public void waitForResourceState(String resourceName) {
        waitForResourceState(resourceName, null);
    }

    /** Waits for a resource to reach a specified state. */
    public void waitForResourceState(String resourceName, String targetState) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("notificationService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceName", AspireClient.serializeValue(resourceName));
        if (targetState != null) {
            reqArgs.put("targetState", AspireClient.serializeValue(targetState));
        }
        getClient().invokeCapability("Aspire.Hosting/waitForResourceState", reqArgs);
    }

    /** Waits for a resource to reach one of the specified states. */
    public String waitForResourceStates(String resourceName, String[] targetStates) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("notificationService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceName", AspireClient.serializeValue(resourceName));
        reqArgs.put("targetStates", AspireClient.serializeValue(targetStates));
        var result = getClient().invokeCapability("Aspire.Hosting/waitForResourceStates", reqArgs);
        return (String) result;
    }

    /** Waits for a resource to become healthy. */
    public ResourceEventDto waitForResourceHealthy(String resourceName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("notificationService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceName", AspireClient.serializeValue(resourceName));
        var result = getClient().invokeCapability("Aspire.Hosting/waitForResourceHealthy", reqArgs);
        return ResourceEventDto.fromMap((Map<String, Object>) result);
    }

    /** Waits for all dependencies of a resource to be ready. */
    public void waitForDependencies(IResource resource) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("notificationService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resource", AspireClient.serializeValue(resource));
        getClient().invokeCapability("Aspire.Hosting/waitForDependencies", reqArgs);
    }

    public void waitForDependencies(ResourceBuilderBase resource) {
        waitForDependencies(new IResource(resource.getHandle(), resource.getClient()));
    }

    /** Tries to get the current state of a resource. */
    public ResourceEventDto tryGetResourceState(String resourceName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("notificationService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceName", AspireClient.serializeValue(resourceName));
        var result = getClient().invokeCapability("Aspire.Hosting/tryGetResourceState", reqArgs);
        return ResourceEventDto.fromMap((Map<String, Object>) result);
    }

    /** Publishes an update for a resource's state. */
    public void publishResourceUpdate(IResource resource, PublishResourceUpdateOptions options) {
        var state = options == null ? null : options.getState();
        var stateStyle = options == null ? null : options.getStateStyle();
        publishResourceUpdateImpl(resource, state, stateStyle);
    }

    public void publishResourceUpdate(ResourceBuilderBase resource, PublishResourceUpdateOptions options) {
        publishResourceUpdate(new IResource(resource.getHandle(), resource.getClient()), options);
    }

    public void publishResourceUpdate(IResource resource) {
        publishResourceUpdate(resource, null);
    }

    public void publishResourceUpdate(ResourceBuilderBase resource) {
        publishResourceUpdate(new IResource(resource.getHandle(), resource.getClient()));
    }

    /** Publishes an update for a resource's state. */
    private void publishResourceUpdateImpl(IResource resource, String state, String stateStyle) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("notificationService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resource", AspireClient.serializeValue(resource));
        if (state != null) {
            reqArgs.put("state", AspireClient.serializeValue(state));
        }
        if (stateStyle != null) {
            reqArgs.put("stateStyle", AspireClient.serializeValue(stateStyle));
        }
        getClient().invokeCapability("Aspire.Hosting/publishResourceUpdate", reqArgs);
    }

}
