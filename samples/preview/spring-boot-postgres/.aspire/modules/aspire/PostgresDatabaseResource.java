// PostgresDatabaseResource.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;

/** Wrapper for Aspire.Hosting.PostgreSQL/Aspire.Hosting.ApplicationModel.PostgresDatabaseResource. */
@SuppressWarnings({"all", "unchecked", "serial"})
public class PostgresDatabaseResource extends ResourceBuilderBase {
    PostgresDatabaseResource(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Configures the resource to use the specified container registry for container image operations. */
    public PostgresDatabaseResource withContainerRegistry(IResource registry) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("registry", AspireClient.serializeValue(registry));
        getClient().invokeCapability("Aspire.Hosting/withContainerRegistry", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withContainerRegistry(ResourceBuilderBase registry) {
        return withContainerRegistry(new IResource(registry.getHandle(), registry.getClient()));
    }

    /** Configures custom base images for generated Dockerfiles. */
    public PostgresDatabaseResource withDockerfileBaseImage(WithDockerfileBaseImageOptions optionsBag) {
        var buildImage = optionsBag == null ? null : optionsBag.getBuildImage();
        var runtimeImage = optionsBag == null ? null : optionsBag.getRuntimeImage();
        return withDockerfileBaseImageImpl(buildImage, runtimeImage);
    }

    public PostgresDatabaseResource withDockerfileBaseImage() {
        return withDockerfileBaseImage(null);
    }

    /** Configures custom base images for generated Dockerfiles. */
    private PostgresDatabaseResource withDockerfileBaseImageImpl(String buildImage, String runtimeImage) {
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

    public PostgresDatabaseResource withRequiredCommand(String command) {
        return withRequiredCommand(command, null);
    }

    /** Declares that a resource requires a specific command/executable to be available on the local machine PATH before it can start. */
    public PostgresDatabaseResource withRequiredCommand(String command, String helpLink) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("command", AspireClient.serializeValue(command));
        if (helpLink != null) {
            reqArgs.put("helpLink", AspireClient.serializeValue(helpLink));
        }
        getClient().invokeCapability("Aspire.Hosting/withRequiredCommand", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withRequiredCommandValidation(String command, AspireFunc1<RequiredCommandValidationContext, RequiredCommandValidationResult> validationCallback) {
        return withRequiredCommandValidation(command, validationCallback, null);
    }

    /** Declares that a resource requires a specific command/executable to be available on the local machine PATH before it can start, with custom validation logic. */
    public PostgresDatabaseResource withRequiredCommandValidation(String command, AspireFunc1<RequiredCommandValidationContext, RequiredCommandValidationResult> validationCallback, String helpLink) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("command", AspireClient.serializeValue(command));
        var validationCallbackId = getClient().registerCallback(args -> {
            var arg = (RequiredCommandValidationContext) args[0];
            return AspireClient.awaitValue(validationCallback.invoke(arg));
        });
        if (validationCallbackId != null) {
            reqArgs.put("validationCallback", validationCallbackId);
        }
        if (helpLink != null) {
            reqArgs.put("helpLink", AspireClient.serializeValue(helpLink));
        }
        getClient().invokeCapability("Aspire.Hosting/withRequiredCommandValidation", reqArgs);
        return this;
    }

    /** Configures a resource to use a session lifetime. */
    public PostgresDatabaseResource withSessionLifetime() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withSessionLifetime", reqArgs);
        return this;
    }

    /** Configures a resource to use a persistent lifetime. */
    public PostgresDatabaseResource withPersistentLifetime() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withPersistentLifetime", reqArgs);
        return this;
    }

    /** Configures a resource to match the lifetime of another resource. */
    public PostgresDatabaseResource withLifetimeOf(IResource sourceBuilder) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("sourceBuilder", AspireClient.serializeValue(sourceBuilder));
        getClient().invokeCapability("Aspire.Hosting/withLifetimeOf", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withLifetimeOf(ResourceBuilderBase sourceBuilder) {
        return withLifetimeOf(new IResource(sourceBuilder.getHandle(), sourceBuilder.getClient()));
    }

    /** Configures a resource to use a persistent lifetime that ends when a parent process exits. */
    public PostgresDatabaseResource withParentProcessLifetime(double parentProcessId) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("parentProcessId", AspireClient.serializeValue(parentProcessId));
        getClient().invokeCapability("Aspire.Hosting/withParentProcessLifetime", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withConnectionProperty(String name, String value) {
        return withConnectionProperty(name, AspireUnion.of(value));
    }

    public PostgresDatabaseResource withConnectionProperty(String name, ReferenceExpression value) {
        return withConnectionProperty(name, AspireUnion.of(value));
    }

    /** Adds a connection property annotation to the resource being built. */
    public PostgresDatabaseResource withConnectionProperty(String name, AspireUnion value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/withConnectionProperty", reqArgs);
        return this;
    }

    /** Retrieves the value of a specified connection property from the resource's connection properties. */
    public ReferenceExpression getConnectionProperty(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("resource", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        var result = getClient().invokeCapability("Aspire.Hosting/getConnectionProperty", reqArgs);
        return (ReferenceExpression) result;
    }

    /** Registers a callback to customize the URLs displayed for the resource. */
    public PostgresDatabaseResource withUrls(AspireAction1<ResourceUrlsCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (ResourceUrlsCallbackContext) args[0];
            callback.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withUrls", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withUrl(String url, String displayText) {
        return withUrl(AspireUnion.of(url), displayText);
    }

    public PostgresDatabaseResource withUrl(ReferenceExpression url, String displayText) {
        return withUrl(AspireUnion.of(url), displayText);
    }

    public PostgresDatabaseResource withUrl(AspireUnion url) {
        return withUrl(url, null);
    }

    /** Adds or modifies displayed URLs */
    public PostgresDatabaseResource withUrl(AspireUnion url, String displayText) {
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
    public PostgresDatabaseResource withUrlForEndpoint(String endpointName, AspireAction1<ResourceUrlAnnotation> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("endpointName", AspireClient.serializeValue(endpointName));
        var callbackId = getClient().registerCallback(args -> {
            var obj = ResourceUrlAnnotation.fromMap((Map<String, Object>) args[0]);
            callback.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withUrlForEndpoint", reqArgs);
        return this;
    }

    /** Excludes a resource from being published to the manifest. */
    public PostgresDatabaseResource excludeFromManifest() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/excludeFromManifest", reqArgs);
        return this;
    }

    /** Prevents resource from starting automatically */
    public PostgresDatabaseResource withExplicitStart() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withExplicitStart", reqArgs);
        return this;
    }

    /** Adds a health check by key */
    public PostgresDatabaseResource withHealthCheck(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        getClient().invokeCapability("Aspire.Hosting/withHealthCheck", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withCommand(String name, String displayName, AspireFunc1<ExecuteCommandContext, ExecuteCommandResult> executeCommand) {
        return withCommand(name, displayName, executeCommand, null);
    }

    /** Adds a resource command */
    public PostgresDatabaseResource withCommand(String name, String displayName, AspireFunc1<ExecuteCommandContext, ExecuteCommandResult> executeCommand, CommandOptions commandOptions) {
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
    public PostgresDatabaseResource withProcessCommand(String commandName, String displayName, ProcessCommandExportOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("commandName", AspireClient.serializeValue(commandName));
        reqArgs.put("displayName", AspireClient.serializeValue(displayName));
        reqArgs.put("options", AspireClient.serializeValue(options));
        getClient().invokeCapability("Aspire.Hosting/withProcessCommand", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withProcessCommandFactory(String commandName, String displayName, AspireFunc1<ExecuteCommandContext, ProcessCommandSpecExportData> createProcessSpec) {
        return withProcessCommandFactory(commandName, displayName, createProcessSpec, null);
    }

    /** Adds a command to the resource that starts a local process created by a callback when invoked. */
    public PostgresDatabaseResource withProcessCommandFactory(String commandName, String displayName, AspireFunc1<ExecuteCommandContext, ProcessCommandSpecExportData> createProcessSpec, ProcessCommandResultExportOptions options) {
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

    /** Subscribes to the `BeforeStartEvent` and invokes the specified callback when an HTTPS certificate is determined to be available for the resource. This is used to conditionally update endpoint URI schemes or perform other HTTPS-related configuration at startup. */
    public PostgresDatabaseResource subscribeHttpsEndpointsUpdate(AspireAction1<HttpsEndpointUpdateCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (HttpsEndpointUpdateCallbackContext) args[0];
            callback.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/subscribeHttpsEndpointsUpdate", reqArgs);
        return this;
    }

    /** Adds a relationship to another resource using its builder. */
    public PostgresDatabaseResource withRelationship(IResource resourceBuilder, String type) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceBuilder", AspireClient.serializeValue(resourceBuilder));
        reqArgs.put("type", AspireClient.serializeValue(type));
        getClient().invokeCapability("Aspire.Hosting/withBuilderRelationship", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withRelationship(ResourceBuilderBase resourceBuilder, String type) {
        return withRelationship(new IResource(resourceBuilder.getHandle(), resourceBuilder.getClient()), type);
    }

    /** Sets the parent relationship */
    public PostgresDatabaseResource withParentRelationship(IResource parent) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("parent", AspireClient.serializeValue(parent));
        getClient().invokeCapability("Aspire.Hosting/withBuilderParentRelationship", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withParentRelationship(ResourceBuilderBase parent) {
        return withParentRelationship(new IResource(parent.getHandle(), parent.getClient()));
    }

    /** Sets a child relationship */
    public PostgresDatabaseResource withChildRelationship(IResource child) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("child", AspireClient.serializeValue(child));
        getClient().invokeCapability("Aspire.Hosting/withBuilderChildRelationship", reqArgs);
        return this;
    }

    public PostgresDatabaseResource withChildRelationship(ResourceBuilderBase child) {
        return withChildRelationship(new IResource(child.getHandle(), child.getClient()));
    }

    public PostgresDatabaseResource withIconName(String iconName) {
        return withIconName(iconName, null);
    }

    /** Specifies the icon to use when displaying the resource in the dashboard. */
    public PostgresDatabaseResource withIconName(String iconName, IconVariant iconVariant) {
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
    public PostgresDatabaseResource excludeFromMcp() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/excludeFromMcp", reqArgs);
        return this;
    }

    /** Hides the resource from default resource lists */
    public PostgresDatabaseResource withHidden() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withHidden", reqArgs);
        return this;
    }

    /** Hides the resource from default resource lists after successful completion */
    public PostgresDatabaseResource withHiddenOnCompletion(WithHiddenOnCompletionOptions optionsBag) {
        var exitCode = optionsBag == null ? null : optionsBag.getExitCode();
        var exitCodes = optionsBag == null ? null : optionsBag.getExitCodes();
        return withHiddenOnCompletionImpl(exitCode, exitCodes);
    }

    public PostgresDatabaseResource withHiddenOnCompletion() {
        return withHiddenOnCompletion(null);
    }

    /** Hides the resource from default resource lists after successful completion */
    private PostgresDatabaseResource withHiddenOnCompletionImpl(Number exitCode, double[] exitCodes) {
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

    /** Adds an interactive terminal session to a resource using the default terminal options. */
    public PostgresDatabaseResource withTerminal() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withTerminal", reqArgs);
        return this;
    }

    /** Adds a pipeline step to the resource that will be executed during deployment. */
    public PostgresDatabaseResource withPipelineStepFactory(String stepName, AspireAction1<PipelineStepContext> callback, WithPipelineStepFactoryOptions optionsBag) {
        var dependsOn = optionsBag == null ? null : optionsBag.getDependsOn();
        var requiredBy = optionsBag == null ? null : optionsBag.getRequiredBy();
        var tags = optionsBag == null ? null : optionsBag.getTags();
        var description = optionsBag == null ? null : optionsBag.getDescription();
        return withPipelineStepFactoryImpl(stepName, callback, dependsOn, requiredBy, tags, description);
    }

    public PostgresDatabaseResource withPipelineStepFactory(String stepName, AspireAction1<PipelineStepContext> callback) {
        return withPipelineStepFactory(stepName, callback, null);
    }

    /** Adds a pipeline step to the resource that will be executed during deployment. */
    private PostgresDatabaseResource withPipelineStepFactoryImpl(String stepName, AspireAction1<PipelineStepContext> callback, String[] dependsOn, String[] requiredBy, String[] tags, String description) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("stepName", AspireClient.serializeValue(stepName));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (PipelineStepContext) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
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
    public PostgresDatabaseResource withPipelineConfiguration(AspireAction1<PipelineConfigurationContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (PipelineConfigurationContext) args[0];
            callback.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
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
    public PostgresDatabaseResource onBeforeResourceStarted(AspireAction1<BeforeResourceStartedEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (BeforeResourceStartedEvent) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onBeforeResourceStarted", reqArgs);
        return this;
    }

    /** Subscribes to the ResourceStopped event. */
    public PostgresDatabaseResource onResourceStopped(AspireAction1<ResourceStoppedEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ResourceStoppedEvent) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onResourceStopped", reqArgs);
        return this;
    }

    /** Subscribes to the ConnectionStringAvailable event. */
    public PostgresDatabaseResource onConnectionStringAvailable(AspireAction1<ConnectionStringAvailableEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ConnectionStringAvailableEvent) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onConnectionStringAvailable", reqArgs);
        return this;
    }

    /** Subscribes to the InitializeResource event. */
    public PostgresDatabaseResource onInitializeResource(AspireAction1<InitializeResourceEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (InitializeResourceEvent) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onInitializeResource", reqArgs);
        return this;
    }

    /** Subscribes to the ResourceReady event. */
    public PostgresDatabaseResource onResourceReady(AspireAction1<ResourceReadyEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ResourceReadyEvent) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
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

    /** Configures container build options for a compute resource using an async callback. */
    public PostgresDatabaseResource withContainerBuildOptions(AspireAction1<ContainerBuildOptionsCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ContainerBuildOptionsCallbackContext) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withContainerBuildOptions", reqArgs);
        return this;
    }

    /** Adds a Postgres MCP server container and configures it to connect to the database represented by `builder`. */
    public PostgresDatabaseResource withPostgresMcp(WithPostgresMcpOptions optionsBag) {
        var configureContainer = optionsBag == null ? null : optionsBag.getConfigureContainer();
        var containerName = optionsBag == null ? null : optionsBag.getContainerName();
        return withPostgresMcpImpl(configureContainer, containerName);
    }

    public PostgresDatabaseResource withPostgresMcp() {
        return withPostgresMcp(null);
    }

    /** Adds a Postgres MCP server container and configures it to connect to the database represented by `builder`. */
    private PostgresDatabaseResource withPostgresMcpImpl(AspireAction1<PostgresMcpContainerResource> configureContainer, String containerName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var configureContainerId = configureContainer == null ? null : getClient().registerCallback(args -> {
            var obj = (PostgresMcpContainerResource) args[0];
            configureContainer.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
        });
        if (configureContainerId != null) {
            reqArgs.put("configureContainer", configureContainerId);
        }
        if (containerName != null) {
            reqArgs.put("containerName", AspireClient.serializeValue(containerName));
        }
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withPostgresMcp", reqArgs);
        return this;
    }

    /** Defines the SQL script used to create the database. */
    public PostgresDatabaseResource withCreationScript(String script) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("script", AspireClient.serializeValue(script));
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withCreationScript", reqArgs);
        return this;
    }

    /** Gets the parent PostgresSQL container resource. */
    public PostgresServerResource parent() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresDatabaseResource.parent", reqArgs);
        return (PostgresServerResource) result;
    }

    /** Gets the connection string expression for the Postgres database. */
    public ReferenceExpression connectionStringExpression() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresDatabaseResource.connectionStringExpression", reqArgs);
        return (ReferenceExpression) result;
    }

    /** Gets the database name. */
    public String databaseName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresDatabaseResource.databaseName", reqArgs);
        return (String) result;
    }

    /** Gets the connection URI expression for the PostgreSQL database. */
    public ReferenceExpression uriExpression() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresDatabaseResource.uriExpression", reqArgs);
        return (ReferenceExpression) result;
    }

    /** Gets the JDBC connection string for the PostgreSQL database. */
    public ReferenceExpression jdbcConnectionString() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresDatabaseResource.jdbcConnectionString", reqArgs);
        return (ReferenceExpression) result;
    }

    /** Gets the name of the resource. */
    public String name() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresDatabaseResource.name", reqArgs);
        return (String) result;
    }

}
