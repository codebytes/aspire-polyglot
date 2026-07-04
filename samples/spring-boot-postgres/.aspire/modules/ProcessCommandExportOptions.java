// ProcessCommandExportOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ProcessCommandExportOptions DTO. */
public class ProcessCommandExportOptions implements JsonSerializable {
    private String executablePath;
    private String[] arguments;
    private String workingDirectory;
    private Map<String, String> environmentVariables;
    private Boolean inheritEnvironmentVariables;
    private String standardInputContent;
    private Boolean killEntireProcessTree;
    private CommandOptions commandOptions;
    private Double maxOutputLineCount;
    private Boolean displayImmediately;
    private double[] successExitCodes;

    public String getExecutablePath() { return executablePath; }
    public void setExecutablePath(String value) { this.executablePath = value; }
    public String[] getArguments() { return arguments; }
    public void setArguments(String[] value) { this.arguments = value; }
    public String getWorkingDirectory() { return workingDirectory; }
    public void setWorkingDirectory(String value) { this.workingDirectory = value; }
    public Map<String, String> getEnvironmentVariables() { return environmentVariables; }
    public void setEnvironmentVariables(Map<String, String> value) { this.environmentVariables = value; }
    public Boolean getInheritEnvironmentVariables() { return inheritEnvironmentVariables; }
    public void setInheritEnvironmentVariables(Boolean value) { this.inheritEnvironmentVariables = value; }
    public String getStandardInputContent() { return standardInputContent; }
    public void setStandardInputContent(String value) { this.standardInputContent = value; }
    public Boolean getKillEntireProcessTree() { return killEntireProcessTree; }
    public void setKillEntireProcessTree(Boolean value) { this.killEntireProcessTree = value; }
    public CommandOptions getCommandOptions() { return commandOptions; }
    public void setCommandOptions(CommandOptions value) { this.commandOptions = value; }
    public Double getMaxOutputLineCount() { return maxOutputLineCount; }
    public void setMaxOutputLineCount(Double value) { this.maxOutputLineCount = value; }
    public Boolean getDisplayImmediately() { return displayImmediately; }
    public void setDisplayImmediately(Boolean value) { this.displayImmediately = value; }
    public double[] getSuccessExitCodes() { return successExitCodes; }
    public void setSuccessExitCodes(double[] value) { this.successExitCodes = value; }

    @SuppressWarnings("unchecked")
    public static ProcessCommandExportOptions fromMap(Map<String, Object> map) {
        var value = new ProcessCommandExportOptions();
        var executablePathValue = map.get("ExecutablePath");
        value.setExecutablePath(executablePathValue == null ? null : (String) executablePathValue);
        var argumentsValue = map.get("Arguments");
        value.setArguments((String[]) argumentsValue);
        var workingDirectoryValue = map.get("WorkingDirectory");
        value.setWorkingDirectory(workingDirectoryValue == null ? null : (String) workingDirectoryValue);
        var environmentVariablesValue = map.get("EnvironmentVariables");
        value.setEnvironmentVariables(environmentVariablesValue == null ? null : (Map<String, String>) environmentVariablesValue);
        var inheritEnvironmentVariablesValue = map.get("InheritEnvironmentVariables");
        value.setInheritEnvironmentVariables(inheritEnvironmentVariablesValue == null ? null : (Boolean) inheritEnvironmentVariablesValue);
        var standardInputContentValue = map.get("StandardInputContent");
        value.setStandardInputContent(standardInputContentValue == null ? null : (String) standardInputContentValue);
        var killEntireProcessTreeValue = map.get("KillEntireProcessTree");
        value.setKillEntireProcessTree(killEntireProcessTreeValue == null ? null : (Boolean) killEntireProcessTreeValue);
        var commandOptionsValue = map.get("CommandOptions");
        value.setCommandOptions(commandOptionsValue == null ? null : CommandOptions.fromMap((Map<String, Object>) commandOptionsValue));
        var maxOutputLineCountValue = map.get("MaxOutputLineCount");
        value.setMaxOutputLineCount(maxOutputLineCountValue == null ? null : ((Number) maxOutputLineCountValue).doubleValue());
        var displayImmediatelyValue = map.get("DisplayImmediately");
        value.setDisplayImmediately(displayImmediatelyValue == null ? null : (Boolean) displayImmediatelyValue);
        var successExitCodesValue = map.get("SuccessExitCodes");
        value.setSuccessExitCodes((double[]) successExitCodesValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ExecutablePath", AspireClient.serializeValue(executablePath));
        map.put("Arguments", AspireClient.serializeValue(arguments));
        map.put("WorkingDirectory", AspireClient.serializeValue(workingDirectory));
        map.put("EnvironmentVariables", AspireClient.serializeValue(environmentVariables));
        map.put("InheritEnvironmentVariables", AspireClient.serializeValue(inheritEnvironmentVariables));
        map.put("StandardInputContent", AspireClient.serializeValue(standardInputContent));
        map.put("KillEntireProcessTree", AspireClient.serializeValue(killEntireProcessTree));
        map.put("CommandOptions", AspireClient.serializeValue(commandOptions));
        map.put("MaxOutputLineCount", AspireClient.serializeValue(maxOutputLineCount));
        map.put("DisplayImmediately", AspireClient.serializeValue(displayImmediately));
        map.put("SuccessExitCodes", AspireClient.serializeValue(successExitCodes));
        return map;
    }
}
