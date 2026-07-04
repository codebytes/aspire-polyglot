// ExecuteCommandContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ExecuteCommandContext. */
public class ExecuteCommandContext extends HandleWrapperBase {
    ExecuteCommandContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** The resource name. */
    public String resourceName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ExecuteCommandContext.resourceName", reqArgs);
        return (String) result;
    }

    /** The cancellation token. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ExecuteCommandContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** The logger for the resource. */
    public ILogger logger() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ExecuteCommandContext.logger", reqArgs);
        return (ILogger) result;
    }

    /** Gets the invocation arguments supplied by the client when the command is executed. */
    public InteractionInputCollection arguments() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ExecuteCommandContext.arguments", reqArgs);
        return (InteractionInputCollection) result;
    }

}
