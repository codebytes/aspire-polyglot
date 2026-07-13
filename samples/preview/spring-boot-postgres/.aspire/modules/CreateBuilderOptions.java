// CreateBuilderOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CreateBuilderOptions DTO. */
public class CreateBuilderOptions implements JsonSerializable {
    private String[] args;
    private String projectDirectory;
    private String appHostFilePath;
    private String containerRegistryOverride;
    private boolean disableDashboard;
    private String dashboardApplicationName;
    private boolean allowUnsecuredTransport;
    private boolean enableResourceLogging;

    public String[] getArgs() { return args; }
    public void setArgs(String[] value) { this.args = value; }
    public String getProjectDirectory() { return projectDirectory; }
    public void setProjectDirectory(String value) { this.projectDirectory = value; }
    public String getAppHostFilePath() { return appHostFilePath; }
    public void setAppHostFilePath(String value) { this.appHostFilePath = value; }
    public String getContainerRegistryOverride() { return containerRegistryOverride; }
    public void setContainerRegistryOverride(String value) { this.containerRegistryOverride = value; }
    public boolean getDisableDashboard() { return disableDashboard; }
    public void setDisableDashboard(boolean value) { this.disableDashboard = value; }
    public String getDashboardApplicationName() { return dashboardApplicationName; }
    public void setDashboardApplicationName(String value) { this.dashboardApplicationName = value; }
    public boolean getAllowUnsecuredTransport() { return allowUnsecuredTransport; }
    public void setAllowUnsecuredTransport(boolean value) { this.allowUnsecuredTransport = value; }
    public boolean getEnableResourceLogging() { return enableResourceLogging; }
    public void setEnableResourceLogging(boolean value) { this.enableResourceLogging = value; }

    @SuppressWarnings("unchecked")
    public static CreateBuilderOptions fromMap(Map<String, Object> map) {
        var value = new CreateBuilderOptions();
        var argsValue = map.get("Args");
        value.setArgs((String[]) argsValue);
        var projectDirectoryValue = map.get("ProjectDirectory");
        value.setProjectDirectory(projectDirectoryValue == null ? null : (String) projectDirectoryValue);
        var appHostFilePathValue = map.get("AppHostFilePath");
        value.setAppHostFilePath(appHostFilePathValue == null ? null : (String) appHostFilePathValue);
        var containerRegistryOverrideValue = map.get("ContainerRegistryOverride");
        value.setContainerRegistryOverride(containerRegistryOverrideValue == null ? null : (String) containerRegistryOverrideValue);
        var disableDashboardValue = map.get("DisableDashboard");
        value.setDisableDashboard((Boolean) disableDashboardValue);
        var dashboardApplicationNameValue = map.get("DashboardApplicationName");
        value.setDashboardApplicationName(dashboardApplicationNameValue == null ? null : (String) dashboardApplicationNameValue);
        var allowUnsecuredTransportValue = map.get("AllowUnsecuredTransport");
        value.setAllowUnsecuredTransport((Boolean) allowUnsecuredTransportValue);
        var enableResourceLoggingValue = map.get("EnableResourceLogging");
        value.setEnableResourceLogging((Boolean) enableResourceLoggingValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Args", AspireClient.serializeValue(args));
        map.put("ProjectDirectory", AspireClient.serializeValue(projectDirectory));
        map.put("AppHostFilePath", AspireClient.serializeValue(appHostFilePath));
        map.put("ContainerRegistryOverride", AspireClient.serializeValue(containerRegistryOverride));
        map.put("DisableDashboard", AspireClient.serializeValue(disableDashboard));
        map.put("DashboardApplicationName", AspireClient.serializeValue(dashboardApplicationName));
        map.put("AllowUnsecuredTransport", AspireClient.serializeValue(allowUnsecuredTransport));
        map.put("EnableResourceLogging", AspireClient.serializeValue(enableResourceLogging));
        return map;
    }
}
