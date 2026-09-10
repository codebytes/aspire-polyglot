// ProgressContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ProgressContext. */
public class ProgressContext extends HandleWrapperBase {
    ProgressContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the `CancellationToken` that is triggered when the user clicks the cancel button or the operation is externally canceled. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/ProgressContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

}
