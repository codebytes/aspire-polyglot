// ContainerImagePushOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerImagePushOptions. */
public class ContainerImagePushOptions extends HandleWrapperBase {
    ContainerImagePushOptions(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets or sets the remote image name (repository path without registry endpoint or tag). */
    public String remoteImageName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImagePushOptions.remoteImageName", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Sets the RemoteImageName property */
    public ContainerImagePushOptions setRemoteImageName(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImagePushOptions.setRemoteImageName", reqArgs);
        return (ContainerImagePushOptions) result;
    }

    /** Gets or sets the remote image tag. */
    public String remoteImageTag() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImagePushOptions.remoteImageTag", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Sets the RemoteImageTag property */
    public ContainerImagePushOptions setRemoteImageTag(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImagePushOptions.setRemoteImageTag", reqArgs);
        return (ContainerImagePushOptions) result;
    }

}
