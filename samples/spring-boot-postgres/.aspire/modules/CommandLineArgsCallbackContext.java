// CommandLineArgsCallbackContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.CommandLineArgsCallbackContext. */
public class CommandLineArgsCallbackContext extends HandleWrapperBase {
    CommandLineArgsCallbackContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the editor used to manipulate command-line arguments in polyglot callbacks. */
    public CommandLineArgsEditor args() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/CommandLineArgsCallbackContext.args", reqArgs);
        return (CommandLineArgsEditor) result;
    }

    /** Gets the logger facade used by polyglot callbacks. */
    public LogFacade log() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/CommandLineArgsCallbackContext.log", reqArgs);
        return (LogFacade) result;
    }

    /** The resource associated with this callback context. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/CommandLineArgsCallbackContext.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the execution context associated with this callback. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/CommandLineArgsCallbackContext.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

}
