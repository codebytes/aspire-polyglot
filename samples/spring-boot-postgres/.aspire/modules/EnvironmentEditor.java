// EnvironmentEditor.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.EnvironmentEditor. */
public class EnvironmentEditor extends HandleWrapperBase {
    EnvironmentEditor(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public void set(String name, String value) {
        set(name, AspireUnion.of(value));
    }

    public void set(String name, ReferenceExpression value) {
        set(name, AspireUnion.of(value));
    }

    public void set(String name, EndpointReference value) {
        set(name, AspireUnion.of(value));
    }

    public void set(String name, ParameterResource value) {
        set(name, AspireUnion.of(value));
    }

    public void set(String name, IResourceWithConnectionString value) {
        set(name, AspireUnion.of(value));
    }

    public void set(String name, ResourceBuilderBase value) {
        set(name, new IResourceWithConnectionString(value.getHandle(), value.getClient()));
    }

    public void set(String name, IExpressionValue value) {
        set(name, AspireUnion.of(value));
    }

    public void set(String name, HandleWrapperBase value) {
        set(name, new IExpressionValue(value.getHandle(), value.getClient()));
    }

    /** Sets an environment variable. */
    public void set(String name, AspireUnion value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/set", reqArgs);
    }

}
