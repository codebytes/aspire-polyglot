// ContainerImageReference.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerImageReference. */
public class ContainerImageReference extends HandleWrapperBase {
    ContainerImageReference(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the resource that this container image is associated with. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImageReference.resource", reqArgs);
        return (IResource) result;
    }

    /** Gets the ValueExpression property */
    public String valueExpression() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerImageReference.valueExpression", reqArgs);
        return (String) result;
    }

}
