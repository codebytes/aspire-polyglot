// BoolInteractionResult.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** BoolInteractionResult DTO. */
public class BoolInteractionResult implements JsonSerializable {
    private boolean canceled;
    private Boolean value;

    public boolean getCanceled() { return canceled; }
    public void setCanceled(boolean value) { this.canceled = value; }
    public Boolean getValue() { return value; }
    public void setValue(Boolean value) { this.value = value; }

    @SuppressWarnings("unchecked")
    public static BoolInteractionResult fromMap(Map<String, Object> map) {
        var value = new BoolInteractionResult();
        var canceledValue = map.get("Canceled");
        value.setCanceled((Boolean) canceledValue);
        var valueValue = map.get("Value");
        value.setValue(valueValue == null ? null : (Boolean) valueValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Canceled", AspireClient.serializeValue(canceled));
        map.put("Value", AspireClient.serializeValue(value));
        return map;
    }
}
