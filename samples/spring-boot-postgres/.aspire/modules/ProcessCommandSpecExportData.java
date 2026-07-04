// ProcessCommandSpecExportData.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ProcessCommandSpecExportData DTO. */
public class ProcessCommandSpecExportData implements JsonSerializable {
    private String executablePath;
    private String[] arguments;
    private String workingDirectory;
    private Map<String, String> environmentVariables;
    private Boolean inheritEnvironmentVariables;
    private String standardInputContent;
    private Boolean killEntireProcessTree;

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

    @SuppressWarnings("unchecked")
    public static ProcessCommandSpecExportData fromMap(Map<String, Object> map) {
        var value = new ProcessCommandSpecExportData();
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
        return map;
    }
}
