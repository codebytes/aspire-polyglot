// AfterPublishEvent.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Publishing.AfterPublishEvent. */
public class AfterPublishEvent extends HandleWrapperBase {
    AfterPublishEvent(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** The `IServiceProvider` for the app host. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Publishing/AfterPublishEvent.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** The `DistributedApplicationModel` instance. */
    public DistributedApplicationModel model() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Publishing/AfterPublishEvent.model", reqArgs);
        return (DistributedApplicationModel) result;
    }

}
