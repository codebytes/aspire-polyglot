// HttpsEndpointUpdateCallbackContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.HttpsEndpointUpdateCallbackContext. */
public class HttpsEndpointUpdateCallbackContext extends HandleWrapperBase {
    HttpsEndpointUpdateCallbackContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the `IServiceProvider` instance from the application. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsEndpointUpdateCallbackContext.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** Gets the `IResource` that is being configured for HTTPS. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsEndpointUpdateCallbackContext.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the `DistributedApplicationModel` instance. */
    public DistributedApplicationModel model() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsEndpointUpdateCallbackContext.model", reqArgs);
        return (DistributedApplicationModel) result;
    }

    /** Gets the `CancellationToken` for the operation. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsEndpointUpdateCallbackContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

}
