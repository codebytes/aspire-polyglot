// ILogger.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Microsoft.Extensions.Logging.Abstractions/Microsoft.Extensions.Logging.ILogger. */
public class ILogger extends HandleWrapperBase {
    ILogger(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Logs an information message. */
    public void logInformation(String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("logger", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting/logInformation", reqArgs);
    }

    /** Logs a warning message. */
    public void logWarning(String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("logger", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting/logWarning", reqArgs);
    }

    /** Logs an error message. */
    public void logError(String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("logger", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting/logError", reqArgs);
    }

    /** Logs a debug message. */
    public void logDebug(String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("logger", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting/logDebug", reqArgs);
    }

    /** Logs a message with a specified log level. */
    public void log(String level, String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("logger", AspireClient.serializeValue(getHandle()));
        reqArgs.put("level", AspireClient.serializeValue(level));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting/log", reqArgs);
    }

}
