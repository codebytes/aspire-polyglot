// ContainerPortReference.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerPortReference. */
public class ContainerPortReference extends HandleWrapperBase {
    ContainerPortReference(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource that this container port is associated with. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerPortReference.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the ValueExpression property */
    public String valueExpression() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerPortReference.valueExpression", reqArgs);
        return (String) result;
    }

}
