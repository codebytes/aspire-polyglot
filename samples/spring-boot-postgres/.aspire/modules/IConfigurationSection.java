// IConfigurationSection.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Microsoft.Extensions.Configuration.Abstractions/Microsoft.Extensions.Configuration.IConfigurationSection. */
public class IConfigurationSection extends HandleWrapperBase {
    IConfigurationSection(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the Key property */
    public String key() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Microsoft.Extensions.Configuration/IConfigurationSection.key", reqArgs);
        return (String) result;
    }

    /** Gets the Path property */
    public String path() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Microsoft.Extensions.Configuration/IConfigurationSection.path", reqArgs);
        return (String) result;
    }

    /** Gets the Value property */
    public String value() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Microsoft.Extensions.Configuration/IConfigurationSection.value", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Sets the Value property */
    public IConfigurationSection setValue(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Microsoft.Extensions.Configuration/IConfigurationSection.setValue", reqArgs);
        return (IConfigurationSection) result;
    }

}
