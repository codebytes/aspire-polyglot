// InputsDialogValidationContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.InputsDialogValidationContext. */
public class InputsDialogValidationContext extends HandleWrapperBase {
    InputsDialogValidationContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the inputs that are being validated. */
    public InteractionInputCollection inputs() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/InputsDialogValidationContext.inputs", reqArgs);
        return (InteractionInputCollection) result;
    }

    /** Gets the cancellation token for the validation operation. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/InputsDialogValidationContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** Adds a validation error for the input with the specified name. */
    public void addValidationError(String inputName, String errorMessage) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("inputName", AspireClient.serializeValue(inputName));
        reqArgs.put("errorMessage", AspireClient.serializeValue(errorMessage));
        getClient().invokeCapability("Aspire.Hosting/InputsDialogValidationContext.addValidationError", reqArgs);
    }

}
