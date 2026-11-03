// PostgresServerResource.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Wrapper for Aspire.Hosting.PostgreSQL/Aspire.Hosting.ApplicationModel.PostgresServerResource. */
@SuppressWarnings({"all", "unchecked", "serial"})
public class PostgresServerResource extends ContainerResource {
    PostgresServerResource(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Configures the resource to use the specified container registry for container image operations. */
    public PostgresServerResource withContainerRegistry(IResource registry) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("registry", AspireClient.serializeValue(registry));
        getClient().invokeCapability("Aspire.Hosting/withContainerRegistry", reqArgs);
        return this;
    }

    public PostgresServerResource withContainerRegistry(ResourceBuilderBase registry) {
        return withContainerRegistry(new IResource(registry.getHandle(), registry.getClient()));
    }

    public PostgresServerResource withBindMount(String source, String target) {
        return withBindMount(source, target, null);
    }

    /** Adds a bind mount to a container resource. */
    public PostgresServerResource withBindMount(String source, String target, Boolean isReadOnly) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("source", AspireClient.serializeValue(source));
        reqArgs.put("target", AspireClient.serializeValue(target));
        if (isReadOnly != null) {
            reqArgs.put("isReadOnly", AspireClient.serializeValue(isReadOnly));
        }
        getClient().invokeCapability("Aspire.Hosting/withBindMount", reqArgs);
        return this;
    }

    /** Sets the Entrypoint for the container. */
    public PostgresServerResource withEntrypoint(String entrypoint) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("entrypoint", AspireClient.serializeValue(entrypoint));
        getClient().invokeCapability("Aspire.Hosting/withEntrypoint", reqArgs);
        return this;
    }

    /** Allows overriding the image tag on a container. */
    public PostgresServerResource withImageTag(String tag) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("tag", AspireClient.serializeValue(tag));
        getClient().invokeCapability("Aspire.Hosting/withImageTag", reqArgs);
        return this;
    }

    /** Allows overriding the image registry on a container. */
    public PostgresServerResource withImageRegistry(String registry) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("registry", AspireClient.serializeValue(registry));
        getClient().invokeCapability("Aspire.Hosting/withImageRegistry", reqArgs);
        return this;
    }

    public PostgresServerResource withImage(String image) {
        return withImage(image, null);
    }

    /** Allows overriding the image on a container. */
    public PostgresServerResource withImage(String image, String tag) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("image", AspireClient.serializeValue(image));
        if (tag != null) {
            reqArgs.put("tag", AspireClient.serializeValue(tag));
        }
        getClient().invokeCapability("Aspire.Hosting/withImage", reqArgs);
        return this;
    }

    /** Allows setting the image to a specific sha256 on a container. */
    public PostgresServerResource withImageSHA256(String sha256) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("sha256", AspireClient.serializeValue(sha256));
        getClient().invokeCapability("Aspire.Hosting/withImageSHA256", reqArgs);
        return this;
    }

    /** Adds a callback to be executed with a list of arguments to add to the container runtime run command when a container resource is started. */
    public PostgresServerResource withContainerRuntimeArgs(String[] args) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("args", AspireClient.serializeValue(args));
        getClient().invokeCapability("Aspire.Hosting/withContainerRuntimeArgs", reqArgs);
        return this;
    }

    /** Sets the lifetime behavior of the container resource. */
    public PostgresServerResource withLifetime(ContainerLifetime lifetime) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("lifetime", AspireClient.serializeValue(lifetime));
        getClient().invokeCapability("Aspire.Hosting/withLifetime", reqArgs);
        return this;
    }

    /** Sets the pull policy for the container resource. */
    public PostgresServerResource withImagePullPolicy(ImagePullPolicy pullPolicy) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("pullPolicy", AspireClient.serializeValue(pullPolicy));
        getClient().invokeCapability("Aspire.Hosting/withImagePullPolicy", reqArgs);
        return this;
    }

    /** Changes the resource to be published as a container in the manifest. */
    public PostgresServerResource publishAsContainer() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/publishAsContainer", reqArgs);
        return this;
    }

    /** Causes Aspire to build the specified container image from a Dockerfile. */
    public PostgresServerResource withDockerfile(String contextPath, WithDockerfileOptions optionsBag) {
        var dockerfilePath = optionsBag == null ? null : optionsBag.getDockerfilePath();
        var stage = optionsBag == null ? null : optionsBag.getStage();
        return withDockerfileImpl(contextPath, dockerfilePath, stage);
    }

    public PostgresServerResource withDockerfile(String contextPath) {
        return withDockerfile(contextPath, null);
    }

    /** Causes Aspire to build the specified container image from a Dockerfile. */
    private PostgresServerResource withDockerfileImpl(String contextPath, String dockerfilePath, String stage) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("contextPath", AspireClient.serializeValue(contextPath));
        if (dockerfilePath != null) {
            reqArgs.put("dockerfilePath", AspireClient.serializeValue(dockerfilePath));
        }
        if (stage != null) {
            reqArgs.put("stage", AspireClient.serializeValue(stage));
        }
        getClient().invokeCapability("Aspire.Hosting/withDockerfile", reqArgs);
        return this;
    }

    public PostgresServerResource withDockerfileFactory(String contextPath, AspireFunc1<DockerfileFactoryContext, String> dockerfileFactory) {
        return withDockerfileFactory(contextPath, dockerfileFactory, null);
    }

    /** Builds the specified container image from a Dockerfile generated by an asynchronous factory function. */
    public PostgresServerResource withDockerfileFactory(String contextPath, AspireFunc1<DockerfileFactoryContext, String> dockerfileFactory, String stage) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("contextPath", AspireClient.serializeValue(contextPath));
        var dockerfileFactoryId = getClient().registerCallback(args -> {
            var arg = (DockerfileFactoryContext) args[0];
            return AspireClient.awaitValue(dockerfileFactory.invoke(arg));
        });
        if (dockerfileFactoryId != null) {
            reqArgs.put("dockerfileFactory", dockerfileFactoryId);
        }
        if (stage != null) {
            reqArgs.put("stage", AspireClient.serializeValue(stage));
        }
        getClient().invokeCapability("Aspire.Hosting/withDockerfileFactory", reqArgs);
        return this;
    }

    /** Overrides the default container name for this resource. By default Aspire generates a unique container name based on the resource name and a random postfix (or a postfix based on a hash of the AppHost project path for persistent container resources). This method allows you to override that behavior with a custom name, but could lead to naming conflicts if the specified name is not unique. */
    public PostgresServerResource withContainerName(String name) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        getClient().invokeCapability("Aspire.Hosting/withContainerName", reqArgs);
        return this;
    }

    public PostgresServerResource withBuildArg(String name, String value) {
        return withBuildArg(name, AspireUnion.of(value));
    }

    public PostgresServerResource withBuildArg(String name, ParameterResource value) {
        return withBuildArg(name, AspireUnion.of(value));
    }

    /** Adds a build argument when the container is built from a Dockerfile. */
    public PostgresServerResource withBuildArg(String name, AspireUnion value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/withBuildArg", reqArgs);
        return this;
    }

    /** Adds a secret build argument when the container is built from a Dockerfile. */
    public PostgresServerResource withBuildSecret(String name, ParameterResource value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/withParameterBuildSecret", reqArgs);
        return this;
    }

    /** Adds container certificate path overrides used for certificate trust at run time. */
    public PostgresServerResource withContainerCertificatePaths(WithContainerCertificatePathsOptions optionsBag) {
        var customCertificatesDestination = optionsBag == null ? null : optionsBag.getCustomCertificatesDestination();
        var defaultCertificateBundlePaths = optionsBag == null ? null : optionsBag.getDefaultCertificateBundlePaths();
        var defaultCertificateDirectoryPaths = optionsBag == null ? null : optionsBag.getDefaultCertificateDirectoryPaths();
        return withContainerCertificatePathsImpl(customCertificatesDestination, defaultCertificateBundlePaths, defaultCertificateDirectoryPaths);
    }

    public PostgresServerResource withContainerCertificatePaths() {
        return withContainerCertificatePaths(null);
    }

    /** Adds container certificate path overrides used for certificate trust at run time. */
    private PostgresServerResource withContainerCertificatePathsImpl(String customCertificatesDestination, String[] defaultCertificateBundlePaths, String[] defaultCertificateDirectoryPaths) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (customCertificatesDestination != null) {
            reqArgs.put("customCertificatesDestination", AspireClient.serializeValue(customCertificatesDestination));
        }
        if (defaultCertificateBundlePaths != null) {
            reqArgs.put("defaultCertificateBundlePaths", AspireClient.serializeValue(defaultCertificateBundlePaths));
        }
        if (defaultCertificateDirectoryPaths != null) {
            reqArgs.put("defaultCertificateDirectoryPaths", AspireClient.serializeValue(defaultCertificateDirectoryPaths));
        }
        getClient().invokeCapability("Aspire.Hosting/withContainerCertificatePaths", reqArgs);
        return this;
    }

    public PostgresServerResource withContainerFiles(String destinationPath, String sourcePath) {
        return withContainerFiles(destinationPath, sourcePath, null);
    }

    /** Creates or updates files and folders in a container by copying them from a source path on the host. */
    public PostgresServerResource withContainerFiles(String destinationPath, String sourcePath, ContainerFilesOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("destinationPath", AspireClient.serializeValue(destinationPath));
        reqArgs.put("sourcePath", AspireClient.serializeValue(sourcePath));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        getClient().invokeCapability("Aspire.Hosting/withContainerFiles", reqArgs);
        return this;
    }

    public PostgresServerResource withContainerFilesCallback(String destinationPath, AspireFunc2<ContainerFileSystemCallbackContext, CancellationToken, ContainerFileSystemItem[]> callback) {
        return withContainerFilesCallback(destinationPath, callback, null);
    }

    /** Creates or updates files and/or folders at the destination path in the container using entries produced by a callback. */
    public PostgresServerResource withContainerFilesCallback(String destinationPath, AspireFunc2<ContainerFileSystemCallbackContext, CancellationToken, ContainerFileSystemItem[]> callback, ContainerFilesOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("destinationPath", AspireClient.serializeValue(destinationPath));
        var callbackId = getClient().registerCallback(args -> {
            var arg1 = (ContainerFileSystemCallbackContext) args[0];
            var arg2 = CancellationToken.fromValue(args[1]);
            return AspireClient.awaitValue(callback.invoke(arg1, arg2));
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        getClient().invokeCapability("Aspire.Hosting/withContainerFilesCallback", reqArgs);
        return this;
    }

    public PostgresServerResource withDockerfileBuilder(String contextPath, AspireAction1<DockerfileBuilderCallbackContext> callback) {
        return withDockerfileBuilder(contextPath, callback, null);
    }

    /** Configures the resource to use a programmatically generated Dockerfile */
    public PostgresServerResource withDockerfileBuilder(String contextPath, AspireAction1<DockerfileBuilderCallbackContext> callback, String stage) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("contextPath", AspireClient.serializeValue(contextPath));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (DockerfileBuilderCallbackContext) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        if (stage != null) {
            reqArgs.put("stage", AspireClient.serializeValue(stage));
        }
        getClient().invokeCapability("Aspire.Hosting/withDockerfileBuilder", reqArgs);
        return this;
    }

    /** Configures custom base images for generated Dockerfiles. */
    public PostgresServerResource withDockerfileBaseImage(WithDockerfileBaseImageOptions optionsBag) {
        var buildImage = optionsBag == null ? null : optionsBag.getBuildImage();
        var runtimeImage = optionsBag == null ? null : optionsBag.getRuntimeImage();
        return withDockerfileBaseImageImpl(buildImage, runtimeImage);
    }

    public PostgresServerResource withDockerfileBaseImage() {
        return withDockerfileBaseImage(null);
    }

    /** Configures custom base images for generated Dockerfiles. */
    private PostgresServerResource withDockerfileBaseImageImpl(String buildImage, String runtimeImage) {
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

    /** Adds a network alias to container resource. */
    public PostgresServerResource withContainerNetworkAlias(String alias) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("alias", AspireClient.serializeValue(alias));
        getClient().invokeCapability("Aspire.Hosting/withContainerNetworkAlias", reqArgs);
        return this;
    }

    /** Marks the resource as hosting a Model Context Protocol (MCP) server on the specified endpoint. */
    public PostgresServerResource withMcpServer(WithMcpServerOptions optionsBag) {
        var path = optionsBag == null ? null : optionsBag.getPath();
        var endpointName = optionsBag == null ? null : optionsBag.getEndpointName();
        return withMcpServerImpl(path, endpointName);
    }

    public PostgresServerResource withMcpServer() {
        return withMcpServer(null);
    }

    /** Marks the resource as hosting a Model Context Protocol (MCP) server on the specified endpoint. */
    private PostgresServerResource withMcpServerImpl(String path, String endpointName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (path != null) {
            reqArgs.put("path", AspireClient.serializeValue(path));
        }
        if (endpointName != null) {
            reqArgs.put("endpointName", AspireClient.serializeValue(endpointName));
        }
        getClient().invokeCapability("Aspire.Hosting/withMcpServer", reqArgs);
        return this;
    }

    public PostgresServerResource withOtlpExporter() {
        return withOtlpExporter(null);
    }

    /** Configures OTLP telemetry export */
    public PostgresServerResource withOtlpExporter(OtlpProtocol protocol) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (protocol != null) {
            reqArgs.put("protocol", AspireClient.serializeValue(protocol));
        }
        getClient().invokeCapability("Aspire.Hosting/withOtlpExporter", reqArgs);
        return this;
    }

    /** Changes the resource to be published as a connection string reference in the manifest. */
    public PostgresServerResource publishAsConnectionString() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/publishAsConnectionString", reqArgs);
        return this;
    }

    public PostgresServerResource withRequiredCommand(String command) {
        return withRequiredCommand(command, null);
    }

    /** Declares that a resource requires a specific command/executable to be available on the local machine PATH before it can start. */
    public PostgresServerResource withRequiredCommand(String command, String helpLink) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("command", AspireClient.serializeValue(command));
        if (helpLink != null) {
            reqArgs.put("helpLink", AspireClient.serializeValue(helpLink));
        }
        getClient().invokeCapability("Aspire.Hosting/withRequiredCommand", reqArgs);
        return this;
    }

    public PostgresServerResource withRequiredCommandValidation(String command, AspireFunc1<RequiredCommandValidationContext, RequiredCommandValidationResult> validationCallback) {
        return withRequiredCommandValidation(command, validationCallback, null);
    }

    /** Declares that a resource requires a specific command/executable to be available on the local machine PATH before it can start, with custom validation logic. */
    public PostgresServerResource withRequiredCommandValidation(String command, AspireFunc1<RequiredCommandValidationContext, RequiredCommandValidationResult> validationCallback, String helpLink) {
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
    public PostgresServerResource withSessionLifetime() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withSessionLifetime", reqArgs);
        return this;
    }

    /** Configures a resource to use a persistent lifetime. */
    public PostgresServerResource withPersistentLifetime() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withPersistentLifetime", reqArgs);
        return this;
    }

    /** Configures a resource to match the lifetime of another resource. */
    public PostgresServerResource withLifetimeOf(IResource sourceBuilder) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("sourceBuilder", AspireClient.serializeValue(sourceBuilder));
        getClient().invokeCapability("Aspire.Hosting/withLifetimeOf", reqArgs);
        return this;
    }

    public PostgresServerResource withLifetimeOf(ResourceBuilderBase sourceBuilder) {
        return withLifetimeOf(new IResource(sourceBuilder.getHandle(), sourceBuilder.getClient()));
    }

    /** Configures a resource to use a persistent lifetime that ends when a parent process exits. */
    public PostgresServerResource withParentProcessLifetime(double parentProcessId) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("parentProcessId", AspireClient.serializeValue(parentProcessId));
        getClient().invokeCapability("Aspire.Hosting/withParentProcessLifetime", reqArgs);
        return this;
    }

    public PostgresServerResource withEnvironment(String name, String value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public PostgresServerResource withEnvironment(String name, ReferenceExpression value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public PostgresServerResource withEnvironment(String name, EndpointReference value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public PostgresServerResource withEnvironment(String name, ParameterResource value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public PostgresServerResource withEnvironment(String name, ExternalServiceResource value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public PostgresServerResource withEnvironment(String name, IResourceWithConnectionString value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public PostgresServerResource withEnvironment(String name, ResourceBuilderBase value) {
        return withEnvironment(name, new IResourceWithConnectionString(value.getHandle(), value.getClient()));
    }

    public PostgresServerResource withEnvironment(String name, IExpressionValue value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public PostgresServerResource withEnvironment(String name, HandleWrapperBase value) {
        return withEnvironment(name, new IExpressionValue(value.getHandle(), value.getClient()));
    }

    /** Sets an environment variable */
    public PostgresServerResource withEnvironment(String name, AspireUnion value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/withEnvironment", reqArgs);
        return this;
    }

    /** Allows for the population of environment variables on a resource. */
    public PostgresServerResource withEnvironmentCallback(AspireAction1<EnvironmentCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (EnvironmentCallbackContext) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withEnvironmentCallback", reqArgs);
        return this;
    }

    public PostgresServerResource withConnectionProperty(String name, String value) {
        return withConnectionProperty(name, AspireUnion.of(value));
    }

    public PostgresServerResource withConnectionProperty(String name, ReferenceExpression value) {
        return withConnectionProperty(name, AspireUnion.of(value));
    }

    /** Adds a connection property annotation to the resource being built. */
    public PostgresServerResource withConnectionProperty(String name, AspireUnion value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/withConnectionProperty", reqArgs);
        return this;
    }

    /** Adds arguments to be passed to a resource that supports arguments when it is launched. */
    public PostgresServerResource withArgs(String[] args) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("args", AspireClient.serializeValue(args));
        getClient().invokeCapability("Aspire.Hosting/withArgs", reqArgs);
        return this;
    }

    /** Adds a callback to be executed with a list of command-line arguments when a resource is started. */
    public PostgresServerResource withArgsCallback(AspireAction1<CommandLineArgsCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (CommandLineArgsCallbackContext) args[0];
            callback.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withArgsCallback", reqArgs);
        return this;
    }

    /** Configures how information is injected into environment variables when the resource references other resources. */
    public PostgresServerResource withReferenceEnvironment(ReferenceEnvironmentInjectionOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("options", AspireClient.serializeValue(options));
        getClient().invokeCapability("Aspire.Hosting/withReferenceEnvironment", reqArgs);
        return this;
    }

    public PostgresServerResource withReference(IResource source, WithReferenceOptions options) {
        return withReference(AspireUnion.of(source), options);
    }

    public PostgresServerResource withReference(ResourceBuilderBase source, WithReferenceOptions options) {
        return withReference(new IResource(source.getHandle(), source.getClient()), options);
    }

    public PostgresServerResource withReference(IResource source) {
        return withReference(AspireUnion.of(source));
    }

    public PostgresServerResource withReference(ResourceBuilderBase source) {
        return withReference(new IResource(source.getHandle(), source.getClient()));
    }

    public PostgresServerResource withReference(EndpointReference source, WithReferenceOptions options) {
        return withReference(AspireUnion.of(source), options);
    }

    public PostgresServerResource withReference(EndpointReference source) {
        return withReference(AspireUnion.of(source));
    }

    public PostgresServerResource withReference(String source, WithReferenceOptions options) {
        return withReference(AspireUnion.of(source), options);
    }

    public PostgresServerResource withReference(String source) {
        return withReference(AspireUnion.of(source));
    }

    /** Adds a reference to another resource */
    public PostgresServerResource withReference(AspireUnion source, WithReferenceOptions optionsBag) {
        var connectionName = optionsBag == null ? null : optionsBag.getConnectionName();
        var optional = optionsBag == null ? null : optionsBag.getOptional();
        var name = optionsBag == null ? null : optionsBag.getName();
        return withReferenceImpl(source, connectionName, optional, name);
    }

    public PostgresServerResource withReference(AspireUnion source) {
        return withReference(source, null);
    }

    /** Adds a reference to another resource */
    private PostgresServerResource withReferenceImpl(AspireUnion source, String connectionName, Boolean optional, String name) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("source", AspireClient.serializeValue(source));
        if (connectionName != null) {
            reqArgs.put("connectionName", AspireClient.serializeValue(connectionName));
        }
        if (optional != null) {
            reqArgs.put("optional", AspireClient.serializeValue(optional));
        }
        if (name != null) {
            reqArgs.put("name", AspireClient.serializeValue(name));
        }
        getClient().invokeCapability("Aspire.Hosting/withReference", reqArgs);
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

    public PostgresServerResource withEndpointCallback(String endpointName, AspireAction1<EndpointUpdateContext> callback) {
        return withEndpointCallback(endpointName, callback, null);
    }

    /** Updates a named endpoint via callback */
    public PostgresServerResource withEndpointCallback(String endpointName, AspireAction1<EndpointUpdateContext> callback, Boolean createIfNotExists) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("endpointName", AspireClient.serializeValue(endpointName));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (EndpointUpdateContext) args[0];
            callback.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        if (createIfNotExists != null) {
            reqArgs.put("createIfNotExists", AspireClient.serializeValue(createIfNotExists));
        }
        getClient().invokeCapability("Aspire.Hosting/withEndpointCallback", reqArgs);
        return this;
    }

    /** Updates an HTTP endpoint via callback */
    public PostgresServerResource withHttpEndpointCallback(AspireAction1<EndpointUpdateContext> callback, WithHttpEndpointCallbackOptions optionsBag) {
        var name = optionsBag == null ? null : optionsBag.getName();
        var createIfNotExists = optionsBag == null ? null : optionsBag.getCreateIfNotExists();
        return withHttpEndpointCallbackImpl(callback, name, createIfNotExists);
    }

    public PostgresServerResource withHttpEndpointCallback(AspireAction1<EndpointUpdateContext> callback) {
        return withHttpEndpointCallback(callback, null);
    }

    /** Updates an HTTP endpoint via callback */
    private PostgresServerResource withHttpEndpointCallbackImpl(AspireAction1<EndpointUpdateContext> callback, String name, Boolean createIfNotExists) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (EndpointUpdateContext) args[0];
            callback.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        if (name != null) {
            reqArgs.put("name", AspireClient.serializeValue(name));
        }
        if (createIfNotExists != null) {
            reqArgs.put("createIfNotExists", AspireClient.serializeValue(createIfNotExists));
        }
        getClient().invokeCapability("Aspire.Hosting/withHttpEndpointCallback", reqArgs);
        return this;
    }

    /** Updates an HTTPS endpoint via callback */
    public PostgresServerResource withHttpsEndpointCallback(AspireAction1<EndpointUpdateContext> callback, WithHttpsEndpointCallbackOptions optionsBag) {
        var name = optionsBag == null ? null : optionsBag.getName();
        var createIfNotExists = optionsBag == null ? null : optionsBag.getCreateIfNotExists();
        return withHttpsEndpointCallbackImpl(callback, name, createIfNotExists);
    }

    public PostgresServerResource withHttpsEndpointCallback(AspireAction1<EndpointUpdateContext> callback) {
        return withHttpsEndpointCallback(callback, null);
    }

    /** Updates an HTTPS endpoint via callback */
    private PostgresServerResource withHttpsEndpointCallbackImpl(AspireAction1<EndpointUpdateContext> callback, String name, Boolean createIfNotExists) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (EndpointUpdateContext) args[0];
            callback.invoke(obj);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", obj);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        if (name != null) {
            reqArgs.put("name", AspireClient.serializeValue(name));
        }
        if (createIfNotExists != null) {
            reqArgs.put("createIfNotExists", AspireClient.serializeValue(createIfNotExists));
        }
        getClient().invokeCapability("Aspire.Hosting/withHttpsEndpointCallback", reqArgs);
        return this;
    }

    /** Adds a network endpoint */
    public PostgresServerResource withEndpoint(WithEndpointOptions optionsBag) {
        var port = optionsBag == null ? null : optionsBag.getPort();
        var targetPort = optionsBag == null ? null : optionsBag.getTargetPort();
        var scheme = optionsBag == null ? null : optionsBag.getScheme();
        var name = optionsBag == null ? null : optionsBag.getName();
        var env = optionsBag == null ? null : optionsBag.getEnv();
        var isProxied = optionsBag == null ? null : optionsBag.isProxied();
        var isExternal = optionsBag == null ? null : optionsBag.isExternal();
        var protocol = optionsBag == null ? null : optionsBag.getProtocol();
        return withEndpointImpl(port, targetPort, scheme, name, env, isProxied, isExternal, protocol);
    }

    public PostgresServerResource withEndpoint() {
        return withEndpoint(null);
    }

    /** Adds a network endpoint */
    private PostgresServerResource withEndpointImpl(Number port, Number targetPort, String scheme, String name, String env, Boolean isProxied, Boolean isExternal, ProtocolType protocol) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (port != null) {
            reqArgs.put("port", AspireClient.serializeValue(port));
        }
        if (targetPort != null) {
            reqArgs.put("targetPort", AspireClient.serializeValue(targetPort));
        }
        if (scheme != null) {
            reqArgs.put("scheme", AspireClient.serializeValue(scheme));
        }
        if (name != null) {
            reqArgs.put("name", AspireClient.serializeValue(name));
        }
        if (env != null) {
            reqArgs.put("env", AspireClient.serializeValue(env));
        }
        if (isProxied != null) {
            reqArgs.put("isProxied", AspireClient.serializeValue(isProxied));
        }
        if (isExternal != null) {
            reqArgs.put("isExternal", AspireClient.serializeValue(isExternal));
        }
        if (protocol != null) {
            reqArgs.put("protocol", AspireClient.serializeValue(protocol));
        }
        getClient().invokeCapability("Aspire.Hosting/withEndpoint", reqArgs);
        return this;
    }

    /** Set whether a resource can use proxied endpoints or whether they should be disabled for all endpoints belonging to the resource. If set to `false`, endpoints belonging to the resource will ignore the configured proxy settings and run proxy-less. */
    public PostgresServerResource withEndpointProxySupport(boolean proxyEnabled) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("proxyEnabled", AspireClient.serializeValue(proxyEnabled));
        getClient().invokeCapability("Aspire.Hosting/withEndpointProxySupport", reqArgs);
        return this;
    }

    /** Adds an HTTP endpoint */
    public PostgresServerResource withHttpEndpoint(WithHttpEndpointOptions optionsBag) {
        var port = optionsBag == null ? null : optionsBag.getPort();
        var targetPort = optionsBag == null ? null : optionsBag.getTargetPort();
        var name = optionsBag == null ? null : optionsBag.getName();
        var env = optionsBag == null ? null : optionsBag.getEnv();
        var isProxied = optionsBag == null ? null : optionsBag.isProxied();
        return withHttpEndpointImpl(port, targetPort, name, env, isProxied);
    }

    public PostgresServerResource withHttpEndpoint() {
        return withHttpEndpoint(null);
    }

    /** Adds an HTTP endpoint */
    private PostgresServerResource withHttpEndpointImpl(Number port, Number targetPort, String name, String env, Boolean isProxied) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (port != null) {
            reqArgs.put("port", AspireClient.serializeValue(port));
        }
        if (targetPort != null) {
            reqArgs.put("targetPort", AspireClient.serializeValue(targetPort));
        }
        if (name != null) {
            reqArgs.put("name", AspireClient.serializeValue(name));
        }
        if (env != null) {
            reqArgs.put("env", AspireClient.serializeValue(env));
        }
        if (isProxied != null) {
            reqArgs.put("isProxied", AspireClient.serializeValue(isProxied));
        }
        getClient().invokeCapability("Aspire.Hosting/withHttpEndpoint", reqArgs);
        return this;
    }

    /** Adds an HTTPS endpoint */
    public PostgresServerResource withHttpsEndpoint(WithHttpsEndpointOptions optionsBag) {
        var port = optionsBag == null ? null : optionsBag.getPort();
        var targetPort = optionsBag == null ? null : optionsBag.getTargetPort();
        var name = optionsBag == null ? null : optionsBag.getName();
        var env = optionsBag == null ? null : optionsBag.getEnv();
        var isProxied = optionsBag == null ? null : optionsBag.isProxied();
        return withHttpsEndpointImpl(port, targetPort, name, env, isProxied);
    }

    public PostgresServerResource withHttpsEndpoint() {
        return withHttpsEndpoint(null);
    }

    /** Adds an HTTPS endpoint */
    private PostgresServerResource withHttpsEndpointImpl(Number port, Number targetPort, String name, String env, Boolean isProxied) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (port != null) {
            reqArgs.put("port", AspireClient.serializeValue(port));
        }
        if (targetPort != null) {
            reqArgs.put("targetPort", AspireClient.serializeValue(targetPort));
        }
        if (name != null) {
            reqArgs.put("name", AspireClient.serializeValue(name));
        }
        if (env != null) {
            reqArgs.put("env", AspireClient.serializeValue(env));
        }
        if (isProxied != null) {
            reqArgs.put("isProxied", AspireClient.serializeValue(isProxied));
        }
        getClient().invokeCapability("Aspire.Hosting/withHttpsEndpoint", reqArgs);
        return this;
    }

    /** Marks existing http or https endpoints on a resource as external. */
    public PostgresServerResource withExternalHttpEndpoints() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withExternalHttpEndpoints", reqArgs);
        return this;
    }

    /** Gets an endpoint reference */
    public EndpointReference getEndpoint(String name) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        var result = getClient().invokeCapability("Aspire.Hosting/getEndpoint", reqArgs);
        return (EndpointReference) result;
    }

    /** Configures a resource to mark all endpoints' transport as HTTP/2. This is useful for HTTP/2 services that need prior knowledge. */
    public PostgresServerResource asHttp2Service() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/asHttp2Service", reqArgs);
        return this;
    }

    /** Registers a callback to customize the URLs displayed for the resource. */
    public PostgresServerResource withUrls(AspireAction1<ResourceUrlsCallbackContext> callback) {
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

    public PostgresServerResource withUrl(String url, String displayText) {
        return withUrl(AspireUnion.of(url), displayText);
    }

    public PostgresServerResource withUrl(ReferenceExpression url, String displayText) {
        return withUrl(AspireUnion.of(url), displayText);
    }

    public PostgresServerResource withUrl(AspireUnion url) {
        return withUrl(url, null);
    }

    /** Adds or modifies displayed URLs */
    public PostgresServerResource withUrl(AspireUnion url, String displayText) {
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
    public PostgresServerResource withUrlForEndpoint(String endpointName, AspireAction1<ResourceUrlAnnotation> callback) {
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
    public PostgresServerResource excludeFromManifest() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/excludeFromManifest", reqArgs);
        return this;
    }

    public PostgresServerResource waitFor(IResource dependency) {
        return waitFor(dependency, null);
    }

    public PostgresServerResource waitFor(ResourceBuilderBase dependency) {
        return waitFor(new IResource(dependency.getHandle(), dependency.getClient()));
    }

    /** Waits for another resource to be ready */
    public PostgresServerResource waitFor(IResource dependency, WaitBehavior waitBehavior) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("dependency", AspireClient.serializeValue(dependency));
        if (waitBehavior != null) {
            reqArgs.put("waitBehavior", AspireClient.serializeValue(waitBehavior));
        }
        getClient().invokeCapability("Aspire.Hosting/waitFor", reqArgs);
        return this;
    }

    public PostgresServerResource waitFor(ResourceBuilderBase dependency, WaitBehavior waitBehavior) {
        return waitFor(new IResource(dependency.getHandle(), dependency.getClient()), waitBehavior);
    }

    public PostgresServerResource waitForStart(IResource dependency) {
        return waitForStart(dependency, null);
    }

    public PostgresServerResource waitForStart(ResourceBuilderBase dependency) {
        return waitForStart(new IResource(dependency.getHandle(), dependency.getClient()));
    }

    /** Waits for another resource to start */
    public PostgresServerResource waitForStart(IResource dependency, WaitBehavior waitBehavior) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("dependency", AspireClient.serializeValue(dependency));
        if (waitBehavior != null) {
            reqArgs.put("waitBehavior", AspireClient.serializeValue(waitBehavior));
        }
        getClient().invokeCapability("Aspire.Hosting/waitForStart", reqArgs);
        return this;
    }

    public PostgresServerResource waitForStart(ResourceBuilderBase dependency, WaitBehavior waitBehavior) {
        return waitForStart(new IResource(dependency.getHandle(), dependency.getClient()), waitBehavior);
    }

    /** Prevents resource from starting automatically */
    public PostgresServerResource withExplicitStart() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withExplicitStart", reqArgs);
        return this;
    }

    public PostgresServerResource waitForCompletion(IResource dependency) {
        return waitForCompletion(dependency, null);
    }

    public PostgresServerResource waitForCompletion(ResourceBuilderBase dependency) {
        return waitForCompletion(new IResource(dependency.getHandle(), dependency.getClient()));
    }

    /** Waits for the dependency resource to enter the Exited or Finished state before starting the resource. */
    public PostgresServerResource waitForCompletion(IResource dependency, Number exitCode) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("dependency", AspireClient.serializeValue(dependency));
        if (exitCode != null) {
            reqArgs.put("exitCode", AspireClient.serializeValue(exitCode));
        }
        getClient().invokeCapability("Aspire.Hosting/waitForResourceCompletion", reqArgs);
        return this;
    }

    public PostgresServerResource waitForCompletion(ResourceBuilderBase dependency, Number exitCode) {
        return waitForCompletion(new IResource(dependency.getHandle(), dependency.getClient()), exitCode);
    }

    /** Adds a health check by key */
    public PostgresServerResource withHealthCheck(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        getClient().invokeCapability("Aspire.Hosting/withHealthCheck", reqArgs);
        return this;
    }

    /** Adds a health check to the resource which is mapped to a specific endpoint. */
    public PostgresServerResource withHttpHealthCheck(WithHttpHealthCheckOptions optionsBag) {
        var path = optionsBag == null ? null : optionsBag.getPath();
        var statusCode = optionsBag == null ? null : optionsBag.getStatusCode();
        var endpointName = optionsBag == null ? null : optionsBag.getEndpointName();
        return withHttpHealthCheckImpl(path, statusCode, endpointName);
    }

    public PostgresServerResource withHttpHealthCheck() {
        return withHttpHealthCheck(null);
    }

    /** Adds a health check to the resource which is mapped to a specific endpoint. */
    private PostgresServerResource withHttpHealthCheckImpl(String path, Number statusCode, String endpointName) {
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
        getClient().invokeCapability("Aspire.Hosting/withHttpHealthCheck", reqArgs);
        return this;
    }

    public PostgresServerResource withCommand(String name, String displayName, AspireFunc1<ExecuteCommandContext, ExecuteCommandResult> executeCommand) {
        return withCommand(name, displayName, executeCommand, null);
    }

    /** Adds a resource command */
    public PostgresServerResource withCommand(String name, String displayName, AspireFunc1<ExecuteCommandContext, ExecuteCommandResult> executeCommand, CommandOptions commandOptions) {
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
    public PostgresServerResource withProcessCommand(String commandName, String displayName, ProcessCommandExportOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("commandName", AspireClient.serializeValue(commandName));
        reqArgs.put("displayName", AspireClient.serializeValue(displayName));
        reqArgs.put("options", AspireClient.serializeValue(options));
        getClient().invokeCapability("Aspire.Hosting/withProcessCommand", reqArgs);
        return this;
    }

    public PostgresServerResource withProcessCommandFactory(String commandName, String displayName, AspireFunc1<ExecuteCommandContext, ProcessCommandSpecExportData> createProcessSpec) {
        return withProcessCommandFactory(commandName, displayName, createProcessSpec, null);
    }

    /** Adds a command to the resource that starts a local process created by a callback when invoked. */
    public PostgresServerResource withProcessCommandFactory(String commandName, String displayName, AspireFunc1<ExecuteCommandContext, ProcessCommandSpecExportData> createProcessSpec, ProcessCommandResultExportOptions options) {
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

    public PostgresServerResource withHttpCommand(String path, String displayName) {
        return withHttpCommand(path, displayName, null);
    }

    /** Adds an HTTP resource command */
    public PostgresServerResource withHttpCommand(String path, String displayName, HttpCommandExportOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("path", AspireClient.serializeValue(path));
        reqArgs.put("displayName", AspireClient.serializeValue(displayName));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        getClient().invokeCapability("Aspire.Hosting/withHttpCommand", reqArgs);
        return this;
    }

    /** Indicates whether developer certificates should be treated as trusted certificate authorities for the resource at run time. Currently this indicates trust for the ASP.NET Core developer certificate. The developer certificate will only be trusted when running in local development scenarios; in publish mode resources will use their default certificate trust. */
    public PostgresServerResource withDeveloperCertificateTrust(boolean trust) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("trust", AspireClient.serializeValue(trust));
        getClient().invokeCapability("Aspire.Hosting/withDeveloperCertificateTrust", reqArgs);
        return this;
    }

    /** Sets the certificate trust scope */
    public PostgresServerResource withCertificateTrustScope(CertificateTrustScope scope) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("scope", AspireClient.serializeValue(scope));
        getClient().invokeCapability("Aspire.Hosting/withCertificateTrustScope", reqArgs);
        return this;
    }

    public PostgresServerResource withHttpsDeveloperCertificate() {
        return withHttpsDeveloperCertificate(null);
    }

    /** Indicates that a resource should use the developer certificate key pair for HTTPS endpoints at run time. Currently this indicates use of the ASP.NET Core developer certificate. The developer certificate will only be used when running in local development scenarios; in publish mode resources will use their default certificate configuration. */
    public PostgresServerResource withHttpsDeveloperCertificate(ParameterResource password) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (password != null) {
            reqArgs.put("password", AspireClient.serializeValue(password));
        }
        getClient().invokeCapability("Aspire.Hosting/withParameterHttpsDeveloperCertificate", reqArgs);
        return this;
    }

    /** Disable HTTPS/TLS server certificate configuration for the resource. No HTTPS/TLS termination configuration will be applied. */
    public PostgresServerResource withoutHttpsCertificate() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withoutHttpsCertificate", reqArgs);
        return this;
    }

    /** Adds a callback that allows configuring the resource to use a specific HTTPS/TLS certificate key pair for server authentication. */
    public PostgresServerResource withHttpsCertificateConfiguration(AspireAction1<HttpsCertificateConfigurationCallbackAnnotationContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (HttpsCertificateConfigurationCallbackAnnotationContext) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withHttpsCertificateConfiguration", reqArgs);
        return this;
    }

    /** Subscribes to the `BeforeStartEvent` and invokes the specified callback when an HTTPS certificate is determined to be available for the resource. This is used to conditionally update endpoint URI schemes or perform other HTTPS-related configuration at startup. */
    public PostgresServerResource subscribeHttpsEndpointsUpdate(AspireAction1<HttpsEndpointUpdateCallbackContext> callback) {
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
    public PostgresServerResource withRelationship(IResource resourceBuilder, String type) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceBuilder", AspireClient.serializeValue(resourceBuilder));
        reqArgs.put("type", AspireClient.serializeValue(type));
        getClient().invokeCapability("Aspire.Hosting/withBuilderRelationship", reqArgs);
        return this;
    }

    public PostgresServerResource withRelationship(ResourceBuilderBase resourceBuilder, String type) {
        return withRelationship(new IResource(resourceBuilder.getHandle(), resourceBuilder.getClient()), type);
    }

    /** Sets the parent relationship */
    public PostgresServerResource withParentRelationship(IResource parent) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("parent", AspireClient.serializeValue(parent));
        getClient().invokeCapability("Aspire.Hosting/withBuilderParentRelationship", reqArgs);
        return this;
    }

    public PostgresServerResource withParentRelationship(ResourceBuilderBase parent) {
        return withParentRelationship(new IResource(parent.getHandle(), parent.getClient()));
    }

    /** Sets a child relationship */
    public PostgresServerResource withChildRelationship(IResource child) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("child", AspireClient.serializeValue(child));
        getClient().invokeCapability("Aspire.Hosting/withBuilderChildRelationship", reqArgs);
        return this;
    }

    public PostgresServerResource withChildRelationship(ResourceBuilderBase child) {
        return withChildRelationship(new IResource(child.getHandle(), child.getClient()));
    }

    public PostgresServerResource withIconName(String iconName) {
        return withIconName(iconName, null);
    }

    /** Specifies the icon to use when displaying the resource in the dashboard. */
    public PostgresServerResource withIconName(String iconName, IconVariant iconVariant) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("iconName", AspireClient.serializeValue(iconName));
        if (iconVariant != null) {
            reqArgs.put("iconVariant", AspireClient.serializeValue(iconVariant));
        }
        getClient().invokeCapability("Aspire.Hosting/withIconName", reqArgs);
        return this;
    }

    /** Configures the compute environment for the compute resource. */
    public PostgresServerResource withComputeEnvironment(IComputeEnvironmentResource computeEnvironmentResource) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("computeEnvironmentResource", AspireClient.serializeValue(computeEnvironmentResource));
        getClient().invokeCapability("Aspire.Hosting/withComputeEnvironment", reqArgs);
        return this;
    }

    public PostgresServerResource withComputeEnvironment(ResourceBuilderBase computeEnvironmentResource) {
        return withComputeEnvironment(new IComputeEnvironmentResource(computeEnvironmentResource.getHandle(), computeEnvironmentResource.getClient()));
    }

    /** Adds an HTTP health probe to the resource */
    public PostgresServerResource withHttpProbe(ProbeType probeType, WithHttpProbeOptions optionsBag) {
        var path = optionsBag == null ? null : optionsBag.getPath();
        var initialDelaySeconds = optionsBag == null ? null : optionsBag.getInitialDelaySeconds();
        var periodSeconds = optionsBag == null ? null : optionsBag.getPeriodSeconds();
        var timeoutSeconds = optionsBag == null ? null : optionsBag.getTimeoutSeconds();
        var failureThreshold = optionsBag == null ? null : optionsBag.getFailureThreshold();
        var successThreshold = optionsBag == null ? null : optionsBag.getSuccessThreshold();
        var endpointName = optionsBag == null ? null : optionsBag.getEndpointName();
        return withHttpProbeImpl(probeType, path, initialDelaySeconds, periodSeconds, timeoutSeconds, failureThreshold, successThreshold, endpointName);
    }

    public PostgresServerResource withHttpProbe(ProbeType probeType) {
        return withHttpProbe(probeType, null);
    }

    /** Adds an HTTP health probe to the resource */
    private PostgresServerResource withHttpProbeImpl(ProbeType probeType, String path, Number initialDelaySeconds, Number periodSeconds, Number timeoutSeconds, Number failureThreshold, Number successThreshold, String endpointName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("probeType", AspireClient.serializeValue(probeType));
        if (path != null) {
            reqArgs.put("path", AspireClient.serializeValue(path));
        }
        if (initialDelaySeconds != null) {
            reqArgs.put("initialDelaySeconds", AspireClient.serializeValue(initialDelaySeconds));
        }
        if (periodSeconds != null) {
            reqArgs.put("periodSeconds", AspireClient.serializeValue(periodSeconds));
        }
        if (timeoutSeconds != null) {
            reqArgs.put("timeoutSeconds", AspireClient.serializeValue(timeoutSeconds));
        }
        if (failureThreshold != null) {
            reqArgs.put("failureThreshold", AspireClient.serializeValue(failureThreshold));
        }
        if (successThreshold != null) {
            reqArgs.put("successThreshold", AspireClient.serializeValue(successThreshold));
        }
        if (endpointName != null) {
            reqArgs.put("endpointName", AspireClient.serializeValue(endpointName));
        }
        getClient().invokeCapability("Aspire.Hosting/withHttpProbe", reqArgs);
        return this;
    }

    /** Exclude the resource from MCP operations using the Aspire MCP server. The resource is excluded from results that return resources, console logs and telemetry. */
    public PostgresServerResource excludeFromMcp() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/excludeFromMcp", reqArgs);
        return this;
    }

    /** Hides the resource from default resource lists */
    public PostgresServerResource withHidden() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withHidden", reqArgs);
        return this;
    }

    /** Hides the resource from default resource lists after successful completion */
    public PostgresServerResource withHiddenOnCompletion(WithHiddenOnCompletionOptions optionsBag) {
        var exitCode = optionsBag == null ? null : optionsBag.getExitCode();
        var exitCodes = optionsBag == null ? null : optionsBag.getExitCodes();
        return withHiddenOnCompletionImpl(exitCode, exitCodes);
    }

    public PostgresServerResource withHiddenOnCompletion() {
        return withHiddenOnCompletion(null);
    }

    /** Hides the resource from default resource lists after successful completion */
    private PostgresServerResource withHiddenOnCompletionImpl(Number exitCode, double[] exitCodes) {
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

    /** Adds an asynchronous callback to configure container image push options for the resource. */
    public PostgresServerResource withImagePushOptions(AspireAction1<ContainerImagePushOptionsCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ContainerImagePushOptionsCallbackContext) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withImagePushOptions", reqArgs);
        return this;
    }

    /** Sets the remote image name (without registry endpoint or tag) for container push operations. */
    public PostgresServerResource withRemoteImageName(String remoteImageName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("remoteImageName", AspireClient.serializeValue(remoteImageName));
        getClient().invokeCapability("Aspire.Hosting/withRemoteImageName", reqArgs);
        return this;
    }

    /** Sets the remote image tag for container push operations. */
    public PostgresServerResource withRemoteImageTag(String remoteImageTag) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("remoteImageTag", AspireClient.serializeValue(remoteImageTag));
        getClient().invokeCapability("Aspire.Hosting/withRemoteImageTag", reqArgs);
        return this;
    }

    /** Adds an interactive terminal session to a resource using the default terminal options. */
    public PostgresServerResource withTerminal() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withTerminal", reqArgs);
        return this;
    }

    /** Adds a pipeline step to the resource that will be executed during deployment. */
    public PostgresServerResource withPipelineStepFactory(String stepName, AspireAction1<PipelineStepContext> callback, WithPipelineStepFactoryOptions optionsBag) {
        var dependsOn = optionsBag == null ? null : optionsBag.getDependsOn();
        var requiredBy = optionsBag == null ? null : optionsBag.getRequiredBy();
        var tags = optionsBag == null ? null : optionsBag.getTags();
        var description = optionsBag == null ? null : optionsBag.getDescription();
        return withPipelineStepFactoryImpl(stepName, callback, dependsOn, requiredBy, tags, description);
    }

    public PostgresServerResource withPipelineStepFactory(String stepName, AspireAction1<PipelineStepContext> callback) {
        return withPipelineStepFactory(stepName, callback, null);
    }

    /** Adds a pipeline step to the resource that will be executed during deployment. */
    private PostgresServerResource withPipelineStepFactoryImpl(String stepName, AspireAction1<PipelineStepContext> callback, String[] dependsOn, String[] requiredBy, String[] tags, String description) {
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
    public PostgresServerResource withPipelineConfiguration(AspireAction1<PipelineConfigurationContext> callback) {
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

    /** Adds a volume to a container resource. */
    public PostgresServerResource withVolume(String target, WithVolumeOptions optionsBag) {
        var name = optionsBag == null ? null : optionsBag.getName();
        var isReadOnly = optionsBag == null ? null : optionsBag.isReadOnly();
        return withVolumeImpl(target, name, isReadOnly);
    }

    public PostgresServerResource withVolume(String target) {
        return withVolume(target, null);
    }

    /** Adds a volume to a container resource. */
    private PostgresServerResource withVolumeImpl(String target, String name, Boolean isReadOnly) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("resource", AspireClient.serializeValue(getHandle()));
        reqArgs.put("target", AspireClient.serializeValue(target));
        if (name != null) {
            reqArgs.put("name", AspireClient.serializeValue(name));
        }
        if (isReadOnly != null) {
            reqArgs.put("isReadOnly", AspireClient.serializeValue(isReadOnly));
        }
        getClient().invokeCapability("Aspire.Hosting/withVolume", reqArgs);
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
    public PostgresServerResource onBeforeResourceStarted(AspireAction1<BeforeResourceStartedEvent> callback) {
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
    public PostgresServerResource onResourceStopped(AspireAction1<ResourceStoppedEvent> callback) {
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
    public PostgresServerResource onConnectionStringAvailable(AspireAction1<ConnectionStringAvailableEvent> callback) {
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
    public PostgresServerResource onInitializeResource(AspireAction1<InitializeResourceEvent> callback) {
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

    /** Subscribes to the ResourceEndpointsAllocated event. */
    public PostgresServerResource onResourceEndpointsAllocated(AspireAction1<ResourceEndpointsAllocatedEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ResourceEndpointsAllocatedEvent) args[0];
            callback.invoke(arg);
            var __aspireCallbackArguments = new HashMap<String, Object>();
            __aspireCallbackArguments.put("p0", arg);
            return __aspireCallbackArguments;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onResourceEndpointsAllocated", reqArgs);
        return this;
    }

    /** Subscribes to the ResourceReady event. */
    public PostgresServerResource onResourceReady(AspireAction1<ResourceReadyEvent> callback) {
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
    public PostgresServerResource withContainerBuildOptions(AspireAction1<ContainerBuildOptionsCallbackContext> callback) {
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

    /** Adds a REPL command that opens an authenticated PostgreSQL shell in the dashboard terminal dock. */
    public PostgresServerResource withRepl() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withRepl", reqArgs);
        return this;
    }

    public PostgresDatabaseResource addDatabase(String name) {
        return addDatabase(name, null);
    }

    /** Adds a PostgreSQL database to the application model. */
    public PostgresDatabaseResource addDatabase(String name, String databaseName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (databaseName != null) {
            reqArgs.put("databaseName", AspireClient.serializeValue(databaseName));
        }
        var result = getClient().invokeCapability("Aspire.Hosting.PostgreSQL/addDatabase", reqArgs);
        return (PostgresDatabaseResource) result;
    }

    /** Adds a pgAdmin 4 administration and development platform for PostgreSQL to the application model. */
    public PostgresServerResource withPgAdmin(WithPgAdminOptions optionsBag) {
        var configureContainer = optionsBag == null ? null : optionsBag.getConfigureContainer();
        var containerName = optionsBag == null ? null : optionsBag.getContainerName();
        return withPgAdminImpl(configureContainer, containerName);
    }

    public PostgresServerResource withPgAdmin() {
        return withPgAdmin(null);
    }

    /** Adds a pgAdmin 4 administration and development platform for PostgreSQL to the application model. */
    private PostgresServerResource withPgAdminImpl(AspireAction1<PgAdminContainerResource> configureContainer, String containerName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var configureContainerId = configureContainer == null ? null : getClient().registerCallback(args -> {
            var obj = (PgAdminContainerResource) args[0];
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
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withPgAdmin", reqArgs);
        return this;
    }

    /** Adds an administration and development platform for PostgreSQL to the application model using pgweb. */
    public PostgresServerResource withPgWeb(WithPgWebOptions optionsBag) {
        var configureContainer = optionsBag == null ? null : optionsBag.getConfigureContainer();
        var containerName = optionsBag == null ? null : optionsBag.getContainerName();
        return withPgWebImpl(configureContainer, containerName);
    }

    public PostgresServerResource withPgWeb() {
        return withPgWeb(null);
    }

    /** Adds an administration and development platform for PostgreSQL to the application model using pgweb. */
    private PostgresServerResource withPgWebImpl(AspireAction1<PgWebContainerResource> configureContainer, String containerName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var configureContainerId = configureContainer == null ? null : getClient().registerCallback(args -> {
            var obj = (PgWebContainerResource) args[0];
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
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withPgWeb", reqArgs);
        return this;
    }

    /** Adds a named volume for the data folder to a PostgreSQL container resource. */
    public PostgresServerResource withDataVolume(WithDataVolumeOptions optionsBag) {
        var name = optionsBag == null ? null : optionsBag.getName();
        var isReadOnly = optionsBag == null ? null : optionsBag.isReadOnly();
        return withDataVolumeImpl(name, isReadOnly);
    }

    public PostgresServerResource withDataVolume() {
        return withDataVolume(null);
    }

    /** Adds a named volume for the data folder to a PostgreSQL container resource. */
    private PostgresServerResource withDataVolumeImpl(String name, Boolean isReadOnly) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (name != null) {
            reqArgs.put("name", AspireClient.serializeValue(name));
        }
        if (isReadOnly != null) {
            reqArgs.put("isReadOnly", AspireClient.serializeValue(isReadOnly));
        }
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withDataVolume", reqArgs);
        return this;
    }

    public PostgresServerResource withDataBindMount(String source) {
        return withDataBindMount(source, null);
    }

    /** Adds a bind mount for the data folder to a PostgreSQL container resource. */
    public PostgresServerResource withDataBindMount(String source, Boolean isReadOnly) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("source", AspireClient.serializeValue(source));
        if (isReadOnly != null) {
            reqArgs.put("isReadOnly", AspireClient.serializeValue(isReadOnly));
        }
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withDataBindMount", reqArgs);
        return this;
    }

    /** Copies init files to a PostgreSQL container resource. */
    public PostgresServerResource withInitFiles(String source) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("source", AspireClient.serializeValue(source));
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withInitFiles", reqArgs);
        return this;
    }

    /** Configures the password that the PostgreSQL resource is used. */
    public PostgresServerResource withPassword(ParameterResource password) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("password", AspireClient.serializeValue(password));
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withPassword", reqArgs);
        return this;
    }

    /** Configures the user name that the PostgreSQL resource is used. */
    public PostgresServerResource withUserName(ParameterResource userName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("userName", AspireClient.serializeValue(userName));
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withUserName", reqArgs);
        return this;
    }

    public PostgresServerResource withHostPort() {
        return withHostPort(null);
    }

    /** Configures the host port that the PostgreSQL resource is exposed on instead of using randomly assigned port. */
    public PostgresServerResource withHostPort(Number port) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (port != null) {
            reqArgs.put("port", AspireClient.serializeValue(port));
        }
        getClient().invokeCapability("Aspire.Hosting.PostgreSQL/withPostgresHostPort", reqArgs);
        return this;
    }

    /** Gets the primary endpoint for the PostgreSQL server. */
    public EndpointReference primaryEndpoint() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.primaryEndpoint", reqArgs);
        return (EndpointReference) result;
    }

    /** Gets or sets the parameter that contains the PostgreSQL server user name. */
    public ParameterResource userNameParameter() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.userNameParameter", reqArgs);
        return (ParameterResource) result;
    }

    /** Sets the UserNameParameter property */
    public PostgresServerResource setUserNameParameter(ParameterResource value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.setUserNameParameter", reqArgs);
        return (PostgresServerResource) result;
    }

    /** Gets a reference to the user name for the PostgreSQL server. */
    public ReferenceExpression userNameReference() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.userNameReference", reqArgs);
        return (ReferenceExpression) result;
    }

    /** Gets or sets the parameter that contains the PostgreSQL server password. */
    public ParameterResource passwordParameter() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.passwordParameter", reqArgs);
        return (ParameterResource) result;
    }

    /** Sets the PasswordParameter property */
    public PostgresServerResource setPasswordParameter(ParameterResource value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.setPasswordParameter", reqArgs);
        return (PostgresServerResource) result;
    }

    /** Gets the connection string expression for the PostgreSQL server. */
    public ReferenceExpression connectionStringExpression() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.connectionStringExpression", reqArgs);
        return (ReferenceExpression) result;
    }

    /** A dictionary where the key is the resource name and the value is the database name. */
    private AspireDict<String, String> databasesField;
    public AspireDict<String, String> databases() {
        if (databasesField == null) {
            databasesField = new AspireDict<>(getHandle(), getClient(), "Aspire.Hosting.ApplicationModel/PostgresServerResource.databases");
        }
        return databasesField;
    }

    /** Gets the host endpoint reference for this service. */
    public EndpointReferenceExpression host() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.host", reqArgs);
        return (EndpointReferenceExpression) result;
    }

    /** Gets the endpoint reference expression that identifies the port for this endpoint. */
    public EndpointReferenceExpression port() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.port", reqArgs);
        return (EndpointReferenceExpression) result;
    }

    /** Gets the connection URI expression for the PostgreSQL server. */
    public ReferenceExpression uriExpression() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.uriExpression", reqArgs);
        return (ReferenceExpression) result;
    }

    /** Gets the JDBC connection string for the PostgreSQL server. */
    public ReferenceExpression jdbcConnectionString() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.jdbcConnectionString", reqArgs);
        return (ReferenceExpression) result;
    }

    /** The container Entrypoint. */
    public String entrypoint() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.entrypoint", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Sets the Entrypoint property */
    public PostgresServerResource setEntrypoint(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.setEntrypoint", reqArgs);
        return (PostgresServerResource) result;
    }

    /** Should any custom arguments be wrapped in -c ">values<"? */
    public Boolean shellExecution() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.shellExecution", reqArgs);
        return result == null ? null : (Boolean) result;
    }

    /** Sets the ShellExecution property */
    public PostgresServerResource setShellExecution(Boolean value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.setShellExecution", reqArgs);
        return (PostgresServerResource) result;
    }

    /** Gets the name of the resource. */
    public String name() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/PostgresServerResource.name", reqArgs);
        return (String) result;
    }

}
