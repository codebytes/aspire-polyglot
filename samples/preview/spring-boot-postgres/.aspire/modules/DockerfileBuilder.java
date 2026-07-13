// DockerfileBuilder.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.Docker.DockerfileBuilder. */
public class DockerfileBuilder extends HandleWrapperBase {
    DockerfileBuilder(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public DockerfileBuilder arg(String name) {
        return arg(name, null);
    }

    /** Adds a global ARG statement to the Dockerfile */
    public DockerfileBuilder arg(String name, String defaultValue) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (defaultValue != null) {
            reqArgs.put("defaultValue", AspireClient.serializeValue(defaultValue));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/dockerfileBuilderArg", reqArgs);
        return (DockerfileBuilder) result;
    }

    public DockerfileStage from(String image) {
        return from(image, null);
    }

    /** Adds a FROM statement to start a Dockerfile stage */
    public DockerfileStage from(String image, String stageName) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("image", AspireClient.serializeValue(image));
        if (stageName != null) {
            reqArgs.put("stageName", AspireClient.serializeValue(stageName));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/dockerfileBuilderFrom", reqArgs);
        return (DockerfileStage) result;
    }

    public DockerfileBuilder addContainerFilesStages(IResource resource) {
        return addContainerFilesStages(resource, null);
    }

    public DockerfileBuilder addContainerFilesStages(ResourceBuilderBase resource) {
        return addContainerFilesStages(new IResource(resource.getHandle(), resource.getClient()));
    }

    /** Adds Dockerfile stages for published container files */
    public DockerfileBuilder addContainerFilesStages(IResource resource, ILogger logger) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resource", AspireClient.serializeValue(resource));
        if (logger != null) {
            reqArgs.put("logger", AspireClient.serializeValue(logger));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/dockerfileBuilderAddContainerFilesStages", reqArgs);
        return (DockerfileBuilder) result;
    }

    public DockerfileBuilder addContainerFilesStages(ResourceBuilderBase resource, ILogger logger) {
        return addContainerFilesStages(new IResource(resource.getHandle(), resource.getClient()), logger);
    }

    public DockerfileBuilder addContainerFilesStages(IResource resource, HandleWrapperBase logger) {
        return addContainerFilesStages(resource, new ILogger(logger.getHandle(), logger.getClient()));
    }

    public DockerfileBuilder addContainerFilesStages(ResourceBuilderBase resource, HandleWrapperBase logger) {
        return addContainerFilesStages(new IResource(resource.getHandle(), resource.getClient()), new ILogger(logger.getHandle(), logger.getClient()));
    }

}
