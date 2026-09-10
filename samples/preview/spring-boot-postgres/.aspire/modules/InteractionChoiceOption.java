// InteractionChoiceOption.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** InteractionChoiceOption DTO. */
public class InteractionChoiceOption implements JsonSerializable {
    private String value;
    private String label;

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getLabel() { return label; }
    public void setLabel(String value) { this.label = value; }

    @SuppressWarnings("unchecked")
    public static InteractionChoiceOption fromMap(Map<String, Object> map) {
        var value = new InteractionChoiceOption();
        var valueValue = map.get("Value");
        value.setValue((String) valueValue);
        var labelValue = map.get("Label");
        value.setLabel((String) labelValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Value", AspireClient.serializeValue(value));
        map.put("Label", AspireClient.serializeValue(label));
        return map;
    }
}
