// RequiredCommandValidationResult.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.RequiredCommandValidationResult. */
@SuppressWarnings({"all", "unchecked", "serial"})
public class RequiredCommandValidationResult extends HandleWrapperBase {
    RequiredCommandValidationResult(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets a value indicating whether the command validation succeeded. */
    public boolean isValid() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/RequiredCommandValidationResult.isValid", reqArgs);
        return (Boolean) result;
    }

    /** Gets an optional validation message describing why validation failed. */
    public String validationMessage() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/RequiredCommandValidationResult.validationMessage", reqArgs);
        return result == null ? null : (String) result;
    }

}
