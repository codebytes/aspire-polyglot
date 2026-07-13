// IDistributedApplicationPipeline.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.IDistributedApplicationPipeline. */
public class IDistributedApplicationPipeline extends HandleWrapperBase {
    IDistributedApplicationPipeline(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Disables the publish and deploy validation that requires build-only containers to be consumed by another resource. */
    public IDistributedApplicationPipeline disableBuildOnlyContainerValidation() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("pipeline", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/disableBuildOnlyContainerValidation", reqArgs);
        return (IDistributedApplicationPipeline) result;
    }

    /** Adds an application-level pipeline step in a TypeScript-friendly shape. */
    public void addStep(String stepName, AspireAction1<PipelineStepContext> callback, AddStepOptions options) {
        var dependsOn = options == null ? null : options.getDependsOn();
        var requiredBy = options == null ? null : options.getRequiredBy();
        addStepImpl(stepName, callback, dependsOn, requiredBy);
    }

    public void addStep(String stepName, AspireAction1<PipelineStepContext> callback) {
        addStep(stepName, callback, null);
    }

    /** Adds an application-level pipeline step in a TypeScript-friendly shape. */
    private void addStepImpl(String stepName, AspireAction1<PipelineStepContext> callback, String[] dependsOn, String[] requiredBy) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("pipeline", AspireClient.serializeValue(getHandle()));
        reqArgs.put("stepName", AspireClient.serializeValue(stepName));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (PipelineStepContext) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        if (dependsOn != null) {
            reqArgs.put("dependsOn", AspireClient.serializeValue(dependsOn));
        }
        if (requiredBy != null) {
            reqArgs.put("requiredBy", AspireClient.serializeValue(requiredBy));
        }
        getClient().invokeCapability("Aspire.Hosting/addStep", reqArgs);
    }

    /** Registers a pipeline configuration callback in a TypeScript-friendly shape. */
    public void configure(AspireAction1<PipelineConfigurationContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("pipeline", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (PipelineConfigurationContext) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/configure", reqArgs);
    }

}
