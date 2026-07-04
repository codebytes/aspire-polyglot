// ContainerMountAnnotation.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerMountAnnotation. */
public class ContainerMountAnnotation extends HandleWrapperBase {
    ContainerMountAnnotation(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the source of the bind mount or name if a volume. Can be `null` if the mount is an anonymous volume. */
    public String source() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerMountAnnotation.source", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Gets the target of the mount. */
    public String target() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerMountAnnotation.target", reqArgs);
        return (String) result;
    }

    /** Gets the type of the mount. */
    public ContainerMountType type() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerMountAnnotation.type", reqArgs);
        return ContainerMountType.fromValue((String) result);
    }

    /** Gets a value indicating whether the volume mount is read-only. */
    public boolean isReadOnly() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerMountAnnotation.isReadOnly", reqArgs);
        return (Boolean) result;
    }

}
