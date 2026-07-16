// HttpCommandExportOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** HttpCommandExportOptions DTO. */
public class HttpCommandExportOptions implements JsonSerializable {
    private String description;
    private String confirmationMessage;
    private String iconName;
    private IconVariant iconVariant;
    private boolean isHighlighted;
    private String commandName;
    private String endpointName;
    private String methodName;
    private HttpCommandResultMode resultMode;

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
    public String getConfirmationMessage() { return confirmationMessage; }
    public void setConfirmationMessage(String value) { this.confirmationMessage = value; }
    public String getIconName() { return iconName; }
    public void setIconName(String value) { this.iconName = value; }
    public IconVariant getIconVariant() { return iconVariant; }
    public void setIconVariant(IconVariant value) { this.iconVariant = value; }
    public boolean getIsHighlighted() { return isHighlighted; }
    public void setIsHighlighted(boolean value) { this.isHighlighted = value; }
    public String getCommandName() { return commandName; }
    public void setCommandName(String value) { this.commandName = value; }
    public String getEndpointName() { return endpointName; }
    public void setEndpointName(String value) { this.endpointName = value; }
    public String getMethodName() { return methodName; }
    public void setMethodName(String value) { this.methodName = value; }
    public HttpCommandResultMode getResultMode() { return resultMode; }
    public void setResultMode(HttpCommandResultMode value) { this.resultMode = value; }

    @SuppressWarnings("unchecked")
    public static HttpCommandExportOptions fromMap(Map<String, Object> map) {
        var value = new HttpCommandExportOptions();
        var descriptionValue = map.get("Description");
        value.setDescription(descriptionValue == null ? null : (String) descriptionValue);
        var confirmationMessageValue = map.get("ConfirmationMessage");
        value.setConfirmationMessage(confirmationMessageValue == null ? null : (String) confirmationMessageValue);
        var iconNameValue = map.get("IconName");
        value.setIconName(iconNameValue == null ? null : (String) iconNameValue);
        var iconVariantValue = map.get("IconVariant");
        value.setIconVariant(iconVariantValue == null ? null : IconVariant.fromValue((String) iconVariantValue));
        var isHighlightedValue = map.get("IsHighlighted");
        value.setIsHighlighted((Boolean) isHighlightedValue);
        var commandNameValue = map.get("CommandName");
        value.setCommandName(commandNameValue == null ? null : (String) commandNameValue);
        var endpointNameValue = map.get("EndpointName");
        value.setEndpointName(endpointNameValue == null ? null : (String) endpointNameValue);
        var methodNameValue = map.get("MethodName");
        value.setMethodName(methodNameValue == null ? null : (String) methodNameValue);
        var resultModeValue = map.get("ResultMode");
        value.setResultMode(HttpCommandResultMode.fromValue((String) resultModeValue));
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Description", AspireClient.serializeValue(description));
        map.put("ConfirmationMessage", AspireClient.serializeValue(confirmationMessage));
        map.put("IconName", AspireClient.serializeValue(iconName));
        map.put("IconVariant", AspireClient.serializeValue(iconVariant));
        map.put("IsHighlighted", AspireClient.serializeValue(isHighlighted));
        map.put("CommandName", AspireClient.serializeValue(commandName));
        map.put("EndpointName", AspireClient.serializeValue(endpointName));
        map.put("MethodName", AspireClient.serializeValue(methodName));
        map.put("ResultMode", AspireClient.serializeValue(resultMode));
        return map;
    }
}
