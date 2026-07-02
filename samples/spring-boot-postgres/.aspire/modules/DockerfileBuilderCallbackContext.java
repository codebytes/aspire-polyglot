// DockerfileBuilderCallbackContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.DockerfileBuilderCallbackContext. */
public class DockerfileBuilderCallbackContext extends HandleWrapperBase {
    DockerfileBuilderCallbackContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource being built. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/DockerfileBuilderCallbackContext.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the Dockerfile builder instance. */
    public DockerfileBuilder builder() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/DockerfileBuilderCallbackContext.builder", reqArgs);
        return (DockerfileBuilder) result;
    }

    /** Gets the service provider for dependency injection. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/DockerfileBuilderCallbackContext.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** Gets the cancellation token to observe while waiting for the task to complete. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/DockerfileBuilderCallbackContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

}
