// PipelineStep.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineStep. */
public class PipelineStep extends HandleWrapperBase {
    PipelineStep(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets or initializes the unique name of the step. */
    public String name() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStep.name", reqArgs);
        return (String) result;
    }

    /** Gets or initializes the description of the step. */
    public String description() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineStep.description", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Gets or initializes the list of step names that this step depends on. */
    private AspireList<String> dependsOnStepsField;
    public AspireList<String> dependsOnSteps() {
        if (dependsOnStepsField == null) {
            dependsOnStepsField = new AspireList<>(getHandle(), getClient(), "Aspire.Hosting.Pipelines/PipelineStep.dependsOnSteps");
        }
        return dependsOnStepsField;
    }

    /** Gets or initializes the list of step names that require this step to complete before they can finish. This is used internally during pipeline construction and is converted to DependsOn relationships. */
    private AspireList<String> requiredByStepsField;
    public AspireList<String> requiredBySteps() {
        if (requiredByStepsField == null) {
            requiredByStepsField = new AspireList<>(getHandle(), getClient(), "Aspire.Hosting.Pipelines/PipelineStep.requiredBySteps");
        }
        return requiredByStepsField;
    }

    /** Gets or initializes the list of tags that categorize this step. */
    private AspireList<String> tagsField;
    public AspireList<String> tags() {
        if (tagsField == null) {
            tagsField = new AspireList<>(getHandle(), getClient(), "Aspire.Hosting.Pipelines/PipelineStep.tags");
        }
        return tagsField;
    }

    /** Adds a dependency on another step. */
    public void dependsOn(String stepName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("stepName", AspireClient.serializeValue(stepName));
        getClient().invokeCapability("Aspire.Hosting.Pipelines/dependsOn", reqArgs);
    }

    /** Specifies that this step is required by another step. This creates the inverse relationship where the other step will depend on this step. */
    public void requiredBy(String stepName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("stepName", AspireClient.serializeValue(stepName));
        getClient().invokeCapability("Aspire.Hosting.Pipelines/requiredBy", reqArgs);
    }

    /** Adds a tag to the step. */
    public void addTag(String tag) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("tag", AspireClient.serializeValue(tag));
        getClient().invokeCapability("Aspire.Hosting.Pipelines/addTag", reqArgs);
    }

}
