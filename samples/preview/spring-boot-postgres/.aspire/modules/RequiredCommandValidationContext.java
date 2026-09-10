// RequiredCommandValidationContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.RequiredCommandValidationContext. */
public class RequiredCommandValidationContext extends HandleWrapperBase {
    RequiredCommandValidationContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resolved full path to the command executable. */
    public String resolvedPath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/RequiredCommandValidationContext.resolvedPath", reqArgs);
        return (String) result;
    }

    /** Gets the service provider for accessing application services. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/RequiredCommandValidationContext.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** Gets a cancellation token that can be used to cancel the validation. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/RequiredCommandValidationContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** Creates a successful validation result. */
    public RequiredCommandValidationResult success() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/RequiredCommandValidationContext.success", reqArgs);
        return (RequiredCommandValidationResult) result;
    }

    /** Creates a failed validation result with the specified message. */
    public RequiredCommandValidationResult failure(String validationMessage) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("validationMessage", AspireClient.serializeValue(validationMessage));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/RequiredCommandValidationContext.failure", reqArgs);
        return (RequiredCommandValidationResult) result;
    }

}
