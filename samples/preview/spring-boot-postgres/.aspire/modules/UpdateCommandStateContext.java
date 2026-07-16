// UpdateCommandStateContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.UpdateCommandStateContext. */
public class UpdateCommandStateContext extends HandleWrapperBase {
    UpdateCommandStateContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource snapshot data available to polyglot command state callbacks. */
    public UpdateCommandStateResourceSnapshot resourceSnapshot() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/UpdateCommandStateContext.resourceSnapshot", reqArgs);
        return UpdateCommandStateResourceSnapshot.fromMap((Map<String, Object>) result);
    }

}
