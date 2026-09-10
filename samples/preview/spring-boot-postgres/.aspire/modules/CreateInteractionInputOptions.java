// CreateInteractionInputOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CreateInteractionInputOptions DTO. */
public class CreateInteractionInputOptions implements JsonSerializable {
    private String label;
    private String description;
    private Boolean enableDescriptionMarkdown;
    private Boolean required;
    private String placeholder;
    private String value;
    private Boolean allowCustomChoice;
    private Boolean disabled;
    private Double maxLength;
    private Double maxFileSize;
    private Boolean allowMultipleFiles;
    private String fileFilter;

    public String getLabel() { return label; }
    public void setLabel(String value) { this.label = value; }
    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
    public Boolean getEnableDescriptionMarkdown() { return enableDescriptionMarkdown; }
    public void setEnableDescriptionMarkdown(Boolean value) { this.enableDescriptionMarkdown = value; }
    public Boolean getRequired() { return required; }
    public void setRequired(Boolean value) { this.required = value; }
    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String value) { this.placeholder = value; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public Boolean getAllowCustomChoice() { return allowCustomChoice; }
    public void setAllowCustomChoice(Boolean value) { this.allowCustomChoice = value; }
    public Boolean getDisabled() { return disabled; }
    public void setDisabled(Boolean value) { this.disabled = value; }
    public Double getMaxLength() { return maxLength; }
    public void setMaxLength(Double value) { this.maxLength = value; }
    public Double getMaxFileSize() { return maxFileSize; }
    public void setMaxFileSize(Double value) { this.maxFileSize = value; }
    public Boolean getAllowMultipleFiles() { return allowMultipleFiles; }
    public void setAllowMultipleFiles(Boolean value) { this.allowMultipleFiles = value; }
    public String getFileFilter() { return fileFilter; }
    public void setFileFilter(String value) { this.fileFilter = value; }

    @SuppressWarnings("unchecked")
    public static CreateInteractionInputOptions fromMap(Map<String, Object> map) {
        var value = new CreateInteractionInputOptions();
        var labelValue = map.get("Label");
        value.setLabel(labelValue == null ? null : (String) labelValue);
        var descriptionValue = map.get("Description");
        value.setDescription(descriptionValue == null ? null : (String) descriptionValue);
        var enableDescriptionMarkdownValue = map.get("EnableDescriptionMarkdown");
        value.setEnableDescriptionMarkdown(enableDescriptionMarkdownValue == null ? null : (Boolean) enableDescriptionMarkdownValue);
        var requiredValue = map.get("Required");
        value.setRequired(requiredValue == null ? null : (Boolean) requiredValue);
        var placeholderValue = map.get("Placeholder");
        value.setPlaceholder(placeholderValue == null ? null : (String) placeholderValue);
        var valueValue = map.get("Value");
        value.setValue(valueValue == null ? null : (String) valueValue);
        var allowCustomChoiceValue = map.get("AllowCustomChoice");
        value.setAllowCustomChoice(allowCustomChoiceValue == null ? null : (Boolean) allowCustomChoiceValue);
        var disabledValue = map.get("Disabled");
        value.setDisabled(disabledValue == null ? null : (Boolean) disabledValue);
        var maxLengthValue = map.get("MaxLength");
        value.setMaxLength(maxLengthValue == null ? null : ((Number) maxLengthValue).doubleValue());
        var maxFileSizeValue = map.get("MaxFileSize");
        value.setMaxFileSize(maxFileSizeValue == null ? null : ((Number) maxFileSizeValue).doubleValue());
        var allowMultipleFilesValue = map.get("AllowMultipleFiles");
        value.setAllowMultipleFiles(allowMultipleFilesValue == null ? null : (Boolean) allowMultipleFilesValue);
        var fileFilterValue = map.get("FileFilter");
        value.setFileFilter(fileFilterValue == null ? null : (String) fileFilterValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Label", AspireClient.serializeValue(label));
        map.put("Description", AspireClient.serializeValue(description));
        map.put("EnableDescriptionMarkdown", AspireClient.serializeValue(enableDescriptionMarkdown));
        map.put("Required", AspireClient.serializeValue(required));
        map.put("Placeholder", AspireClient.serializeValue(placeholder));
        map.put("Value", AspireClient.serializeValue(value));
        map.put("AllowCustomChoice", AspireClient.serializeValue(allowCustomChoice));
        map.put("Disabled", AspireClient.serializeValue(disabled));
        map.put("MaxLength", AspireClient.serializeValue(maxLength));
        map.put("MaxFileSize", AspireClient.serializeValue(maxFileSize));
        map.put("AllowMultipleFiles", AspireClient.serializeValue(allowMultipleFiles));
        map.put("FileFilter", AspireClient.serializeValue(fileFilter));
        return map;
    }
}
