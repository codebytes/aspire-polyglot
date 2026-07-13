// InteractionInput.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** InteractionInput DTO. */
public class InteractionInput implements JsonSerializable {
    private String name;
    private String label;
    private String description;
    private Boolean enableDescriptionMarkdown;
    private InputType inputType;
    private Boolean required;
    private Object[] options;
    private Object dynamicLoading;
    private String value;
    private String placeholder;
    private Boolean allowCustomChoice;
    private boolean disabled;
    private Double maxLength;

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    public String getLabel() { return label; }
    public void setLabel(String value) { this.label = value; }
    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
    public Boolean getEnableDescriptionMarkdown() { return enableDescriptionMarkdown; }
    public void setEnableDescriptionMarkdown(Boolean value) { this.enableDescriptionMarkdown = value; }
    public InputType getInputType() { return inputType; }
    public void setInputType(InputType value) { this.inputType = value; }
    public Boolean getRequired() { return required; }
    public void setRequired(Boolean value) { this.required = value; }
    public Object[] getOptions() { return options; }
    public void setOptions(Object[] value) { this.options = value; }
    public Object getDynamicLoading() { return dynamicLoading; }
    public void setDynamicLoading(Object value) { this.dynamicLoading = value; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String value) { this.placeholder = value; }
    public Boolean getAllowCustomChoice() { return allowCustomChoice; }
    public void setAllowCustomChoice(Boolean value) { this.allowCustomChoice = value; }
    public boolean getDisabled() { return disabled; }
    public void setDisabled(boolean value) { this.disabled = value; }
    public Double getMaxLength() { return maxLength; }
    public void setMaxLength(Double value) { this.maxLength = value; }

    @SuppressWarnings("unchecked")
    public static InteractionInput fromMap(Map<String, Object> map) {
        var value = new InteractionInput();
        var nameValue = map.get("Name");
        value.setName((String) nameValue);
        var labelValue = map.get("Label");
        value.setLabel(labelValue == null ? null : (String) labelValue);
        var descriptionValue = map.get("Description");
        value.setDescription(descriptionValue == null ? null : (String) descriptionValue);
        var enableDescriptionMarkdownValue = map.get("EnableDescriptionMarkdown");
        value.setEnableDescriptionMarkdown(enableDescriptionMarkdownValue == null ? null : (Boolean) enableDescriptionMarkdownValue);
        var inputTypeValue = map.get("InputType");
        value.setInputType(InputType.fromValue((String) inputTypeValue));
        var requiredValue = map.get("Required");
        value.setRequired(requiredValue == null ? null : (Boolean) requiredValue);
        var optionsValue = map.get("Options");
        value.setOptions((Object[]) optionsValue);
        var dynamicLoadingValue = map.get("DynamicLoading");
        value.setDynamicLoading(dynamicLoadingValue);
        var valueValue = map.get("Value");
        value.setValue(valueValue == null ? null : (String) valueValue);
        var placeholderValue = map.get("Placeholder");
        value.setPlaceholder(placeholderValue == null ? null : (String) placeholderValue);
        var allowCustomChoiceValue = map.get("AllowCustomChoice");
        value.setAllowCustomChoice(allowCustomChoiceValue == null ? null : (Boolean) allowCustomChoiceValue);
        var disabledValue = map.get("Disabled");
        value.setDisabled((Boolean) disabledValue);
        var maxLengthValue = map.get("MaxLength");
        value.setMaxLength(maxLengthValue == null ? null : ((Number) maxLengthValue).doubleValue());
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", AspireClient.serializeValue(name));
        map.put("Label", AspireClient.serializeValue(label));
        map.put("Description", AspireClient.serializeValue(description));
        map.put("EnableDescriptionMarkdown", AspireClient.serializeValue(enableDescriptionMarkdown));
        map.put("InputType", AspireClient.serializeValue(inputType));
        map.put("Required", AspireClient.serializeValue(required));
        map.put("Options", AspireClient.serializeValue(options));
        map.put("DynamicLoading", AspireClient.serializeValue(dynamicLoading));
        map.put("Value", AspireClient.serializeValue(value));
        map.put("Placeholder", AspireClient.serializeValue(placeholder));
        map.put("AllowCustomChoice", AspireClient.serializeValue(allowCustomChoice));
        map.put("Disabled", AspireClient.serializeValue(disabled));
        map.put("MaxLength", AspireClient.serializeValue(maxLength));
        return map;
    }
}
