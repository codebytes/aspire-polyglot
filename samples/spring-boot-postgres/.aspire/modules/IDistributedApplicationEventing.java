// IDistributedApplicationEventing.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Eventing.IDistributedApplicationEventing. */
public class IDistributedApplicationEventing extends HandleWrapperBase {
    IDistributedApplicationEventing(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Unsubscribe from an event. */
    public void unsubscribe(DistributedApplicationEventSubscription subscription) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("subscription", AspireClient.serializeValue(subscription));
        getClient().invokeCapability("Aspire.Hosting.Eventing/IDistributedApplicationEventing.unsubscribe", reqArgs);
    }

}
