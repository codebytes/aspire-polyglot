// DistributedApplicationExecutionContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.DistributedApplicationExecutionContext. */
public class DistributedApplicationExecutionContext extends HandleWrapperBase {
    DistributedApplicationExecutionContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** The name of the publisher that is being used if `Operation` is set to `Publish`. */
    public String publisherName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/DistributedApplicationExecutionContext.publisherName", reqArgs);
        return (String) result;
    }

    /** Sets the PublisherName property */
    public DistributedApplicationExecutionContext setPublisherName(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting/DistributedApplicationExecutionContext.setPublisherName", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

    /** The operation currently being performed by the AppHost. */
    public DistributedApplicationOperation operation() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/DistributedApplicationExecutionContext.operation", reqArgs);
        return DistributedApplicationOperation.fromValue((String) result);
    }

    /** The `IServiceProvider` for the AppHost. */
    public IServiceProvider serviceProvider() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/DistributedApplicationExecutionContext.serviceProvider", reqArgs);
        return (IServiceProvider) result;
    }

    /** Returns true if the current operation is publishing. */
    public boolean isPublishMode() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/DistributedApplicationExecutionContext.isPublishMode", reqArgs);
        return (Boolean) result;
    }

    /** Returns true if the current operation is running. */
    public boolean isRunMode() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/DistributedApplicationExecutionContext.isRunMode", reqArgs);
        return (Boolean) result;
    }

}
