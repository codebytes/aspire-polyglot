// EnvironmentCallbackContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.EnvironmentCallbackContext. */
public class EnvironmentCallbackContext extends HandleWrapperBase {
    EnvironmentCallbackContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the editor used to set environment variables in polyglot callbacks. */
    public EnvironmentEditor environment() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EnvironmentCallbackContext.environment", reqArgs);
        return (EnvironmentEditor) result;
    }

    /** Gets the logger facade used by polyglot callbacks. */
    public LogFacade log() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EnvironmentCallbackContext.log", reqArgs);
        return (LogFacade) result;
    }

    /** The resource associated with this callback context. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EnvironmentCallbackContext.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the execution context associated with this invocation of the AppHost. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EnvironmentCallbackContext.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

}
