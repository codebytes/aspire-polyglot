// ReferenceExpression.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;

/**
 * ReferenceExpression represents a reference expression.
 * Supports value mode (format + value providers), conditional mode, and handle mode.
 */
public class ReferenceExpression {
    private final String format;
    private final Object[] valueProviders;
    private final Object condition;
    private final ReferenceExpression whenTrue;
    private final ReferenceExpression whenFalse;
    private final String matchValue;
    private final Handle handle;
    private final AspireClient client;

    ReferenceExpression(String format, Object... valueProviders) {
        this.format = format;
        this.valueProviders = valueProviders;
        this.condition = null;
        this.whenTrue = null;
        this.whenFalse = null;
        this.matchValue = null;
        this.handle = null;
        this.client = null;
    }

    private ReferenceExpression(Object condition, String matchValue, ReferenceExpression whenTrue, ReferenceExpression whenFalse) {
        this.format = null;
        this.valueProviders = null;
        this.condition = condition;
        this.whenTrue = whenTrue;
        this.whenFalse = whenFalse;
        this.matchValue = matchValue != null ? matchValue : "True";
        this.handle = null;
        this.client = null;
    }

    ReferenceExpression(Handle handle, AspireClient client) {
        this.format = null;
        this.valueProviders = null;
        this.condition = null;
        this.whenTrue = null;
        this.whenFalse = null;
        this.matchValue = null;
        this.handle = handle;
        this.client = client;
    }

    boolean isConditional() {
        return condition != null;
    }

    boolean isHandle() {
        return handle != null;
    }

    Map<String, Object> toJson() {
        if (handle != null) {
            return handle.toJson();
        }

        Map<String, Object> expression = new HashMap<>();
        if (isConditional()) {
            expression.put("condition", extractValueProvider(condition));
            expression.put("whenTrue", whenTrue.toJson());
            expression.put("whenFalse", whenFalse.toJson());
            expression.put("matchValue", matchValue);
        } else {
            expression.put("format", format);
            if (valueProviders != null && valueProviders.length > 0) {
                List<Object> providers = new ArrayList<>(valueProviders.length);
                for (Object valueProvider : valueProviders) {
                    providers.add(extractValueProvider(valueProvider));
                }
                expression.put("valueProviders", providers);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("$expr", expression);
        return result;
    }

    public String getValue() {
        return getValue(null);
    }

    public String getValue(CancellationToken cancellationToken) {
        if (handle == null || client == null) {
            throw new IllegalStateException("getValue is only available on server-returned ReferenceExpression instances");
        }

        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(handle));
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", client.registerCancellation(cancellationToken));
        }

        return (String) client.invokeCapability("Aspire.Hosting.ApplicationModel/getValue", reqArgs);
    }

    public static ReferenceExpression refExpr(String format, Object... valueProviders) {
        return new ReferenceExpression(format, valueProviders);
    }

    public static ReferenceExpression createConditional(Object condition, String matchValue, ReferenceExpression whenTrue, ReferenceExpression whenFalse) {
        return new ReferenceExpression(condition, matchValue, whenTrue, whenFalse);
    }

    private static Object extractValueProvider(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot use null in a reference expression");
        }

        if (value instanceof String || value instanceof Number || value instanceof Boolean) {
            return value;
        }

        return AspireClient.serializeValue(value);
    }
}
