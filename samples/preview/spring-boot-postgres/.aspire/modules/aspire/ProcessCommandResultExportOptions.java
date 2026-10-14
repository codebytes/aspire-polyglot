// ProcessCommandResultExportOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;

/** ProcessCommandResultExportOptions DTO. */
@SuppressWarnings({"all", "unchecked", "serial"})
public class ProcessCommandResultExportOptions implements JsonSerializable {
    private CommandOptions commandOptions;
    private Number maxOutputLineCount;
    private Boolean displayImmediately;
    private double[] successExitCodes;

    public CommandOptions getCommandOptions() { return commandOptions; }
    public void setCommandOptions(CommandOptions value) { this.commandOptions = value; }
    public Number getMaxOutputLineCount() { return maxOutputLineCount; }
    public void setMaxOutputLineCount(Number value) { this.maxOutputLineCount = value; }
    public Boolean getDisplayImmediately() { return displayImmediately; }
    public void setDisplayImmediately(Boolean value) { this.displayImmediately = value; }
    public double[] getSuccessExitCodes() { return successExitCodes; }
    public void setSuccessExitCodes(double[] value) { this.successExitCodes = value; }

    @SuppressWarnings("unchecked")
    public static ProcessCommandResultExportOptions fromMap(Map<String, Object> map) {
        var value = new ProcessCommandResultExportOptions();
        var commandOptionsValue = map.get("CommandOptions");
        value.setCommandOptions(commandOptionsValue == null ? null : CommandOptions.fromMap((Map<String, Object>) commandOptionsValue));
        var maxOutputLineCountValue = map.get("MaxOutputLineCount");
        value.setMaxOutputLineCount(maxOutputLineCountValue == null ? null : ((Number) maxOutputLineCountValue).doubleValue());
        var displayImmediatelyValue = map.get("DisplayImmediately");
        value.setDisplayImmediately(displayImmediatelyValue == null ? null : (Boolean) displayImmediatelyValue);
        var successExitCodesValue = map.get("SuccessExitCodes");
        value.setSuccessExitCodes(successExitCodesValue == null ? null : (double[]) AspireClient.convertArray(successExitCodesValue, double[].class.getComponentType(), item0 -> ((Number) item0).doubleValue()));
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("CommandOptions", AspireClient.serializeValue(commandOptions));
        map.put("MaxOutputLineCount", AspireClient.serializeValue(maxOutputLineCount));
        map.put("DisplayImmediately", AspireClient.serializeValue(displayImmediately));
        map.put("SuccessExitCodes", AspireClient.serializeValue(successExitCodes));
        return map;
    }
}
