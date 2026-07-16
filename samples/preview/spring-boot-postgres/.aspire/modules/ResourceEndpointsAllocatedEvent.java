// ResourceEndpointsAllocatedEvent.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceEndpointsAllocatedEvent. */
public class ResourceEndpointsAllocatedEvent extends HandleWrapperBase {
    ResourceEndpointsAllocatedEvent(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the Resource property */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceEndpointsAllocatedEvent.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the Services property */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceEndpointsAllocatedEvent.services", reqArgs);
        return (IServiceProvider) result;
    }

}
