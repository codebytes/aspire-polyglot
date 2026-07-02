// CSharpAppResource.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.CSharpAppResource. */
public class CSharpAppResource extends ProjectResource {
    CSharpAppResource(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Configures the resource to use the specified container registry for container image operations. */
    public CSharpAppResource withContainerRegistry(IResource registry) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("registry", AspireClient.serializeValue(registry));
        getClient().invokeCapability("Aspire.Hosting/withContainerRegistry", reqArgs);
        return this;
    }

    public CSharpAppResource withContainerRegistry(ResourceBuilderBase registry) {
        return withContainerRegistry(new IResource(registry.getHandle(), registry.getClient()));
    }

    /** Configures custom base images for generated Dockerfiles. */
    public CSharpAppResource withDockerfileBaseImage(WithDockerfileBaseImageOptions options) {
        var buildImage = options == null ? null : options.getBuildImage();
        var runtimeImage = options == null ? null : options.getRuntimeImage();
        return withDockerfileBaseImageImpl(buildImage, runtimeImage);
    }

    public CSharpAppResource withDockerfileBaseImage() {
        return withDockerfileBaseImage(null);
    }

    /** Configures custom base images for generated Dockerfiles. */
    private CSharpAppResource withDockerfileBaseImageImpl(String buildImage, String runtimeImage) {
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

    /** Marks the resource as hosting a Model Context Protocol (MCP) server on the specified endpoint. */
    public CSharpAppResource withMcpServer(WithMcpServerOptions options) {
        var path = options == null ? null : options.getPath();
        var endpointName = options == null ? null : options.getEndpointName();
        return withMcpServerImpl(path, endpointName);
    }

    public CSharpAppResource withMcpServer() {
        return withMcpServer(null);
    }

    /** Marks the resource as hosting a Model Context Protocol (MCP) server on the specified endpoint. */
    private CSharpAppResource withMcpServerImpl(String path, String endpointName) {
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

    public CSharpAppResource withOtlpExporter() {
        return withOtlpExporter(null);
    }

    /** Configures OTLP telemetry export */
    public CSharpAppResource withOtlpExporter(OtlpProtocol protocol) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (protocol != null) {
            reqArgs.put("protocol", AspireClient.serializeValue(protocol));
        }
        getClient().invokeCapability("Aspire.Hosting/withOtlpExporter", reqArgs);
        return this;
    }

    /** Configures how many replicas of the project should be created for the project. */
    public CSharpAppResource withReplicas(double replicas) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("replicas", AspireClient.serializeValue(replicas));
        getClient().invokeCapability("Aspire.Hosting/withReplicas", reqArgs);
        return this;
    }

