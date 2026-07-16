// ResourceReadyEvent.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceReadyEvent. */
public class ResourceReadyEvent extends HandleWrapperBase {
    ResourceReadyEvent(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** The resource that is in a healthy state. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceReadyEvent.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the Services property */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ResourceReadyEvent.services", reqArgs);
        return (IServiceProvider) result;
    }

}
