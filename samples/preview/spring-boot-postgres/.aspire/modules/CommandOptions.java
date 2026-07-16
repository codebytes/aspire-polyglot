// CommandOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CommandOptions DTO. */
public class CommandOptions implements JsonSerializable {
    private String description;
    private Object parameter;
    private InteractionInput[] arguments;
    private Object validateArguments;
    private ResourceCommandVisibility visibility;
    private String confirmationMessage;
    private String iconName;
    private IconVariant iconVariant;
    private boolean isHighlighted;
    private Object updateState;

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
    public Object getParameter() { return parameter; }
    public void setParameter(Object value) { this.parameter = value; }
    public InteractionInput[] getArguments() { return arguments; }
    public void setArguments(InteractionInput[] value) { this.arguments = value; }
    public Object getValidateArguments() { return validateArguments; }
    public void setValidateArguments(Object value) { this.validateArguments = value; }
    public ResourceCommandVisibility getVisibility() { return visibility; }
    public void setVisibility(ResourceCommandVisibility value) { this.visibility = value; }
    public String getConfirmationMessage() { return confirmationMessage; }
    public void setConfirmationMessage(String value) { this.confirmationMessage = value; }
    public String getIconName() { return iconName; }
    public void setIconName(String value) { this.iconName = value; }
    public IconVariant getIconVariant() { return iconVariant; }
    public void setIconVariant(IconVariant value) { this.iconVariant = value; }
    public boolean getIsHighlighted() { return isHighlighted; }
    public void setIsHighlighted(boolean value) { this.isHighlighted = value; }
    public Object getUpdateState() { return updateState; }
    public void setUpdateState(Object value) { this.updateState = value; }

    @SuppressWarnings("unchecked")
    public static CommandOptions fromMap(Map<String, Object> map) {
        var value = new CommandOptions();
        var descriptionValue = map.get("Description");
        value.setDescription(descriptionValue == null ? null : (String) descriptionValue);
        var parameterValue = map.get("Parameter");
        value.setParameter(parameterValue);
        var argumentsValue = map.get("Arguments");
        value.setArguments((InteractionInput[]) argumentsValue);
        var validateArgumentsValue = map.get("ValidateArguments");
        value.setValidateArguments(validateArgumentsValue);
        var visibilityValue = map.get("Visibility");
        value.setVisibility(ResourceCommandVisibility.fromValue((String) visibilityValue));
        var confirmationMessageValue = map.get("ConfirmationMessage");
        value.setConfirmationMessage(confirmationMessageValue == null ? null : (String) confirmationMessageValue);
        var iconNameValue = map.get("IconName");
        value.setIconName(iconNameValue == null ? null : (String) iconNameValue);
        var iconVariantValue = map.get("IconVariant");
        value.setIconVariant(iconVariantValue == null ? null : IconVariant.fromValue((String) iconVariantValue));
        var isHighlightedValue = map.get("IsHighlighted");
        value.setIsHighlighted((Boolean) isHighlightedValue);
        var updateStateValue = map.get("UpdateState");
        value.setUpdateState(updateStateValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Description", AspireClient.serializeValue(description));
        map.put("Parameter", AspireClient.serializeValue(parameter));
        map.put("Arguments", AspireClient.serializeValue(arguments));
        map.put("ValidateArguments", AspireClient.serializeValue(validateArguments));
        map.put("Visibility", AspireClient.serializeValue(visibility));
        map.put("ConfirmationMessage", AspireClient.serializeValue(confirmationMessage));
        map.put("IconName", AspireClient.serializeValue(iconName));
        map.put("IconVariant", AspireClient.serializeValue(iconVariant));
        map.put("IsHighlighted", AspireClient.serializeValue(isHighlighted));
        map.put("UpdateState", AspireClient.serializeValue(updateState));
        return map;
    }
}
