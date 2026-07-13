// DockerfileFactoryContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.DockerfileFactoryContext. */
public class DockerfileFactoryContext extends HandleWrapperBase {
    DockerfileFactoryContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource for which the Dockerfile is being generated. This allows factory functions to query resource annotations and properties to customize the generated Dockerfile. ``` var containerAnnotation = context.Resource.Annotations.OfType<ContainerImageAnnotation>().FirstOrDefault(); var baseImage = containerAnnotation?.Image ?? "alpine:latest"; ``` */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/DockerfileFactoryContext.resource", reqArgs);
        return (IResource) result;
    }

}
