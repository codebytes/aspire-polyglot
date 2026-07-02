// IHostEnvironment.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Microsoft.Extensions.Hosting.Abstractions/Microsoft.Extensions.Hosting.IHostEnvironment. */
public class IHostEnvironment extends HandleWrapperBase {
    IHostEnvironment(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the EnvironmentName property */
    public String environmentName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Microsoft.Extensions.Hosting/IHostEnvironment.environmentName", reqArgs);
        return (String) result;
    }

    /** Sets the EnvironmentName property */
    public IHostEnvironment setEnvironmentName(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Microsoft.Extensions.Hosting/IHostEnvironment.setEnvironmentName", reqArgs);
        return (IHostEnvironment) result;
    }

    /** Gets the ApplicationName property */
    public String applicationName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Microsoft.Extensions.Hosting/IHostEnvironment.applicationName", reqArgs);
        return (String) result;
    }

    /** Sets the ApplicationName property */
    public IHostEnvironment setApplicationName(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Microsoft.Extensions.Hosting/IHostEnvironment.setApplicationName", reqArgs);
        return (IHostEnvironment) result;
    }

    /** Gets the ContentRootPath property */
    public String contentRootPath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Microsoft.Extensions.Hosting/IHostEnvironment.contentRootPath", reqArgs);
        return (String) result;
    }

    /** Sets the ContentRootPath property */
    public IHostEnvironment setContentRootPath(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Microsoft.Extensions.Hosting/IHostEnvironment.setContentRootPath", reqArgs);
        return (IHostEnvironment) result;
    }

    /** Checks if the environment is Development. */
    public boolean isDevelopment() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("environment", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/isDevelopment", reqArgs);
        return (Boolean) result;
    }

    /** Checks if the environment is Production. */
    public boolean isProduction() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("environment", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/isProduction", reqArgs);
        return (Boolean) result;
    }

    /** Checks if the environment is Staging. */
    public boolean isStaging() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("environment", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/isStaging", reqArgs);
        return (Boolean) result;
    }

    /** Checks if the environment matches the specified name. */
    public boolean isEnvironment(String environmentName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("environment", AspireClient.serializeValue(getHandle()));
        reqArgs.put("environmentName", AspireClient.serializeValue(environmentName));
        var result = getClient().invokeCapability("Aspire.Hosting/isEnvironment", reqArgs);
        return (Boolean) result;
    }

}
