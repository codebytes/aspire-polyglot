// CommandOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CommandOptions DTO. */
public class CommandOptions implements JsonSerializable {
    private String description;
    private Object parameter;
    private InteractionInput[] arguments;
    private AspireAction1<InputsDialogValidationContext> validateArguments;
    private ResourceCommandVisibility visibility;
    private String confirmationMessage;
    private String iconName;
    private IconVariant iconVariant;
    private boolean isHighlighted;
    private AspireFunc1<UpdateCommandStateContext, ResourceCommandState> updateState;
    private CommandProgressOptions progress;

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
    public Object getParameter() { return parameter; }
    public void setParameter(Object value) { this.parameter = value; }
    public InteractionInput[] getArguments() { return arguments; }
    public void setArguments(InteractionInput[] value) { this.arguments = value; }
    public AspireAction1<InputsDialogValidationContext> getValidateArguments() { return validateArguments; }
    public void setValidateArguments(AspireAction1<InputsDialogValidationContext> value) { this.validateArguments = value; }
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
    public AspireFunc1<UpdateCommandStateContext, ResourceCommandState> getUpdateState() { return updateState; }
    public void setUpdateState(AspireFunc1<UpdateCommandStateContext, ResourceCommandState> value) { this.updateState = value; }
    public CommandProgressOptions getProgress() { return progress; }
    public void setProgress(CommandProgressOptions value) { this.progress = value; }

    @SuppressWarnings("unchecked")
    public static CommandOptions fromMap(Map<String, Object> map) {
        var value = new CommandOptions();
        var descriptionValue = map.get("Description");
        value.setDescription(descriptionValue == null ? null : (String) descriptionValue);
        var parameterValue = map.get("Parameter");
        value.setParameter(parameterValue);
        var argumentsValue = map.get("Arguments");
        value.setArguments((InteractionInput[]) argumentsValue);
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
        var progressValue = map.get("Progress");
        value.setProgress(progressValue == null ? null : CommandProgressOptions.fromMap((Map<String, Object>) progressValue));
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Description", AspireClient.serializeValue(description));
        map.put("Parameter", AspireClient.serializeValue(parameter));
        map.put("Arguments", AspireClient.serializeValue(arguments));
        map.put("ValidateArguments", validateArguments == null ? null : (java.util.function.Function<Object, Object>) (transportArg -> {
            var arg = (InputsDialogValidationContext) transportArg;
            validateArguments.invoke(arg);
            return null;
        }));
        map.put("Visibility", AspireClient.serializeValue(visibility));
        map.put("ConfirmationMessage", AspireClient.serializeValue(confirmationMessage));
        map.put("IconName", AspireClient.serializeValue(iconName));
        map.put("IconVariant", AspireClient.serializeValue(iconVariant));
        map.put("IsHighlighted", AspireClient.serializeValue(isHighlighted));
        map.put("UpdateState", updateState == null ? null : (java.util.function.Function<Object, Object>) (transportArg -> {
            var arg = (UpdateCommandStateContext) transportArg;
            return AspireClient.awaitValue(updateState.invoke(arg));
        }));
        map.put("Progress", AspireClient.serializeValue(progress));
        return map;
    }
}