    /** Configures the project to disable forwarded headers when being published. */
    public CSharpAppResource disableForwardedHeaders() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/disableForwardedHeaders", reqArgs);
        return this;
    }

    public CSharpAppResource publishAsDockerFile() {
        return publishAsDockerFile(null);
    }

    /** Publishes a project as a Docker file with optional container configuration */
    public CSharpAppResource publishAsDockerFile(AspireAction1<ContainerResource> configure) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var configureId = configure == null ? null : getClient().registerCallback(args -> {
            var obj = (ContainerResource) args[0];
            configure.invoke(obj);
            return null;
        });
        if (configureId != null) {
            reqArgs.put("configure", configureId);
        }
        getClient().invokeCapability("Aspire.Hosting/publishProjectAsDockerFileWithConfigure", reqArgs);
        return this;
    }

    public CSharpAppResource withRequiredCommand(String command) {
        return withRequiredCommand(command, null);
    }

    /** Declares that a resource requires a specific command/executable to be available on the local machine PATH before it can start. */
    public CSharpAppResource withRequiredCommand(String command, String helpLink) {
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
    public CSharpAppResource withSessionLifetime() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withSessionLifetime", reqArgs);
        return this;
    }

    /** Configures a resource to use a persistent lifetime. */
    public CSharpAppResource withPersistentLifetime() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withPersistentLifetime", reqArgs);
        return this;
    }

    /** Configures a resource to match the lifetime of another resource. */
    public CSharpAppResource withLifetimeOf(IResource sourceBuilder) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("sourceBuilder", AspireClient.serializeValue(sourceBuilder));
        getClient().invokeCapability("Aspire.Hosting/withLifetimeOf", reqArgs);
        return this;
    }

    public CSharpAppResource withLifetimeOf(ResourceBuilderBase sourceBuilder) {
        return withLifetimeOf(new IResource(sourceBuilder.getHandle(), sourceBuilder.getClient()));
    }

    /** Configures a resource to use a persistent lifetime that ends when a parent process exits. */
    public CSharpAppResource withParentProcessLifetime(double parentProcessId) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("parentProcessId", AspireClient.serializeValue(parentProcessId));
        getClient().invokeCapability("Aspire.Hosting/withParentProcessLifetime", reqArgs);
        return this;
    }

    public CSharpAppResource withEnvironment(String name, String value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public CSharpAppResource withEnvironment(String name, ReferenceExpression value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public CSharpAppResource withEnvironment(String name, EndpointReference value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public CSharpAppResource withEnvironment(String name, ParameterResource value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public CSharpAppResource withEnvironment(String name, ExternalServiceResource value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public CSharpAppResource withEnvironment(String name, IResourceWithConnectionString value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public CSharpAppResource withEnvironment(String name, ResourceBuilderBase value) {
        return withEnvironment(name, new IResourceWithConnectionString(value.getHandle(), value.getClient()));
    }

    public CSharpAppResource withEnvironment(String name, IExpressionValue value) {
        return withEnvironment(name, AspireUnion.of(value));
    }

    public CSharpAppResource withEnvironment(String name, HandleWrapperBase value) {
        return withEnvironment(name, new IExpressionValue(value.getHandle(), value.getClient()));
    }

    /** Sets an environment variable */
    public CSharpAppResource withEnvironment(String name, AspireUnion value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/withEnvironment", reqArgs);
        return this;
    }

    /** Allows for the population of environment variables on a resource. */
    public CSharpAppResource withEnvironmentCallback(AspireAction1<EnvironmentCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (EnvironmentCallbackContext) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withEnvironmentCallback", reqArgs);
        return this;
    }

    /** Adds arguments to be passed to a resource that supports arguments when it is launched. */
    public CSharpAppResource withArgs(String[] args) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("args", AspireClient.serializeValue(args));
        getClient().invokeCapability("Aspire.Hosting/withArgs", reqArgs);
        return this;
    }

    /** Adds a callback to be executed with a list of command-line arguments when a resource is started. */
    public CSharpAppResource withArgsCallback(AspireAction1<CommandLineArgsCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (CommandLineArgsCallbackContext) args[0];
            callback.invoke(obj);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withArgsCallback", reqArgs);
        return this;
    }

    /** Configures how information is injected into environment variables when the resource references other resources. */
    public CSharpAppResource withReferenceEnvironment(ReferenceEnvironmentInjectionOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("options", AspireClient.serializeValue(options));
        getClient().invokeCapability("Aspire.Hosting/withReferenceEnvironment", reqArgs);
        return this;
    }

    public CSharpAppResource withReference(IResource source, WithReferenceOptions options) {
        return withReference(AspireUnion.of(source), options);
    }

    public CSharpAppResource withReference(ResourceBuilderBase source, WithReferenceOptions options) {
        return withReference(new IResource(source.getHandle(), source.getClient()), options);
    }

    public CSharpAppResource withReference(IResource source) {
        return withReference(AspireUnion.of(source));
    }

    public CSharpAppResource withReference(EndpointReference source, WithReferenceOptions options) {
        return withReference(AspireUnion.of(source), options);
    }

    public CSharpAppResource withReference(EndpointReference source) {
        return withReference(AspireUnion.of(source));
    }

    public CSharpAppResource withReference(String source, WithReferenceOptions options) {
        return withReference(AspireUnion.of(source), options);
    }

    public CSharpAppResource withReference(String source) {
        return withReference(AspireUnion.of(source));
    }

    /** Adds a reference to another resource */
    public CSharpAppResource withReference(AspireUnion source, WithReferenceOptions options) {
        var connectionName = options == null ? null : options.getConnectionName();
        var optional = options == null ? null : options.getOptional();
        var name = options == null ? null : options.getName();
        return withReferenceImpl(source, connectionName, optional, name);
    }

    public CSharpAppResource withReference(AspireUnion source) {
        return withReference(source, null);
    }

    /** Adds a reference to another resource */
    private CSharpAppResource withReferenceImpl(AspireUnion source, String connectionName, Boolean optional, String name) {
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

    public CSharpAppResource withEndpointCallback(String endpointName, AspireAction1<EndpointUpdateContext> callback) {
        return withEndpointCallback(endpointName, callback, null);
    }

    /** Updates a named endpoint via callback */
    public CSharpAppResource withEndpointCallback(String endpointName, AspireAction1<EndpointUpdateContext> callback, Boolean createIfNotExists) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("endpointName", AspireClient.serializeValue(endpointName));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (EndpointUpdateContext) args[0];
            callback.invoke(obj);
            return null;
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
    public CSharpAppResource withHttpEndpointCallback(AspireAction1<EndpointUpdateContext> callback, WithHttpEndpointCallbackOptions options) {
        var name = options == null ? null : options.getName();
        var createIfNotExists = options == null ? null : options.getCreateIfNotExists();
        return withHttpEndpointCallbackImpl(callback, name, createIfNotExists);
    }

    public CSharpAppResource withHttpEndpointCallback(AspireAction1<EndpointUpdateContext> callback) {
        return withHttpEndpointCallback(callback, null);
    }

    /** Updates an HTTP endpoint via callback */
    private CSharpAppResource withHttpEndpointCallbackImpl(AspireAction1<EndpointUpdateContext> callback, String name, Boolean createIfNotExists) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (EndpointUpdateContext) args[0];
            callback.invoke(obj);
            return null;
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
    public CSharpAppResource withHttpsEndpointCallback(AspireAction1<EndpointUpdateContext> callback, WithHttpsEndpointCallbackOptions options) {
        var name = options == null ? null : options.getName();
        var createIfNotExists = options == null ? null : options.getCreateIfNotExists();
        return withHttpsEndpointCallbackImpl(callback, name, createIfNotExists);
    }

    public CSharpAppResource withHttpsEndpointCallback(AspireAction1<EndpointUpdateContext> callback) {
        return withHttpsEndpointCallback(callback, null);
    }

    /** Updates an HTTPS endpoint via callback */
    private CSharpAppResource withHttpsEndpointCallbackImpl(AspireAction1<EndpointUpdateContext> callback, String name, Boolean createIfNotExists) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var obj = (EndpointUpdateContext) args[0];
            callback.invoke(obj);
            return null;
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
    public CSharpAppResource withEndpoint(WithEndpointOptions options) {
        var port = options == null ? null : options.getPort();
        var targetPort = options == null ? null : options.getTargetPort();
        var scheme = options == null ? null : options.getScheme();
        var name = options == null ? null : options.getName();
        var env = options == null ? null : options.getEnv();
        var isProxied = options == null ? null : options.isProxied();
        var isExternal = options == null ? null : options.isExternal();
        var protocol = options == null ? null : options.getProtocol();
        return withEndpointImpl(port, targetPort, scheme, name, env, isProxied, isExternal, protocol);
    }

    public CSharpAppResource withEndpoint() {
        return withEndpoint(null);
    }

    /** Adds a network endpoint */
    private CSharpAppResource withEndpointImpl(Double port, Double targetPort, String scheme, String name, String env, Boolean isProxied, Boolean isExternal, ProtocolType protocol) {
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
    public CSharpAppResource withEndpointProxySupport(boolean proxyEnabled) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("proxyEnabled", AspireClient.serializeValue(proxyEnabled));
        getClient().invokeCapability("Aspire.Hosting/withEndpointProxySupport", reqArgs);
        return this;
    }

    /** Adds an HTTP endpoint */
    public CSharpAppResource withHttpEndpoint(WithHttpEndpointOptions options) {
        var port = options == null ? null : options.getPort();
        var targetPort = options == null ? null : options.getTargetPort();
        var name = options == null ? null : options.getName();
        var env = options == null ? null : options.getEnv();
        var isProxied = options == null ? null : options.isProxied();
        return withHttpEndpointImpl(port, targetPort, name, env, isProxied);
    }

    public CSharpAppResource withHttpEndpoint() {
        return withHttpEndpoint(null);
    }

    /** Adds an HTTP endpoint */
    private CSharpAppResource withHttpEndpointImpl(Double port, Double targetPort, String name, String env, Boolean isProxied) {
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
    public CSharpAppResource withHttpsEndpoint(WithHttpsEndpointOptions options) {
        var port = options == null ? null : options.getPort();
        var targetPort = options == null ? null : options.getTargetPort();
        var name = options == null ? null : options.getName();
        var env = options == null ? null : options.getEnv();
        var isProxied = options == null ? null : options.isProxied();
        return withHttpsEndpointImpl(port, targetPort, name, env, isProxied);
    }

    public CSharpAppResource withHttpsEndpoint() {
        return withHttpsEndpoint(null);
    }

    /** Adds an HTTPS endpoint */
    private CSharpAppResource withHttpsEndpointImpl(Double port, Double targetPort, String name, String env, Boolean isProxied) {
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
    public CSharpAppResource withExternalHttpEndpoints() {
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
    public CSharpAppResource asHttp2Service() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/asHttp2Service", reqArgs);
        return this;
    }

    /** Registers a callback to customize the URLs displayed for the resource. */
    public CSharpAppResource withUrls(AspireAction1<ResourceUrlsCallbackContext> callback) {
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

    public CSharpAppResource withUrl(String url, String displayText) {
        return withUrl(AspireUnion.of(url), displayText);
    }

    public CSharpAppResource withUrl(ReferenceExpression url, String displayText) {
        return withUrl(AspireUnion.of(url), displayText);
    }

    public CSharpAppResource withUrl(AspireUnion url) {
        return withUrl(url, null);
    }

    /** Adds or modifies displayed URLs */
    public CSharpAppResource withUrl(AspireUnion url, String displayText) {
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
    public CSharpAppResource withUrlForEndpoint(String endpointName, AspireAction1<ResourceUrlAnnotation> callback) {
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

    /** Configures the resource to copy container files from the specified source resource during publishing. */
    public CSharpAppResource publishWithContainerFiles(IResourceWithContainerFiles source, String destinationPath) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("source", AspireClient.serializeValue(source));
        reqArgs.put("destinationPath", AspireClient.serializeValue(destinationPath));
        getClient().invokeCapability("Aspire.Hosting/publishWithContainerFilesFromResource", reqArgs);
        return this;
    }

    public CSharpAppResource publishWithContainerFiles(ResourceBuilderBase source, String destinationPath) {
        return publishWithContainerFiles(new IResourceWithContainerFiles(source.getHandle(), source.getClient()), destinationPath);
    }

    /** Excludes a resource from being published to the manifest. */
    public CSharpAppResource excludeFromManifest() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/excludeFromManifest", reqArgs);
        return this;
    }

    public CSharpAppResource waitFor(IResource dependency) {
        return waitFor(dependency, null);
    }

    public CSharpAppResource waitFor(ResourceBuilderBase dependency) {
        return waitFor(new IResource(dependency.getHandle(), dependency.getClient()));
    }

    /** Waits for another resource to be ready */
    public CSharpAppResource waitFor(IResource dependency, WaitBehavior waitBehavior) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("dependency", AspireClient.serializeValue(dependency));
        if (waitBehavior != null) {
            reqArgs.put("waitBehavior", AspireClient.serializeValue(waitBehavior));
        }
        getClient().invokeCapability("Aspire.Hosting/waitFor", reqArgs);
        return this;
    }

    public CSharpAppResource waitFor(ResourceBuilderBase dependency, WaitBehavior waitBehavior) {
        return waitFor(new IResource(dependency.getHandle(), dependency.getClient()), waitBehavior);
    }

    public CSharpAppResource waitForStart(IResource dependency) {
        return waitForStart(dependency, null);
    }

    public CSharpAppResource waitForStart(ResourceBuilderBase dependency) {
        return waitForStart(new IResource(dependency.getHandle(), dependency.getClient()));
    }

    /** Waits for another resource to start */
    public CSharpAppResource waitForStart(IResource dependency, WaitBehavior waitBehavior) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("dependency", AspireClient.serializeValue(dependency));
        if (waitBehavior != null) {
            reqArgs.put("waitBehavior", AspireClient.serializeValue(waitBehavior));
        }
        getClient().invokeCapability("Aspire.Hosting/waitForStart", reqArgs);
        return this;
    }

    public CSharpAppResource waitForStart(ResourceBuilderBase dependency, WaitBehavior waitBehavior) {
        return waitForStart(new IResource(dependency.getHandle(), dependency.getClient()), waitBehavior);
    }

    /** Prevents resource from starting automatically */
    public CSharpAppResource withExplicitStart() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withExplicitStart", reqArgs);
        return this;
    }

    public CSharpAppResource waitForCompletion(IResource dependency) {
        return waitForCompletion(dependency, null);
    }

    public CSharpAppResource waitForCompletion(ResourceBuilderBase dependency) {
        return waitForCompletion(new IResource(dependency.getHandle(), dependency.getClient()));
    }

    /** Waits for the dependency resource to enter the Exited or Finished state before starting the resource. */
    public CSharpAppResource waitForCompletion(IResource dependency, Double exitCode) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("dependency", AspireClient.serializeValue(dependency));
        if (exitCode != null) {
            reqArgs.put("exitCode", AspireClient.serializeValue(exitCode));
        }
        getClient().invokeCapability("Aspire.Hosting/waitForResourceCompletion", reqArgs);
        return this;
    }

    public CSharpAppResource waitForCompletion(ResourceBuilderBase dependency, Double exitCode) {
        return waitForCompletion(new IResource(dependency.getHandle(), dependency.getClient()), exitCode);
    }

    /** Adds a health check by key */
    public CSharpAppResource withHealthCheck(String key) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        getClient().invokeCapability("Aspire.Hosting/withHealthCheck", reqArgs);
        return this;
    }

    /** Adds a health check to the resource which is mapped to a specific endpoint. */
    public CSharpAppResource withHttpHealthCheck(WithHttpHealthCheckOptions options) {
        var path = options == null ? null : options.getPath();
        var statusCode = options == null ? null : options.getStatusCode();
        var endpointName = options == null ? null : options.getEndpointName();
        return withHttpHealthCheckImpl(path, statusCode, endpointName);
    }

    public CSharpAppResource withHttpHealthCheck() {
        return withHttpHealthCheck(null);
    }

    /** Adds a health check to the resource which is mapped to a specific endpoint. */
    private CSharpAppResource withHttpHealthCheckImpl(String path, Double statusCode, String endpointName) {
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

    public CSharpAppResource withCommand(String name, String displayName, AspireFunc1<ExecuteCommandContext, ExecuteCommandResult> executeCommand) {
        return withCommand(name, displayName, executeCommand, null);
    }

    /** Adds a resource command */
    public CSharpAppResource withCommand(String name, String displayName, AspireFunc1<ExecuteCommandContext, ExecuteCommandResult> executeCommand, CommandOptions commandOptions) {
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
    public CSharpAppResource withProcessCommand(String commandName, String displayName, ProcessCommandExportOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("commandName", AspireClient.serializeValue(commandName));
        reqArgs.put("displayName", AspireClient.serializeValue(displayName));
        reqArgs.put("options", AspireClient.serializeValue(options));
        getClient().invokeCapability("Aspire.Hosting/withProcessCommand", reqArgs);
        return this;
    }

    public CSharpAppResource withProcessCommandFactory(String commandName, String displayName, AspireFunc1<ExecuteCommandContext, ProcessCommandSpecExportData> createProcessSpec) {
        return withProcessCommandFactory(commandName, displayName, createProcessSpec, null);
    }

    /** Adds a command to the resource that starts a local process created by a callback when invoked. */
    public CSharpAppResource withProcessCommandFactory(String commandName, String displayName, AspireFunc1<ExecuteCommandContext, ProcessCommandSpecExportData> createProcessSpec, ProcessCommandResultExportOptions options) {
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

    public CSharpAppResource withHttpCommand(String path, String displayName) {
        return withHttpCommand(path, displayName, null);
    }

    /** Adds an HTTP resource command */
    public CSharpAppResource withHttpCommand(String path, String displayName, HttpCommandExportOptions options) {
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
    public CSharpAppResource withDeveloperCertificateTrust(boolean trust) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("trust", AspireClient.serializeValue(trust));
        getClient().invokeCapability("Aspire.Hosting/withDeveloperCertificateTrust", reqArgs);
        return this;
    }

    /** Sets the certificate trust scope */
    public CSharpAppResource withCertificateTrustScope(CertificateTrustScope scope) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("scope", AspireClient.serializeValue(scope));
        getClient().invokeCapability("Aspire.Hosting/withCertificateTrustScope", reqArgs);
        return this;
    }

    public CSharpAppResource withHttpsDeveloperCertificate() {
        return withHttpsDeveloperCertificate(null);
    }

    /** Indicates that a resource should use the developer certificate key pair for HTTPS endpoints at run time. Currently this indicates use of the ASP.NET Core developer certificate. The developer certificate will only be used when running in local development scenarios; in publish mode resources will use their default certificate configuration. */
    public CSharpAppResource withHttpsDeveloperCertificate(ParameterResource password) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        if (password != null) {
            reqArgs.put("password", AspireClient.serializeValue(password));
        }
        getClient().invokeCapability("Aspire.Hosting/withParameterHttpsDeveloperCertificate", reqArgs);
        return this;
    }

    /** Disable HTTPS/TLS server certificate configuration for the resource. No HTTPS/TLS termination configuration will be applied. */
    public CSharpAppResource withoutHttpsCertificate() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withoutHttpsCertificate", reqArgs);
        return this;
    }

    /** Adds a relationship to another resource using its builder. */
    public CSharpAppResource withRelationship(IResource resourceBuilder, String type) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resourceBuilder", AspireClient.serializeValue(resourceBuilder));
        reqArgs.put("type", AspireClient.serializeValue(type));
        getClient().invokeCapability("Aspire.Hosting/withBuilderRelationship", reqArgs);
        return this;
    }

    public CSharpAppResource withRelationship(ResourceBuilderBase resourceBuilder, String type) {
        return withRelationship(new IResource(resourceBuilder.getHandle(), resourceBuilder.getClient()), type);
    }

    /** Sets the parent relationship */
    public CSharpAppResource withParentRelationship(IResource parent) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("parent", AspireClient.serializeValue(parent));
        getClient().invokeCapability("Aspire.Hosting/withBuilderParentRelationship", reqArgs);
        return this;
    }

    public CSharpAppResource withParentRelationship(ResourceBuilderBase parent) {
        return withParentRelationship(new IResource(parent.getHandle(), parent.getClient()));
    }

    /** Sets a child relationship */
    public CSharpAppResource withChildRelationship(IResource child) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("child", AspireClient.serializeValue(child));
        getClient().invokeCapability("Aspire.Hosting/withBuilderChildRelationship", reqArgs);
        return this;
    }

    public CSharpAppResource withChildRelationship(ResourceBuilderBase child) {
        return withChildRelationship(new IResource(child.getHandle(), child.getClient()));
    }

    public CSharpAppResource withIconName(String iconName) {
        return withIconName(iconName, null);
    }

    /** Specifies the icon to use when displaying the resource in the dashboard. */
    public CSharpAppResource withIconName(String iconName, IconVariant iconVariant) {
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
    public CSharpAppResource withComputeEnvironment(IComputeEnvironmentResource computeEnvironmentResource) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("computeEnvironmentResource", AspireClient.serializeValue(computeEnvironmentResource));
        getClient().invokeCapability("Aspire.Hosting/withComputeEnvironment", reqArgs);
        return this;
    }

    public CSharpAppResource withComputeEnvironment(ResourceBuilderBase computeEnvironmentResource) {
        return withComputeEnvironment(new IComputeEnvironmentResource(computeEnvironmentResource.getHandle(), computeEnvironmentResource.getClient()));
    }

    /** Adds an HTTP health probe to the resource */
    public CSharpAppResource withHttpProbe(ProbeType probeType, WithHttpProbeOptions options) {
        var path = options == null ? null : options.getPath();
        var initialDelaySeconds = options == null ? null : options.getInitialDelaySeconds();
        var periodSeconds = options == null ? null : options.getPeriodSeconds();
        var timeoutSeconds = options == null ? null : options.getTimeoutSeconds();
        var failureThreshold = options == null ? null : options.getFailureThreshold();
        var successThreshold = options == null ? null : options.getSuccessThreshold();
        var endpointName = options == null ? null : options.getEndpointName();
        return withHttpProbeImpl(probeType, path, initialDelaySeconds, periodSeconds, timeoutSeconds, failureThreshold, successThreshold, endpointName);
    }

    public CSharpAppResource withHttpProbe(ProbeType probeType) {
        return withHttpProbe(probeType, null);
    }

    /** Adds an HTTP health probe to the resource */
    private CSharpAppResource withHttpProbeImpl(ProbeType probeType, String path, Double initialDelaySeconds, Double periodSeconds, Double timeoutSeconds, Double failureThreshold, Double successThreshold, String endpointName) {
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
    public CSharpAppResource excludeFromMcp() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/excludeFromMcp", reqArgs);
        return this;
    }

    /** Hides the resource from default resource lists */
    public CSharpAppResource withHidden() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        getClient().invokeCapability("Aspire.Hosting/withHidden", reqArgs);
        return this;
    }

    /** Hides the resource from default resource lists after successful completion */
    public CSharpAppResource withHiddenOnCompletion(WithHiddenOnCompletionOptions options) {
        var exitCode = options == null ? null : options.getExitCode();
        var exitCodes = options == null ? null : options.getExitCodes();
        return withHiddenOnCompletionImpl(exitCode, exitCodes);
    }

    public CSharpAppResource withHiddenOnCompletion() {
        return withHiddenOnCompletion(null);
    }

    /** Hides the resource from default resource lists after successful completion */
    private CSharpAppResource withHiddenOnCompletionImpl(Double exitCode, double[] exitCodes) {
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
    public CSharpAppResource withImagePushOptions(AspireAction1<ContainerImagePushOptionsCallbackContext> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ContainerImagePushOptionsCallbackContext) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/withImagePushOptions", reqArgs);
        return this;
    }

    /** Sets the remote image name (without registry endpoint or tag) for container push operations. */
    public CSharpAppResource withRemoteImageName(String remoteImageName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("remoteImageName", AspireClient.serializeValue(remoteImageName));
        getClient().invokeCapability("Aspire.Hosting/withRemoteImageName", reqArgs);
        return this;
    }

    /** Sets the remote image tag for container push operations. */
    public CSharpAppResource withRemoteImageTag(String remoteImageTag) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("remoteImageTag", AspireClient.serializeValue(remoteImageTag));
        getClient().invokeCapability("Aspire.Hosting/withRemoteImageTag", reqArgs);
        return this;
    }

    /** Adds a pipeline step to the resource that will be executed during deployment. */
    public CSharpAppResource withPipelineStepFactory(String stepName, AspireAction1<PipelineStepContext> callback, WithPipelineStepFactoryOptions options) {
        var dependsOn = options == null ? null : options.getDependsOn();
        var requiredBy = options == null ? null : options.getRequiredBy();
        var tags = options == null ? null : options.getTags();
        var description = options == null ? null : options.getDescription();
        return withPipelineStepFactoryImpl(stepName, callback, dependsOn, requiredBy, tags, description);
    }

    public CSharpAppResource withPipelineStepFactory(String stepName, AspireAction1<PipelineStepContext> callback) {
        return withPipelineStepFactory(stepName, callback, null);
    }

    /** Adds a pipeline step to the resource that will be executed during deployment. */
    private CSharpAppResource withPipelineStepFactoryImpl(String stepName, AspireAction1<PipelineStepContext> callback, String[] dependsOn, String[] requiredBy, String[] tags, String description) {
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
    public CSharpAppResource withPipelineConfiguration(AspireAction1<PipelineConfigurationContext> callback) {
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
    public CSharpAppResource onBeforeResourceStarted(AspireAction1<BeforeResourceStartedEvent> callback) {
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
    public CSharpAppResource onResourceStopped(AspireAction1<ResourceStoppedEvent> callback) {
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
    public CSharpAppResource onInitializeResource(AspireAction1<InitializeResourceEvent> callback) {
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

    /** Subscribes to the ResourceEndpointsAllocated event. */
    public CSharpAppResource onResourceEndpointsAllocated(AspireAction1<ResourceEndpointsAllocatedEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (ResourceEndpointsAllocatedEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        getClient().invokeCapability("Aspire.Hosting/onResourceEndpointsAllocated", reqArgs);
        return this;
    }

    /** Subscribes to the ResourceReady event. */
    public CSharpAppResource onResourceReady(AspireAction1<ResourceReadyEvent> callback) {
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
