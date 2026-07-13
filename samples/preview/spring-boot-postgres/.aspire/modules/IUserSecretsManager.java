// IUserSecretsManager.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.IUserSecretsManager. */
public class IUserSecretsManager extends HandleWrapperBase {
    IUserSecretsManager(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets a value indicating whether user secrets are available. */
    public boolean isAvailable() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/IUserSecretsManager.isAvailable", reqArgs);
        return (Boolean) result;
    }

    /** Gets the path to the user secrets file. */
    public String filePath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/IUserSecretsManager.filePath", reqArgs);
        return (String) result;
    }

    /** Attempts to set a user secret value synchronously. */
    public boolean trySetSecret(String name, String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting/IUserSecretsManager.trySetSecret", reqArgs);
        return (Boolean) result;
    }

    /** Attempts to delete a user secret value synchronously. */
    public boolean tryDeleteSecret(String name) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        var result = getClient().invokeCapability("Aspire.Hosting/IUserSecretsManager.tryDeleteSecret", reqArgs);
        return (Boolean) result;
    }

    public void saveStateJson(String json) {
        saveStateJson(json, null);
    }

    /** Saves state to user secrets from a JSON string. */
    public void saveStateJson(String json, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("userSecretsManager", AspireClient.serializeValue(getHandle()));
        reqArgs.put("json", AspireClient.serializeValue(json));
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        getClient().invokeCapability("Aspire.Hosting/saveStateJson", reqArgs);
    }

    /** Gets a secret value if it exists in configuration, or sets it to the provided value if it does not. */
    public void getOrSetSecret(IResource resourceBuilder, String name, String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("userSecretsManager", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceBuilder", AspireClient.serializeValue(resourceBuilder));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/getOrSetSecret", reqArgs);
    }

    public void getOrSetSecret(ResourceBuilderBase resourceBuilder, String name, String value) {
        getOrSetSecret(new IResource(resourceBuilder.getHandle(), resourceBuilder.getClient()), name, value);
    }

}
