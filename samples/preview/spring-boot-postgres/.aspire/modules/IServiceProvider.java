// IServiceProvider.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for System.ComponentModel/System.IServiceProvider. */
public class IServiceProvider extends HandleWrapperBase {
    IServiceProvider(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the distributed application eventing service from the service provider. */
    public IDistributedApplicationEventing getEventing() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("serviceProvider", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getEventing", reqArgs);
        return (IDistributedApplicationEventing) result;
    }

    /** Gets the logger factory from the service provider. */
    public ILoggerFactory getLoggerFactory() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("serviceProvider", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getLoggerFactory", reqArgs);
        return (ILoggerFactory) result;
    }

    /** Gets the resource logger service from the service provider. */
    public ResourceLoggerService getResourceLoggerService() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("serviceProvider", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getResourceLoggerService", reqArgs);
        return (ResourceLoggerService) result;
    }

    /** Gets the distributed application model from the service provider. */
    public DistributedApplicationModel getDistributedApplicationModel() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("serviceProvider", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getDistributedApplicationModel", reqArgs);
        return (DistributedApplicationModel) result;
    }

    /** Gets the resource notification service from the service provider. */
    public ResourceNotificationService getResourceNotificationService() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("serviceProvider", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getResourceNotificationService", reqArgs);
        return (ResourceNotificationService) result;
    }

    /** Gets the resource command service from the service provider. */
    public ResourceCommandService getResourceCommandService() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("serviceProvider", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getResourceCommandService", reqArgs);
        return (ResourceCommandService) result;
    }

    /** Gets the Aspire store from the service provider. */
    public IAspireStore getAspireStore() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("serviceProvider", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getAspireStore", reqArgs);
        return (IAspireStore) result;
    }

    /** Gets the user secrets manager from the service provider. */
    public IUserSecretsManager getUserSecretsManager() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("serviceProvider", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getUserSecretsManager", reqArgs);
        return (IUserSecretsManager) result;
    }

}
