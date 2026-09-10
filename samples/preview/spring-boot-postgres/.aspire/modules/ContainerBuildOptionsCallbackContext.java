// ContainerBuildOptionsCallbackContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerBuildOptionsCallbackContext. */
public class ContainerBuildOptionsCallbackContext extends HandleWrapperBase {
    ContainerBuildOptionsCallbackContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource being built. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the service provider. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** Gets the logger instance. */
    public ILogger logger() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.logger", reqArgs);
        return (ILogger) result;
    }

    /** Gets the cancellation token. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** Gets the distributed application execution context. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

    /** Gets or sets the destination for the container image. */
    public ContainerImageDestination destination() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.destination", reqArgs);
        return result == null ? null : ContainerImageDestination.fromValue((String) result);
    }

    /** Sets the Destination property */
    public ContainerBuildOptionsCallbackContext setDestination(ContainerImageDestination value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.setDestination", reqArgs);
        return (ContainerBuildOptionsCallbackContext) result;
    }

    /** Gets or sets the output path for the container archive. */
    public String outputPath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.outputPath", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Sets the OutputPath property */
    public ContainerBuildOptionsCallbackContext setOutputPath(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.setOutputPath", reqArgs);
        return (ContainerBuildOptionsCallbackContext) result;
    }

    /** Gets or sets the container image format. */
    public ContainerImageFormat imageFormat() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.imageFormat", reqArgs);
        return result == null ? null : ContainerImageFormat.fromValue((String) result);
    }

    /** Sets the ImageFormat property */
    public ContainerBuildOptionsCallbackContext setImageFormat(ContainerImageFormat value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.setImageFormat", reqArgs);
        return (ContainerBuildOptionsCallbackContext) result;
    }

    /** Gets or sets the target platform for the container. */
    public ContainerTargetPlatform targetPlatform() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.targetPlatform", reqArgs);
        return result == null ? null : ContainerTargetPlatform.fromValue((String) result);
    }

    /** Sets the TargetPlatform property */
    public ContainerBuildOptionsCallbackContext setTargetPlatform(ContainerTargetPlatform value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.setTargetPlatform", reqArgs);
        return (ContainerBuildOptionsCallbackContext) result;
    }

    /** Gets or sets the local image name for the built container. */
    public String localImageName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.localImageName", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Sets the LocalImageName property */
    public ContainerBuildOptionsCallbackContext setLocalImageName(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.setLocalImageName", reqArgs);
        return (ContainerBuildOptionsCallbackContext) result;
    }

    /** Gets or sets the local image tag for the built container. */
    public String localImageTag() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.localImageTag", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Sets the LocalImageTag property */
    public ContainerBuildOptionsCallbackContext setLocalImageTag(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerBuildOptionsCallbackContext.setLocalImageTag", reqArgs);
        return (ContainerBuildOptionsCallbackContext) result;
    }

}
