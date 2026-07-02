// ILoggerFactory.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Microsoft.Extensions.Logging.Abstractions/Microsoft.Extensions.Logging.ILoggerFactory. */
public class ILoggerFactory extends HandleWrapperBase {
    ILoggerFactory(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Creates a logger for the specified category name. */
    public ILogger createLogger(String categoryName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("loggerFactory", AspireClient.serializeValue(getHandle()));
        reqArgs.put("categoryName", AspireClient.serializeValue(categoryName));
        var result = getClient().invokeCapability("Aspire.Hosting/createLogger", reqArgs);
        return (ILogger) result;
    }

}
