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

}
