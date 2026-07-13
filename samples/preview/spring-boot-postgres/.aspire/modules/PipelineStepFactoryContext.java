// PipelineStepFactoryContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineStepFactoryContext. */
public class PipelineStepFactoryContext extends HandleWrapperBase {
    PipelineStepFactoryContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the pipeline context that has the model and other properties. */
    public PipelineContext pipelineContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepFactoryContext.pipelineContext", reqArgs);
        return (PipelineContext) result;
    }

    /** Gets the resource that this factory is associated with. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepFactoryContext.resource", reqArgs);
        return (IResource) result;
    }

}
