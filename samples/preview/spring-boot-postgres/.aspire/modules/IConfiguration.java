// IConfiguration.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Microsoft.Extensions.Configuration.Abstractions/Microsoft.Extensions.Configuration.IConfiguration. */
public class IConfiguration extends HandleWrapperBase {
    IConfiguration(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets a configuration value by key. */
    public String getConfigValue(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("configuration", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        var result = getClient().invokeCapability("Aspire.Hosting/getConfigValue", reqArgs);
        return (String) result;
    }

    /** Gets a connection string by name. */
    public String getConnectionString(String name) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("configuration", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        var result = getClient().invokeCapability("Aspire.Hosting/getConnectionString", reqArgs);
        return (String) result;
    }

    /** Gets a configuration section by key. */
    public IConfigurationSection getSection(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("configuration", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        var result = getClient().invokeCapability("Aspire.Hosting/getSection", reqArgs);
        return (IConfigurationSection) result;
    }

    /** Gets the child sections of a configuration handle. */
    public IConfigurationSection[] getChildren() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("configuration", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getChildren", reqArgs);
        return (IConfigurationSection[]) result;
    }

    /** Checks whether a configuration section exists. */
    public boolean exists(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("configuration", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        var result = getClient().invokeCapability("Aspire.Hosting/exists", reqArgs);
        return (Boolean) result;
    }

}
