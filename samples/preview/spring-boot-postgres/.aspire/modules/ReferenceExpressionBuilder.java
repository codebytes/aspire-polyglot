// ReferenceExpressionBuilder.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ReferenceExpressionBuilder. */
public class ReferenceExpressionBuilder extends HandleWrapperBase {
    ReferenceExpressionBuilder(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Indicates whether the expression is empty. */
    public boolean isEmpty() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ReferenceExpressionBuilder.isEmpty", reqArgs);
        return (Boolean) result;
    }

    /** Appends a literal value to the expression. */
    public void appendLiteral(String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/appendLiteral", reqArgs);
    }

    public void appendFormatted(String value) {
        appendFormatted(value, null);
    }

    /** Appends a formatted value to the expression. */
    public void appendFormatted(String value, String format) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("value", AspireClient.serializeValue(value));
        if (format != null) {
            reqArgs.put("format", AspireClient.serializeValue(format));
        }
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/appendFormatted", reqArgs);
    }

    public void appendValueProvider(Object valueProvider) {
        appendValueProvider(valueProvider, null);
    }

    /** Appends a value provider to the reference expression */
    public void appendValueProvider(Object valueProvider, String format) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("valueProvider", AspireClient.serializeValue(valueProvider));
        if (format != null) {
            reqArgs.put("format", AspireClient.serializeValue(format));
        }
        getClient().invokeCapability("Aspire.Hosting.ApplicationModel/appendValueProvider", reqArgs);
    }

    /** Builds the reference expression */
    public ReferenceExpression build() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/build", reqArgs);
        return (ReferenceExpression) result;
    }

}
