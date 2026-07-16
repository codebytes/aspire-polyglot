// EndpointReferenceExpression.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.EndpointReferenceExpression. */
public class EndpointReferenceExpression extends HandleWrapperBase {
    EndpointReferenceExpression(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the `EndpointReference`. */
    public EndpointReference endpoint() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReferenceExpression.endpoint", reqArgs);
        return (EndpointReference) result;
    }

    /** Gets the `EndpointProperty` for the property expression. */
    public EndpointProperty property() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReferenceExpression.property", reqArgs);
        return EndpointProperty.fromValue((String) result);
    }

    /** Gets the expression of the property of the endpoint. */
    public String valueExpression() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/EndpointReferenceExpression.valueExpression", reqArgs);
        return (String) result;
    }

}
