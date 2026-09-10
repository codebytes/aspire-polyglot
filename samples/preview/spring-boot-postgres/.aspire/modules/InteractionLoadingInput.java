// InteractionLoadingInput.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Ats.InteractionLoadingInput. */
public class InteractionLoadingInput extends HandleWrapperBase {
    InteractionLoadingInput(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the name of the input. */
    public String getName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/getName", reqArgs);
        return (String) result;
    }

    /** Sets the choice options for the input. */
    public void setChoiceOptions(InteractionChoiceOption[] choices) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("choices", AspireClient.serializeValue(choices));
        getClient().invokeCapability("Aspire.Hosting.Ats/setChoiceOptions", reqArgs);
    }

    /** Sets the value of the input. */
    public void setValue(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting.Ats/setValue", reqArgs);
    }

}
