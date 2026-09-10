// InteractionInputsDialogOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** InteractionInputsDialogOptions DTO. */
public class InteractionInputsDialogOptions implements JsonSerializable {
    private String primaryButtonText;
    private String secondaryButtonText;
    private Boolean showSecondaryButton;
    private Boolean showDismiss;
    private Boolean enableMessageMarkdown;
    private AspireAction1<InputsDialogValidationContext> validationCallback;

    public String getPrimaryButtonText() { return primaryButtonText; }
    public void setPrimaryButtonText(String value) { this.primaryButtonText = value; }
    public String getSecondaryButtonText() { return secondaryButtonText; }
    public void setSecondaryButtonText(String value) { this.secondaryButtonText = value; }
    public Boolean getShowSecondaryButton() { return showSecondaryButton; }
    public void setShowSecondaryButton(Boolean value) { this.showSecondaryButton = value; }
    public Boolean getShowDismiss() { return showDismiss; }
    public void setShowDismiss(Boolean value) { this.showDismiss = value; }
    public Boolean getEnableMessageMarkdown() { return enableMessageMarkdown; }
    public void setEnableMessageMarkdown(Boolean value) { this.enableMessageMarkdown = value; }
    public AspireAction1<InputsDialogValidationContext> getValidationCallback() { return validationCallback; }
    public void setValidationCallback(AspireAction1<InputsDialogValidationContext> value) { this.validationCallback = value; }

    @SuppressWarnings("unchecked")
    public static InteractionInputsDialogOptions fromMap(Map<String, Object> map) {
        var value = new InteractionInputsDialogOptions();
        var primaryButtonTextValue = map.get("PrimaryButtonText");
        value.setPrimaryButtonText(primaryButtonTextValue == null ? null : (String) primaryButtonTextValue);
        var secondaryButtonTextValue = map.get("SecondaryButtonText");
        value.setSecondaryButtonText(secondaryButtonTextValue == null ? null : (String) secondaryButtonTextValue);
        var showSecondaryButtonValue = map.get("ShowSecondaryButton");
        value.setShowSecondaryButton(showSecondaryButtonValue == null ? null : (Boolean) showSecondaryButtonValue);
        var showDismissValue = map.get("ShowDismiss");
        value.setShowDismiss(showDismissValue == null ? null : (Boolean) showDismissValue);
        var enableMessageMarkdownValue = map.get("EnableMessageMarkdown");
        value.setEnableMessageMarkdown(enableMessageMarkdownValue == null ? null : (Boolean) enableMessageMarkdownValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("PrimaryButtonText", AspireClient.serializeValue(primaryButtonText));
        map.put("SecondaryButtonText", AspireClient.serializeValue(secondaryButtonText));
        map.put("ShowSecondaryButton", AspireClient.serializeValue(showSecondaryButton));
        map.put("ShowDismiss", AspireClient.serializeValue(showDismiss));
        map.put("EnableMessageMarkdown", AspireClient.serializeValue(enableMessageMarkdown));
        map.put("ValidationCallback", validationCallback == null ? null : (java.util.function.Function<Object, Object>) (transportArg -> {
            var arg = (InputsDialogValidationContext) transportArg;
            validationCallback.invoke(arg);
            return null;
        }));
        return map;
    }
}
