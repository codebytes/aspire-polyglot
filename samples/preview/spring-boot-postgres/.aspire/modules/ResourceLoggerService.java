// ResourceLoggerService.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceLoggerService. */
public class ResourceLoggerService extends HandleWrapperBase {
    ResourceLoggerService(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Completes the log stream for a resource. */
    public void completeLog(IResource resource) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("loggerService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resource", AspireClient.serializeValue(resource));
        getClient().invokeCapability("Aspire.Hosting/completeLog", reqArgs);
    }

    public void completeLog(ResourceBuilderBase resource) {
        completeLog(new IResource(resource.getHandle(), resource.getClient()));
    }

    /** Completes the log stream by resource name. */
    public void completeLogByName(String resourceName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("loggerService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceName", AspireClient.serializeValue(resourceName));
        getClient().invokeCapability("Aspire.Hosting/completeLogByName", reqArgs);
    }

}
