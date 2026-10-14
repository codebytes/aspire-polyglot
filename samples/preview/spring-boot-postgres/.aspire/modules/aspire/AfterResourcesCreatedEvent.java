// AfterResourcesCreatedEvent.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.AfterResourcesCreatedEvent. */
@SuppressWarnings({"all", "unchecked", "serial"})
public class AfterResourcesCreatedEvent extends HandleWrapperBase {
    AfterResourcesCreatedEvent(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** The `IServiceProvider` instance. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/AfterResourcesCreatedEvent.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** The `DistributedApplicationModel` instance. */
    public DistributedApplicationModel model() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/AfterResourcesCreatedEvent.model", reqArgs);
        return (DistributedApplicationModel) result;
    }

}
