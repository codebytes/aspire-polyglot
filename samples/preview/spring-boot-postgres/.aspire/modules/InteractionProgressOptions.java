// InteractionProgressOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** InteractionProgressOptions DTO. */
public class InteractionProgressOptions implements JsonSerializable {
    private String title;
    private String primaryButtonText;
    private Boolean enableMessageMarkdown;
    private AspireAction1<ProgressContext> work;

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }
    public String getPrimaryButtonText() { return primaryButtonText; }
    public void setPrimaryButtonText(String value) { this.primaryButtonText = value; }
    public Boolean getEnableMessageMarkdown() { return enableMessageMarkdown; }
    public void setEnableMessageMarkdown(Boolean value) { this.enableMessageMarkdown = value; }
    public AspireAction1<ProgressContext> getWork() { return work; }
    public void setWork(AspireAction1<ProgressContext> value) { this.work = value; }

    @SuppressWarnings("unchecked")
    public static InteractionProgressOptions fromMap(Map<String, Object> map) {
        var value = new InteractionProgressOptions();
        var titleValue = map.get("Title");
        value.setTitle(titleValue == null ? null : (String) titleValue);
        var primaryButtonTextValue = map.get("PrimaryButtonText");
        value.setPrimaryButtonText(primaryButtonTextValue == null ? null : (String) primaryButtonTextValue);
        var enableMessageMarkdownValue = map.get("EnableMessageMarkdown");
        value.setEnableMessageMarkdown(enableMessageMarkdownValue == null ? null : (Boolean) enableMessageMarkdownValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Title", AspireClient.serializeValue(title));
        map.put("PrimaryButtonText", AspireClient.serializeValue(primaryButtonText));
        map.put("EnableMessageMarkdown", AspireClient.serializeValue(enableMessageMarkdown));
        map.put("Work", work == null ? null : (java.util.function.Function<Object, Object>) (transportArg -> {
            var arg = (ProgressContext) transportArg;
            work.invoke(arg);
            return null;
        }));
        return map;
    }
}
