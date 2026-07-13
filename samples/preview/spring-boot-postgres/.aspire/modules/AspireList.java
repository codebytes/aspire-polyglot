// AspireList.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;

/**
 * AspireList is a handle-backed list with lazy handle resolution.
 */
public class AspireList<T> extends HandleWrapperBase {
    private final String getterCapabilityId;
    private Handle resolvedHandle;

    AspireList(Handle handle, AspireClient client) {
        super(handle, client);
        this.getterCapabilityId = null;
        this.resolvedHandle = handle;
    }

    AspireList(Handle contextHandle, AspireClient client, String getterCapabilityId) {
        super(contextHandle, client);
        this.getterCapabilityId = getterCapabilityId;
        this.resolvedHandle = null;
    }

    private Handle ensureHandle() {
        if (resolvedHandle != null) {
            return resolvedHandle;
        }
        if (getterCapabilityId != null) {
            Map<String, Object> args = new HashMap<>();
            args.put("context", getHandle().toJson());
            Object result = getClient().invokeCapability(getterCapabilityId, args);
            if (result instanceof Handle handle) {
                resolvedHandle = handle;
            }
        }
        if (resolvedHandle == null) {
            resolvedHandle = getHandle();
        }
        return resolvedHandle;
    }
}
