// PipelineStepContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineStepContext. */
public class PipelineStepContext extends HandleWrapperBase {
    PipelineStepContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the pipeline context shared across all steps. */
    public PipelineContext pipelineContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepContext.pipelineContext", reqArgs);
        return (PipelineContext) result;
    }

    /** Gets the publishing step associated with this specific step execution. */
    public IReportingStep reportingStep() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepContext.reportingStep", reqArgs);
        return (IReportingStep) result;
    }

    /** Gets the distributed application model to be deployed. */
    public DistributedApplicationModel model() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepContext.model", reqArgs);
        return (DistributedApplicationModel) result;
    }

    /** Gets the execution context for the distributed application. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepContext.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

    /** Gets the service provider for dependency resolution. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepContext.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** Gets the logger for pipeline operations that writes to both the pipeline logger and the step logger. */
    public ILogger logger() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepContext.logger", reqArgs);
        return (ILogger) result;
    }

    /** Gets the cancellation token for the pipeline operation. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** Gets the pipeline summary that steps can add information to. The summary will be displayed to users after pipeline execution completes. */
    public PipelineSummary summary() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStepContext.summary", reqArgs);
        return (PipelineSummary) result;
    }

}
