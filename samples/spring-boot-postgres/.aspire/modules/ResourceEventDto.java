// ResourceEventDto.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ResourceEventDto DTO. */
public class ResourceEventDto implements JsonSerializable {
    private String resourceName;
    private String resourceId;
    private String state;
    private String stateStyle;
    private String healthStatus;
    private Double exitCode;

    public String getResourceName() { return resourceName; }
    public void setResourceName(String value) { this.resourceName = value; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String value) { this.resourceId = value; }
    public String getState() { return state; }
    public void setState(String value) { this.state = value; }
    public String getStateStyle() { return stateStyle; }
    public void setStateStyle(String value) { this.stateStyle = value; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String value) { this.healthStatus = value; }
    public Double getExitCode() { return exitCode; }
    public void setExitCode(Double value) { this.exitCode = value; }

    @SuppressWarnings("unchecked")
    public static ResourceEventDto fromMap(Map<String, Object> map) {
        var value = new ResourceEventDto();
        var resourceNameValue = map.get("ResourceName");
        value.setResourceName((String) resourceNameValue);
        var resourceIdValue = map.get("ResourceId");
        value.setResourceId((String) resourceIdValue);
        var stateValue = map.get("State");
        value.setState(stateValue == null ? null : (String) stateValue);
        var stateStyleValue = map.get("StateStyle");
        value.setStateStyle(stateStyleValue == null ? null : (String) stateStyleValue);
        var healthStatusValue = map.get("HealthStatus");
        value.setHealthStatus(healthStatusValue == null ? null : (String) healthStatusValue);
        var exitCodeValue = map.get("ExitCode");
        value.setExitCode(exitCodeValue == null ? null : ((Number) exitCodeValue).doubleValue());
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ResourceName", AspireClient.serializeValue(resourceName));
        map.put("ResourceId", AspireClient.serializeValue(resourceId));
        map.put("State", AspireClient.serializeValue(state));
        map.put("StateStyle", AspireClient.serializeValue(stateStyle));
        map.put("HealthStatus", AspireClient.serializeValue(healthStatus));
        map.put("ExitCode", AspireClient.serializeValue(exitCode));
        return map;
    }
}
