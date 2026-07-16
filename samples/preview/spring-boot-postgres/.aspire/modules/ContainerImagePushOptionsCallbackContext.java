// ContainerImagePushOptionsCallbackContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerImagePushOptionsCallbackContext. */
public class ContainerImagePushOptionsCallbackContext extends HandleWrapperBase {
    ContainerImagePushOptionsCallbackContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource being configured for container image push operations. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImagePushOptionsCallbackContext.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the cancellation token to observe while configuring image push options. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImagePushOptionsCallbackContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** Gets the container image push options that can be modified by the callback. */
    public ContainerImagePushOptions options() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImagePushOptionsCallbackContext.options", reqArgs);
        return (ContainerImagePushOptions) result;
    }

}
