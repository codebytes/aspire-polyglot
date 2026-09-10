// InputsInteractionResult.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Ats.InputsInteractionResult. */
public class InputsInteractionResult extends HandleWrapperBase {
    InputsInteractionResult(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets a value indicating whether the interaction was canceled by the user. */
    public boolean canceled() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/InputsInteractionResult.canceled", reqArgs);
        return (Boolean) result;
    }

    /** Gets the inputs returned from the interaction. Empty when `Canceled` is `true`. */
    public InteractionInputCollection inputs() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/InputsInteractionResult.inputs", reqArgs);
        return (InteractionInputCollection) result;
    }

}
