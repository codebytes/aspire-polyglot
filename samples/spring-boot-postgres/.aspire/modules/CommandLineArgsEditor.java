// CommandLineArgsEditor.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.CommandLineArgsEditor. */
public class CommandLineArgsEditor extends HandleWrapperBase {
    CommandLineArgsEditor(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public void add(String value) {
        add(AspireUnion.of(value));
    }

    public void add(ReferenceExpression value) {
        add(AspireUnion.of(value));
    }

    public void add(EndpointReference value) {
        add(AspireUnion.of(value));
    }

    public void add(ParameterResource value) {
        add(AspireUnion.of(value));
    }

    public void add(IResourceWithConnectionString value) {
        add(AspireUnion.of(value));
    }

    public void add(ResourceBuilderBase value) {
        add(new IResourceWithConnectionString(value.getHandle(), value.getClient()));
    }

    public void add(IExpressionValue value) {
        add(AspireUnion.of(value));
    }

    public void add(HandleWrapperBase value) {
        add(new IExpressionValue(value.getHandle(), value.getClient()));
    }

    /** Adds a command-line argument. */
    public void add(AspireUnion value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/add", reqArgs);
    }

}
