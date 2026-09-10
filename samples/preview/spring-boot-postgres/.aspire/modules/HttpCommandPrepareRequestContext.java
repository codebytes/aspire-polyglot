// HttpCommandPrepareRequestContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.HttpCommandPrepareRequestContext. */
public class HttpCommandPrepareRequestContext extends HandleWrapperBase {
    HttpCommandPrepareRequestContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** The name of the resource the command was configured on. */
    public String resourceName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpCommandPrepareRequestContext.resourceName", reqArgs);
        return (String) result;
    }

    /** The endpoint the request is targeting. */
    public EndpointReference endpoint() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpCommandPrepareRequestContext.endpoint", reqArgs);
        return (EndpointReference) result;
    }

    /** The cancellation token. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpCommandPrepareRequestContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** Gets the invocation arguments supplied by the client when the command is executed. */
    public InteractionInputCollection arguments() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpCommandPrepareRequestContext.arguments", reqArgs);
        return (InteractionInputCollection) result;
    }

}
