// ParameterCustomInputOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ParameterCustomInputOptions DTO. */
public class ParameterCustomInputOptions implements JsonSerializable {
    private InputType inputType;
    private String label;
    private String description;
    private Boolean enableDescriptionMarkdown;
    private Map<String, String> options;
    private String value;
    private String placeholder;
    private Boolean allowCustomChoice;
    private Boolean disabled;
    private Double maxLength;

    public InputType getInputType() { return inputType; }
    public void setInputType(InputType value) { this.inputType = value; }
    public String getLabel() { return label; }
    public void setLabel(String value) { this.label = value; }
    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
    public Boolean getEnableDescriptionMarkdown() { return enableDescriptionMarkdown; }
    public void setEnableDescriptionMarkdown(Boolean value) { this.enableDescriptionMarkdown = value; }
    public Map<String, String> getOptions() { return options; }
    public void setOptions(Map<String, String> value) { this.options = value; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String value) { this.placeholder = value; }
    public Boolean getAllowCustomChoice() { return allowCustomChoice; }
    public void setAllowCustomChoice(Boolean value) { this.allowCustomChoice = value; }
    public Boolean getDisabled() { return disabled; }
    public void setDisabled(Boolean value) { this.disabled = value; }
    public Double getMaxLength() { return maxLength; }
    public void setMaxLength(Double value) { this.maxLength = value; }

    @SuppressWarnings("unchecked")
    public static ParameterCustomInputOptions fromMap(Map<String, Object> map) {
        var value = new ParameterCustomInputOptions();
        var inputTypeValue = map.get("InputType");
        value.setInputType(inputTypeValue == null ? null : InputType.fromValue((String) inputTypeValue));
        var labelValue = map.get("Label");
        value.setLabel(labelValue == null ? null : (String) labelValue);
        var descriptionValue = map.get("Description");
        value.setDescription(descriptionValue == null ? null : (String) descriptionValue);
        var enableDescriptionMarkdownValue = map.get("EnableDescriptionMarkdown");
        value.setEnableDescriptionMarkdown(enableDescriptionMarkdownValue == null ? null : (Boolean) enableDescriptionMarkdownValue);
        var optionsValue = map.get("Options");
        value.setOptions(optionsValue == null ? null : (Map<String, String>) optionsValue);
        var valueValue = map.get("Value");
        value.setValue(valueValue == null ? null : (String) valueValue);
        var placeholderValue = map.get("Placeholder");
        value.setPlaceholder(placeholderValue == null ? null : (String) placeholderValue);
        var allowCustomChoiceValue = map.get("AllowCustomChoice");
        value.setAllowCustomChoice(allowCustomChoiceValue == null ? null : (Boolean) allowCustomChoiceValue);
        var disabledValue = map.get("Disabled");
        value.setDisabled(disabledValue == null ? null : (Boolean) disabledValue);
        var maxLengthValue = map.get("MaxLength");
        value.setMaxLength(maxLengthValue == null ? null : ((Number) maxLengthValue).doubleValue());
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("InputType", AspireClient.serializeValue(inputType));
        map.put("Label", AspireClient.serializeValue(label));
        map.put("Description", AspireClient.serializeValue(description));
        map.put("EnableDescriptionMarkdown", AspireClient.serializeValue(enableDescriptionMarkdown));
        map.put("Options", AspireClient.serializeValue(options));
        map.put("Value", AspireClient.serializeValue(value));
        map.put("Placeholder", AspireClient.serializeValue(placeholder));
        map.put("AllowCustomChoice", AspireClient.serializeValue(allowCustomChoice));
        map.put("Disabled", AspireClient.serializeValue(disabled));
        map.put("MaxLength", AspireClient.serializeValue(maxLength));
        return map;
    }
}
