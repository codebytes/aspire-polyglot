// ResourceCommandService.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceCommandService. */
public class ResourceCommandService extends HandleWrapperBase {
    ResourceCommandService(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public ExecuteCommandResult executeCommandAsync(String resource, String commandName, ExecuteCommandAsyncOptions options) {
        return executeCommandAsync(AspireUnion.of(resource), commandName, options);
    }

    public ExecuteCommandResult executeCommandAsync(String resource, String commandName) {
        return executeCommandAsync(AspireUnion.of(resource), commandName);
    }

    public ExecuteCommandResult executeCommandAsync(IResource resource, String commandName, ExecuteCommandAsyncOptions options) {
        return executeCommandAsync(AspireUnion.of(resource), commandName, options);
    }

    public ExecuteCommandResult executeCommandAsync(ResourceBuilderBase resource, String commandName, ExecuteCommandAsyncOptions options) {
        return executeCommandAsync(new IResource(resource.getHandle(), resource.getClient()), commandName, options);
    }

    public ExecuteCommandResult executeCommandAsync(IResource resource, String commandName) {
        return executeCommandAsync(AspireUnion.of(resource), commandName);
    }

    /** Executes a command for the specified resource. */
    public ExecuteCommandResult executeCommandAsync(AspireUnion resource, String commandName, ExecuteCommandAsyncOptions options) {
        var arguments = options == null ? null : options.getArguments();
        var cancellationToken = options == null ? null : options.getCancellationToken();
        return executeCommandAsyncImpl(resource, commandName, arguments, cancellationToken);
    }

    public ExecuteCommandResult executeCommandAsync(AspireUnion resource, String commandName) {
        return executeCommandAsync(resource, commandName, null);
    }

    /** Executes a command for the specified resource. */
    private ExecuteCommandResult executeCommandAsyncImpl(AspireUnion resource, String commandName, Map<String, String> arguments, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("resourceCommandService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("resource", AspireClient.serializeValue(resource));
        reqArgs.put("commandName", AspireClient.serializeValue(commandName));
        if (arguments != null) {
            reqArgs.put("arguments", AspireClient.serializeValue(arguments));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/executeResourceCommand", reqArgs);
        return ExecuteCommandResult.fromMap((Map<String, Object>) result);
    }

}
