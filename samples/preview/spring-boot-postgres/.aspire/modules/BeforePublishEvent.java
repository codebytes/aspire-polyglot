// BeforePublishEvent.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Publishing.BeforePublishEvent. */
public class BeforePublishEvent extends HandleWrapperBase {
    BeforePublishEvent(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** The `IServiceProvider` for the app host. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Publishing/BeforePublishEvent.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** The `DistributedApplicationModel` instance. */
    public DistributedApplicationModel model() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Publishing/BeforePublishEvent.model", reqArgs);
        return (DistributedApplicationModel) result;
    }

}
