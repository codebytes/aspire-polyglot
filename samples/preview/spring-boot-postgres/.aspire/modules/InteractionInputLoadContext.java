// InteractionInputLoadContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Ats.InteractionInputLoadContext. */
public class InteractionInputLoadContext extends HandleWrapperBase {
    InteractionInputLoadContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets all inputs in the prompt, including the one currently loading. */
    public InteractionInputCollection inputs() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/InteractionInputLoadContext.inputs", reqArgs);
        return (InteractionInputCollection) result;
    }

    /** Gets a handle to the input that is loading. Mutate the input through this handle. */
    public InteractionLoadingInput input() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/input", reqArgs);
        return (InteractionLoadingInput) result;
    }

}
