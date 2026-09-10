// InteractionInputBuilder.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Ats.InteractionInputBuilder. */
public class InteractionInputBuilder extends HandleWrapperBase {
    InteractionInputBuilder(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Sets the choice options for the input. */
    public InteractionInputBuilder withChoiceOptions(InteractionChoiceOption[] choices) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("choices", AspireClient.serializeValue(choices));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/withChoiceOptions", reqArgs);
        return (InteractionInputBuilder) result;
    }

    /** Sets the value of the input. */
    public InteractionInputBuilder withValue(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/withValue", reqArgs);
        return (InteractionInputBuilder) result;
    }

    public InteractionInputBuilder withDynamicLoading(AspireAction1<InteractionInputLoadContext> callback) {
        return withDynamicLoading(callback, null);
    }

    /** Attaches a callback that dynamically loads or updates the input after the prompt starts. */
    public InteractionInputBuilder withDynamicLoading(AspireAction1<InteractionInputLoadContext> callback, DynamicLoadingOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (InteractionInputLoadContext) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/withDynamicLoading", reqArgs);
        return (InteractionInputBuilder) result;
    }

}
