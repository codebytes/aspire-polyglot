// HandleWrapperBase.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;

/**
 * HandleWrapperBase is the base class for all handle wrappers.
 */
public class HandleWrapperBase {
    private final Handle handle;
    private final AspireClient client;

    HandleWrapperBase(Handle handle, AspireClient client) {
        this.handle = handle;
        this.client = client;
    }

    Handle getHandle() {
        return handle;
    }

    AspireClient getClient() {
        return client;
    }
}
