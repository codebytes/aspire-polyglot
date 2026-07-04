// ExternalServiceResource.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ExternalServiceResource. */
public class ExternalServiceResource extends ResourceBuilderBase {
    ExternalServiceResource(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Configures the resource to use the specified container registry for container image operations. */
    public ExternalServiceResource withContainerRegistry(IResource registry) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("registry", AspireClient.serializeValue(registry));
        getClient().invokeCapability("Aspire.Hosting/withContainerRegistry", reqArgs);
        return this;
    }

    public ExternalServiceResource withContainerRegistry(ResourceBuilderBase registry) {
        return withContainerRegistry(new IResource(registry.getHandle(), registry.getClient()));
    }

    /** Configures custom base images for generated Dockerfiles. */
    public ExternalServiceResource withDockerfileBaseImage(WithDockerfileBaseImageOptions options) {
        var buildImage = options == null ? null : options.getBuildImage();
        var runtimeImage = options == null ? null : options.getRuntimeImage();
        return withDockerfileBaseImageImpl(buildImage, runtimeImage);
    }

    public ExternalServiceResource withDockerfileBaseImage() {
        return withDockerfileBaseImage(null);
    }

    /** Configures custom base images for generated Dockerfiles. */
    private ExternalServiceResource withDockerfileBaseImageImpl(String buildImage, String runtimeImage) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (buildImage != null) {
            reqArgs.put("buildImage", AspireClient.serializeValue(buildImage));
        }
        if (runtimeImage != null) {
            reqArgs.put("runtimeImage", AspireClient.serializeValue(runtimeImage));
        }
        getClient().invokeCapability("Aspire.Hosting/withDockerfileBaseImage", reqArgs);
        return this;
    }

    /** Adds an HTTP health check to the external service for polyglot app hosts. */
    public ExternalServiceResource withHttpHealthCheck(WithHttpHealthCheckOptions options) {
        var path = options == null ? null : options.getPath();
        var statusCode = options == null ? null : options.getStatusCode();
        var endpointName = options == null ? null : options.getEndpointName();
        return withHttpHealthCheckImpl(path, statusCode, endpointName);
    }

    public ExternalServiceResource withHttpHealthCheck() {
        return withHttpHealthCheck(null);
    }

    /** Adds an HTTP health check to the external service for polyglot app hosts. */
    private ExternalServiceResource withHttpHealthCheckImpl(String path, Double statusCode, String endpointName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (path != null) {
            reqArgs.put("path", AspireClient.serializeValue(path));
        }
        if (statusCode != null) {
            reqArgs.put("statusCode", AspireClient.serializeValue(statusCode));
        }
        if (endpointName != null) {
            reqArgs.put("endpointName", AspireClient.serializeValue(endpointName));
        }
        getClient().invokeCapability("Aspire.Hosting/withExternalServiceHttpHealthCheck", reqArgs);
        return this;
    }

    public ExternalServiceResource withRequiredCommand(String command) {
        return withRequiredCommand(command, null);
    }

    /** Declares that a resource requires a specific command/executable to be available on the local machine PATH before it can start. */
    public ExternalServiceResource withRequiredCommand(String command, String helpLink) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("command", AspireClient.serializeValue(command));
        if (helpLink != null) {
            reqArgs.put("helpLink", AspireClient.serializeValue(helpLink));
        }
        getClient().invokeCapability("Aspire.Hosting/withRequiredCommand", reqArgs);
        return this;
    }

    /** Configures a resource to use a session lifetime. */
    public ExternalServiceResource withSessionLifetime() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withSessionLifetime", reqArgs);
        return this;
    }

    /** Configures a resource to use a persistent lifetime. */
    public ExternalServiceResource withPersistentLifetime() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withPersistentLifetime", reqArgs);
        return this;
    }

    /** Configures a resource to match the lifetime of another resource. */
    public ExternalServiceResource withLifetimeOf(IResource sourceBuilder) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("sourceBuilder", AspireClient.serializeValue(sourceBuilder));
        getClient().invokeCapability("Aspire.Hosting/withLifetimeOf", reqArgs);
        return this;
    }

    public ExternalServiceResource withLifetimeOf(ResourceBuilderBase sourceBuilder) {
        return withLifetimeOf(new IResource(sourceBuilder.getHandle(), sourceBuilder.getClient()));
    }

    /** Configures a resource to use a persistent lifetime that ends when a parent process exits. */
    public ExternalServiceResource withParentProcessLifetime(double parentProcessId) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("parentProcessId", AspireClient.serializeValue(parentProcessId));
        getClient().invokeCapability("Aspire.Hosting/withParentProcessLifetime", reqArgs);
        return this;
    }

    /** Registers a callback to customize the URLs displayed for the resource. */
    public ExternalServiceResource withUrls(AspireAction1<ResourceUrlsCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (ResourceUrlsCallbackContext) args[0];
            callback.invoke(obj);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withUrls", reqArgs);
        return this;
    }

    public ExternalServiceResource withUrl(String url, String displayText) {
        return withUrl(AspireUnion.of(url), displayText);
    }

    public ExternalServiceResource withUrl(ReferenceExpression url, String displayText) {
        return withUrl(AspireUnion.of(url), displayText);
    }

    public ExternalServiceResource withUrl(AspireUnion url) {
        return withUrl(url, null);
    }

    /** Adds or modifies displayed URLs */
    public ExternalServiceResource withUrl(AspireUnion url, String displayText) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("url", AspireClient.serializeValue(url));
        if (displayText != null) {
            reqArgs.put("displayText", AspireClient.serializeValue(displayText));
        }
        getClient().invokeCapability("Aspire.Hosting/withUrl", reqArgs);
        return this;
    }

    /** Registers a callback to update the URL displayed for the endpoint with the specified name. */
    public ExternalServiceResource withUrlForEndpoint(String endpointName, AspireAction1<ResourceUrlAnnotation> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("endpointName", AspireClient.serializeValue(endpointName));
        var callbackId = getClient().registerCallback(args -> {
            var obj = ResourceUrlAnnotation.fromMap((Map<String, Object>) args[0]);
            callback.invoke(obj);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withUrlForEndpoint", reqArgs);
        return this;
    }

    /** Excludes a resource from being published to the manifest. */
    public ExternalServiceResource excludeFromManifest() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/excludeFromManifest", reqArgs);
        return this;
    }

    /** Prevents resource from starting automatically */
    public ExternalServiceResource withExplicitStart() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withExplicitStart", reqArgs);
        return this;
    }

    /** Adds a health check by key */
    public ExternalServiceResource withHealthCheck(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        getClient().invokeCapability("Aspire.Hosting/withHealthCheck", reqArgs);
        return this;
    }

    public ExternalServiceResource withCommand(String name, String displayName, AspireFunc1<ExecuteCommandContext, ExecuteCommandResult> executeCommand) {
        return withCommand(name, displayName, executeCommand, null);
    }

    /** Adds a resource command */
    public ExternalServiceResource withCommand(String name, String displayName, AspireFunc1<ExecuteCommandContext, ExecuteCommandResult> executeCommand, CommandOptions commandOptions) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("displayName", AspireClient.serializeValue(displayName));
        var executeCommandId = getClient().registerCallback(args -> {
            var arg = (ExecuteCommandContext) args[0];
            return AspireClient.awaitValue(executeCommand.invoke(arg));
        });
        if (executeCommandId != null) {
            reqArgs.put("executeCommand", executeCommandId);
        }
        if (commandOptions != null) {
            reqArgs.put("commandOptions", AspireClient.serializeValue(commandOptions));
        }
        getClient().invokeCapability("Aspire.Hosting/withCommand", reqArgs);
        return this;
    }

    /** Adds a command to the resource that starts a local process when invoked. */
    public ExternalServiceResource withProcessCommand(String commandName, String displayName, ProcessCommandExportOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("commandName", AspireClient.serializeValue(commandName));
        reqArgs.put("displayName", AspireClient.serializeValue(displayName));
        reqArgs.put("options", AspireClient.serializeValue(options));
        getClient().invokeCapability("Aspire.Hosting/withProcessCommand", reqArgs);
        return this;
    }

    public ExternalServiceResource withProcessCommandFactory(String commandName, String displayName, AspireFunc1<ExecuteCommandContext, ProcessCommandSpecExportData> createProcessSpec) {
        return withProcessCommandFactory(commandName, displayName, createProcessSpec, null);
    }

    /** Adds a command to the resource that starts a local process created by a callback when invoked. */
    public ExternalServiceResource withProcessCommandFactory(String commandName, String displayName, AspireFunc1<ExecuteCommandContext, ProcessCommandSpecExportData> createProcessSpec, ProcessCommandResultExportOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("commandName", AspireClient.serializeValue(commandName));
        reqArgs.put("displayName", AspireClient.serializeValue(displayName));
        var createProcessSpecId = getClient().registerCallback(args -> {
            var arg = (ExecuteCommandContext) args[0];
            return AspireClient.awaitValue(createProcessSpec.invoke(arg));
        });
        if (createProcessSpecId != null) {
            reqArgs.put("createProcessSpec", createProcessSpecId);
        }
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        getClient().invokeCapability("Aspire.Hosting/withProcessCommandFactory", reqArgs);
        return this;
    }

    /** Adds a relationship to another resource using its builder. */
    public ExternalServiceResource withRelationship(IResource resourceBuilder, String type) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceBuilder", AspireClient.serializeValue(resourceBuilder));
        reqArgs.put("type", AspireClient.serializeValue(type));
        getClient().invokeCapability("Aspire.Hosting/withBuilderRelationship", reqArgs);
        return this;
    }

    public ExternalServiceResource withRelationship(ResourceBuilderBase resourceBuilder, String type) {
        return withRelationship(new IResource(resourceBuilder.getHandle(), resourceBuilder.getClient()), type);
    }

    /** Sets the parent relationship */
    public ExternalServiceResource withParentRelationship(IResource parent) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("parent", AspireClient.serializeValue(parent));
        getClient().invokeCapability("Aspire.Hosting/withBuilderParentRelationship", reqArgs);
        return this;
    }

    public ExternalServiceResource withParentRelationship(ResourceBuilderBase parent) {
        return withParentRelationship(new IResource(parent.getHandle(), parent.getClient()));
    }

    /** Sets a child relationship */
    public ExternalServiceResource withChildRelationship(IResource child) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("child", AspireClient.serializeValue(child));
        getClient().invokeCapability("Aspire.Hosting/withBuilderChildRelationship", reqArgs);
        return this;
    }

    public ExternalServiceResource withChildRelationship(ResourceBuilderBase child) {
        return withChildRelationship(new IResource(child.getHandle(), child.getClient()));
    }

    public ExternalServiceResource withIconName(String iconName) {
        return withIconName(iconName, null);
    }

    /** Specifies the icon to use when displaying the resource in the dashboard. */
    public ExternalServiceResource withIconName(String iconName, IconVariant iconVariant) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("iconName", AspireClient.serializeValue(iconName));
        if (iconVariant != null) {
            reqArgs.put("iconVariant", AspireClient.serializeValue(iconVariant));
        }
        getClient().invokeCapability("Aspire.Hosting/withIconName", reqArgs);
        return this;
    }

    /** Exclude the resource from MCP operations using the Aspire MCP server. The resource is excluded from results that return resources, console logs and telemetry. */
    public ExternalServiceResource excludeFromMcp() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/excludeFromMcp", reqArgs);
        return this;
    }

    /** Hides the resource from default resource lists */
    public ExternalServiceResource withHidden() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withHidden", reqArgs);
        return this;
    }

    /** Hides the resource from default resource lists after successful completion */
    public ExternalServiceResource withHiddenOnCompletion(WithHiddenOnCompletionOptions options) {
        var exitCode = options == null ? null : options.getExitCode();
        var exitCodes = options == null ? null : options.getExitCodes();
        return withHiddenOnCompletionImpl(exitCode, exitCodes);
    }

    public ExternalServiceResource withHiddenOnCompletion() {
        return withHiddenOnCompletion(null);
    }

    /** Hides the resource from default resource lists after successful completion */
    private ExternalServiceResource withHiddenOnCompletionImpl(Double exitCode, double[] exitCodes) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (exitCode != null) {
            reqArgs.put("exitCode", AspireClient.serializeValue(exitCode));
        }
        if (exitCodes != null) {
            reqArgs.put("exitCodes", AspireClient.serializeValue(exitCodes));
        }
        getClient().invokeCapability("Aspire.Hosting/withHiddenOnCompletion", reqArgs);
        return this;
    }

    /** Adds a pipeline step to the resource that will be executed during deployment. */
    public ExternalServiceResource withPipelineStepFactory(String stepName, AspireAction1<PipelineStepContext> callback, WithPipelineStepFactoryOptions options) {
        var dependsOn = options == null ? null : options.getDependsOn();
        var requiredBy = options == null ? null : options.getRequiredBy();
        var tags = options == null ? null : options.getTags();
        var description = options == null ? null : options.getDescription();
        return withPipelineStepFactoryImpl(stepName, callback, dependsOn, requiredBy, tags, description);
    }

    public ExternalServiceResource withPipelineStepFactory(String stepName, AspireAction1<PipelineStepContext> callback) {
        return withPipelineStepFactory(stepName, callback, null);
    }

    /** Adds a pipeline step to the resource that will be executed during deployment. */
    private ExternalServiceResource withPipelineStepFactoryImpl(String stepName, AspireAction1<PipelineStepContext> callback, String[] dependsOn, String[] requiredBy, String[] tags, String description) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
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
        if (tags != null) {
            reqArgs.put("tags", AspireClient.serializeValue(tags));
        }
        if (description != null) {
            reqArgs.put("description", AspireClient.serializeValue(description));
        }
        getClient().invokeCapability("Aspire.Hosting/withPipelineStepFactory", reqArgs);
        return this;
    }

    /** Registers a callback to be executed during the pipeline configuration phase, allowing modification of step dependencies and relationships. */
    public ExternalServiceResource withPipelineConfiguration(AspireAction1<PipelineConfigurationContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (PipelineConfigurationContext) args[0];
            callback.invoke(obj);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withPipelineConfiguration", reqArgs);
        return this;
    }

    /** Gets the name of the resource from a builder. */
    public String getResourceName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("resource", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getResourceName", reqArgs);
        return (String) result;
    }

    /** Subscribes to the BeforeResourceStarted event. */
    public ExternalServiceResource onBeforeResourceStarted(AspireAction1<BeforeResourceStartedEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (BeforeResourceStartedEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onBeforeResourceStarted", reqArgs);
        return this;
    }

    /** Subscribes to the ResourceStopped event. */
    public ExternalServiceResource onResourceStopped(AspireAction1<ResourceStoppedEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ResourceStoppedEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onResourceStopped", reqArgs);
        return this;
    }

    /** Subscribes to the InitializeResource event. */
    public ExternalServiceResource onInitializeResource(AspireAction1<InitializeResourceEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (InitializeResourceEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onInitializeResource", reqArgs);
        return this;
    }

    /** Subscribes to the ResourceReady event. */
    public ExternalServiceResource onResourceReady(AspireAction1<ResourceReadyEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ResourceReadyEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onResourceReady", reqArgs);
        return this;
    }

    /** Creates an execution configuration builder for the specified resource. */
    public IExecutionConfigurationBuilder createExecutionConfiguration() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("resource", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/createExecutionConfiguration", reqArgs);
        return (IExecutionConfigurationBuilder) result;
    }

}
