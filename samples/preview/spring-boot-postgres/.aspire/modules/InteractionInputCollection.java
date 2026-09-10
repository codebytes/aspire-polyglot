// InteractionInputCollection.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.InteractionInputCollection. */
public class InteractionInputCollection extends HandleWrapperBase {
    InteractionInputCollection(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets all inputs in declaration order. */
    public InteractionInput[] toArray() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/InteractionInputCollection.toArray", reqArgs);
        return (InteractionInput[]) result;
    }

    /** Gets the input with the specified name, or null if no input matches. */
    public InteractionInput get(String name) {
        for (var input : toArray()) {
            if (input.getName() != null && input.getName().equalsIgnoreCase(name)) {
                return input;
            }
        }
        return null;
    }

    /** Gets the input with the specified name, or throws if no input matches. */
    public InteractionInput required(String name) {
        var input = get(name);
        if (input == null) {
            throw new IllegalArgumentException("no input with name '" + name + "' was found");
        }
        return input;
    }

    /** Gets the value of the input with the specified name, or an empty string if no input matches or it has no value. */
    public String value(String name) {
        var input = get(name);
        return input == null || input.getValue() == null ? "" : input.getValue();
    }

    /** Gets the value of the input with the specified name, or throws if no input matches. */
    public String requiredValue(String name) {
        return required(name).getValue();
    }

}
