// PipelineContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineContext. */
public class PipelineContext extends HandleWrapperBase {
    PipelineContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the distributed application model to be deployed. */
    public DistributedApplicationModel model() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineContext.model", reqArgs);
        return (DistributedApplicationModel) result;
    }

    /** Gets the execution context for the distributed application. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineContext.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

    /** Gets the service provider for dependency resolution. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineContext.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** Gets the logger for pipeline operations. */
    public ILogger logger() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineContext.logger", reqArgs);
        return (ILogger) result;
    }

    /** Gets the cancellation token for the pipeline operation. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** Sets the CancellationToken property */
    public PipelineContext setCancellationToken(CancellationToken value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        if (value != null) {
            reqArgs.put("value", getClient().registerCancellation(value));
        }
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineContext.setCancellationToken", reqArgs);
        return (PipelineContext) result;
    }

    /** Gets the pipeline summary that steps can add information to. The summary will be displayed to users after pipeline execution completes. */
    public PipelineSummary summary() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineContext.summary", reqArgs);
        return (PipelineSummary) result;
    }

}
