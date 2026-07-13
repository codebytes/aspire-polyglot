// IResourceWithContainerFiles.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.IResourceWithContainerFiles. */
public class IResourceWithContainerFiles extends ResourceBuilderBase {
    IResourceWithContainerFiles(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Adds a container files source annotation to the resource being built, specifying the path to the container files source. */
    public IResourceWithContainerFiles withContainerFilesSource(String sourcePath) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("sourcePath", AspireClient.serializeValue(sourcePath));
        getClient().invokeCapability("Aspire.Hosting/withContainerFilesSource", reqArgs);
        return this;
    }

    /** Removes any container files source annotation from the resource being built. */
    public IResourceWithContainerFiles clearContainerFilesSources() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/clearContainerFilesSources", reqArgs);
        return this;
    }

}
