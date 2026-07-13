// IResourceWithConnectionString.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.IResourceWithConnectionString. */
public class IResourceWithConnectionString extends ResourceBuilderBase {
    IResourceWithConnectionString(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public IResourceWithConnectionString withConnectionProperty(String name, String value) {
        return withConnectionProperty(name, AspireUnion.of(value));
    }

    public IResourceWithConnectionString withConnectionProperty(String name, ReferenceExpression value) {
        return withConnectionProperty(name, AspireUnion.of(value));
    }

    /** Adds a connection property annotation to the resource being built. */
    public IResourceWithConnectionString withConnectionProperty(String name, AspireUnion value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/withConnectionProperty", reqArgs);
        return this;
    }

    /** Retrieves the value of a specified connection property from the resource's connection properties. */
    public ReferenceExpression getConnectionProperty(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("resource", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        var result = getClient().invokeCapability("Aspire.Hosting/getConnectionProperty", reqArgs);
        return (ReferenceExpression) result;
    }

    /** Subscribes to the ConnectionStringAvailable event. */
    public IResourceWithConnectionString onConnectionStringAvailable(AspireAction1<ConnectionStringAvailableEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ConnectionStringAvailableEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onConnectionStringAvailable", reqArgs);
        return this;
    }

}
