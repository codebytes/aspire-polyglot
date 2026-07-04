// DockerfileStage.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.Docker.DockerfileStage. */
public class DockerfileStage extends HandleWrapperBase {
    DockerfileStage(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public DockerfileStage arg(String name) {
        return arg(name, null);
    }

    /** Adds an ARG statement to a Dockerfile stage */
    public DockerfileStage arg(String name, String defaultValue) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (defaultValue != null) {
            reqArgs.put("defaultValue", AspireClient.serializeValue(defaultValue));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/dockerfileStageArg", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds a WORKDIR statement to a Dockerfile stage */
    public DockerfileStage workDir(String path) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("path", AspireClient.serializeValue(path));
        var result = getClient().invokeCapability("Aspire.Hosting/workDir", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds a RUN statement to a Dockerfile stage */
    public DockerfileStage run(String command) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("command", AspireClient.serializeValue(command));
        var result = getClient().invokeCapability("Aspire.Hosting/dockerfileStageRun", reqArgs);
        return (DockerfileStage) result;
    }

    public DockerfileStage copy(String source, String destination) {
        return copy(source, destination, null);
    }

    /** Adds a COPY statement to a Dockerfile stage */
    public DockerfileStage copy(String source, String destination, String chown) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("source", AspireClient.serializeValue(source));
        reqArgs.put("destination", AspireClient.serializeValue(destination));
        if (chown != null) {
            reqArgs.put("chown", AspireClient.serializeValue(chown));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/dockerfileStageCopy", reqArgs);
        return (DockerfileStage) result;
    }

    public DockerfileStage copyFrom(String from, String source, String destination) {
        return copyFrom(from, source, destination, null);
    }

    /** Adds a COPY --from statement to a Dockerfile stage */
    public DockerfileStage copyFrom(String from, String source, String destination, String chown) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("from", AspireClient.serializeValue(from));
        reqArgs.put("source", AspireClient.serializeValue(source));
        reqArgs.put("destination", AspireClient.serializeValue(destination));
        if (chown != null) {
            reqArgs.put("chown", AspireClient.serializeValue(chown));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/dockerfileStageCopyFrom", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds an ENV statement to a Dockerfile stage */
    public DockerfileStage env(String name, String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting/env", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds an EXPOSE statement to a Dockerfile stage */
    public DockerfileStage expose(double port) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("port", AspireClient.serializeValue(port));
        var result = getClient().invokeCapability("Aspire.Hosting/expose", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds a CMD statement to a Dockerfile stage */
    public DockerfileStage cmd(String[] command) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("command", AspireClient.serializeValue(command));
        var result = getClient().invokeCapability("Aspire.Hosting/cmd", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds an ENTRYPOINT statement to a Dockerfile stage */
    public DockerfileStage entrypoint(String[] command) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("command", AspireClient.serializeValue(command));
        var result = getClient().invokeCapability("Aspire.Hosting/entrypoint", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds a RUN statement with mounts to a Dockerfile stage */
    public DockerfileStage runWithMounts(String command, String[] mounts) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("command", AspireClient.serializeValue(command));
        reqArgs.put("mounts", AspireClient.serializeValue(mounts));
        var result = getClient().invokeCapability("Aspire.Hosting/runWithMounts", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds a USER statement to a Dockerfile stage */
    public DockerfileStage user(String user) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("user", AspireClient.serializeValue(user));
        var result = getClient().invokeCapability("Aspire.Hosting/user", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds an empty line to a Dockerfile stage */
    public DockerfileStage emptyLine() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/emptyLine", reqArgs);
        return (DockerfileStage) result;
    }

    /** Adds a comment to a Dockerfile stage */
    public DockerfileStage comment(String comment) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("comment", AspireClient.serializeValue(comment));
        var result = getClient().invokeCapability("Aspire.Hosting/comment", reqArgs);
        return (DockerfileStage) result;
    }

    public DockerfileStage addContainerFiles(IResource resource, String rootDestinationPath) {
        return addContainerFiles(resource, rootDestinationPath, null);
    }

    public DockerfileStage addContainerFiles(ResourceBuilderBase resource, String rootDestinationPath) {
        return addContainerFiles(new IResource(resource.getHandle(), resource.getClient()), rootDestinationPath);
    }

    /** Adds COPY --from statements for published container files */
    public DockerfileStage addContainerFiles(IResource resource, String rootDestinationPath, ILogger logger) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("stage", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resource", AspireClient.serializeValue(resource));
        reqArgs.put("rootDestinationPath", AspireClient.serializeValue(rootDestinationPath));
        if (logger != null) {
            reqArgs.put("logger", AspireClient.serializeValue(logger));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/dockerfileStageAddContainerFiles", reqArgs);
        return (DockerfileStage) result;
    }

    public DockerfileStage addContainerFiles(ResourceBuilderBase resource, String rootDestinationPath, ILogger logger) {
        return addContainerFiles(new IResource(resource.getHandle(), resource.getClient()), rootDestinationPath, logger);
    }

    public DockerfileStage addContainerFiles(IResource resource, String rootDestinationPath, HandleWrapperBase logger) {
        return addContainerFiles(resource, rootDestinationPath, new ILogger(logger.getHandle(), logger.getClient()));
    }

    public DockerfileStage addContainerFiles(ResourceBuilderBase resource, String rootDestinationPath, HandleWrapperBase logger) {
        return addContainerFiles(new IResource(resource.getHandle(), resource.getClient()), rootDestinationPath, new ILogger(logger.getHandle(), logger.getClient()));
    }

}
