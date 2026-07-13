// PipelineConfigurationContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineConfigurationContext. */
public class PipelineConfigurationContext extends HandleWrapperBase {
    PipelineConfigurationContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the pipeline editor used by polyglot callbacks. */
    public PipelineEditor pipeline() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineConfigurationContext.pipeline", reqArgs);
        return (PipelineEditor) result;
    }

    /** Gets the logger facade used by polyglot callbacks. */
    public LogFacade log() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineConfigurationContext.log", reqArgs);
        return (LogFacade) result;
    }

    /** Gets all pipeline steps with the specified tag. */
    public PipelineStep[] getSteps(String tag) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("tag", AspireClient.serializeValue(tag));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/getSteps", reqArgs);
        return (PipelineStep[]) result;
    }

}
