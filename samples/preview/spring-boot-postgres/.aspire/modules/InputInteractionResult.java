// InputInteractionResult.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** InputInteractionResult DTO. */
public class InputInteractionResult implements JsonSerializable {
    private boolean canceled;
    private InteractionInput input;

    public boolean getCanceled() { return canceled; }
    public void setCanceled(boolean value) { this.canceled = value; }
    public InteractionInput getInput() { return input; }
    public void setInput(InteractionInput value) { this.input = value; }

    @SuppressWarnings("unchecked")
    public static InputInteractionResult fromMap(Map<String, Object> map) {
        var value = new InputInteractionResult();
        var canceledValue = map.get("Canceled");
        value.setCanceled((Boolean) canceledValue);
        var inputValue = map.get("Input");
        value.setInput(inputValue == null ? null : InteractionInput.fromMap((Map<String, Object>) inputValue));
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Canceled", AspireClient.serializeValue(canceled));
        map.put("Input", AspireClient.serializeValue(input));
        return map;
    }
}
