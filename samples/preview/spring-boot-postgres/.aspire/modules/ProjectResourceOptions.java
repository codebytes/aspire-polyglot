// ProjectResourceOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ProjectResourceOptions. */
public class ProjectResourceOptions extends HandleWrapperBase {
    ProjectResourceOptions(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** The launch profile to use. If `null` then the default launch profile will be used. */
    public String launchProfileName() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/ProjectResourceOptions.launchProfileName", reqArgs);
        return result == null ? null : (String) result;
    }

    /** Sets the LaunchProfileName property */
    public ProjectResourceOptions setLaunchProfileName(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting/ProjectResourceOptions.setLaunchProfileName", reqArgs);
        return (ProjectResourceOptions) result;
    }

    /** If set, no launch profile will be used, and LaunchProfileName will be ignored. */
    public boolean excludeLaunchProfile() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/ProjectResourceOptions.excludeLaunchProfile", reqArgs);
        return (Boolean) result;
    }

    /** Sets the ExcludeLaunchProfile property */
    public ProjectResourceOptions setExcludeLaunchProfile(boolean value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting/ProjectResourceOptions.setExcludeLaunchProfile", reqArgs);
        return (ProjectResourceOptions) result;
    }

    /** If set, ignore endpoints coming from Kestrel configuration. */
    public boolean excludeKestrelEndpoints() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/ProjectResourceOptions.excludeKestrelEndpoints", reqArgs);
        return (Boolean) result;
    }

    /** Sets the ExcludeKestrelEndpoints property */
    public ProjectResourceOptions setExcludeKestrelEndpoints(boolean value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        var result = getClient().invokeCapability("Aspire.Hosting/ProjectResourceOptions.setExcludeKestrelEndpoints", reqArgs);
        return (ProjectResourceOptions) result;
    }

}
