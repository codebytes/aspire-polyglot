// InteractionNotificationOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** InteractionNotificationOptions DTO. */
public class InteractionNotificationOptions implements JsonSerializable {
    private String primaryButtonText;
    private String secondaryButtonText;
    private Boolean showSecondaryButton;
    private Boolean showDismiss;
    private Boolean enableMessageMarkdown;
    private MessageIntent intent;
    private String linkText;
    private String linkUrl;

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
    public MessageIntent getIntent() { return intent; }
    public void setIntent(MessageIntent value) { this.intent = value; }
    public String getLinkText() { return linkText; }
    public void setLinkText(String value) { this.linkText = value; }
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String value) { this.linkUrl = value; }

    @SuppressWarnings("unchecked")
    public static InteractionNotificationOptions fromMap(Map<String, Object> map) {
        var value = new InteractionNotificationOptions();
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
        var intentValue = map.get("Intent");
        value.setIntent(intentValue == null ? null : MessageIntent.fromValue((String) intentValue));
        var linkTextValue = map.get("LinkText");
        value.setLinkText(linkTextValue == null ? null : (String) linkTextValue);
        var linkUrlValue = map.get("LinkUrl");
        value.setLinkUrl(linkUrlValue == null ? null : (String) linkUrlValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("PrimaryButtonText", AspireClient.serializeValue(primaryButtonText));
        map.put("SecondaryButtonText", AspireClient.serializeValue(secondaryButtonText));
        map.put("ShowSecondaryButton", AspireClient.serializeValue(showSecondaryButton));
        map.put("ShowDismiss", AspireClient.serializeValue(showDismiss));
        map.put("EnableMessageMarkdown", AspireClient.serializeValue(enableMessageMarkdown));
        map.put("Intent", AspireClient.serializeValue(intent));
        map.put("LinkText", AspireClient.serializeValue(linkText));
        map.put("LinkUrl", AspireClient.serializeValue(linkUrl));
        return map;
    }
}
