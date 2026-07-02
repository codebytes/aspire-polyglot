// CommandResultData.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CommandResultData DTO. */
public class CommandResultData implements JsonSerializable {
    private String value;
    private CommandResultFormat format;
    private Boolean displayImmediately;

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public CommandResultFormat getFormat() { return format; }
    public void setFormat(CommandResultFormat value) { this.format = value; }
    public Boolean getDisplayImmediately() { return displayImmediately; }
    public void setDisplayImmediately(Boolean value) { this.displayImmediately = value; }

    @SuppressWarnings("unchecked")
    public static CommandResultData fromMap(Map<String, Object> map) {
        var value = new CommandResultData();
        var valueValue = map.get("Value");
        value.setValue((String) valueValue);
        var formatValue = map.get("Format");
        value.setFormat(formatValue == null ? null : CommandResultFormat.fromValue((String) formatValue));
        var displayImmediatelyValue = map.get("DisplayImmediately");
        value.setDisplayImmediately(displayImmediatelyValue == null ? null : (Boolean) displayImmediatelyValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Value", AspireClient.serializeValue(value));
        map.put("Format", AspireClient.serializeValue(format));
        map.put("DisplayImmediately", AspireClient.serializeValue(displayImmediately));
        return map;
    }
}
