// PipelineEditor.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineEditor. */
@SuppressWarnings({"all", "unchecked", "serial"})
public class PipelineEditor extends HandleWrapperBase {
    PipelineEditor(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets all configured pipeline steps. */
    public PipelineStep[] steps() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/steps", reqArgs);
        return (PipelineStep[]) AspireClient.convertArray(result, PipelineStep[].class.getComponentType(), item0 -> (PipelineStep) item0);
    }

    /** Gets all pipeline steps that have the specified tag. */
    public PipelineStep[] stepsByTag(String tag) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("tag", AspireClient.serializeValue(tag));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/stepsByTag", reqArgs);
        return (PipelineStep[]) AspireClient.convertArray(result, PipelineStep[].class.getComponentType(), item0 -> (PipelineStep) item0);
    }

}
