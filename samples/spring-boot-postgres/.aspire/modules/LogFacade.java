// LogFacade.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.LogFacade. */
public class LogFacade extends HandleWrapperBase {
    LogFacade(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Writes an informational log message. */
    public void info(String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/info", reqArgs);
    }

    /** Writes a warning log message. */
    public void warning(String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/warning", reqArgs);
    }

    /** Writes an error log message. */
    public void error(String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/error", reqArgs);
    }

    /** Writes a debug log message. */
    public void debug(String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/debug", reqArgs);
    }

}
