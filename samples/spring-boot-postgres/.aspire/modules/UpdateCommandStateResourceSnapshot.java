// UpdateCommandStateResourceSnapshot.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** UpdateCommandStateResourceSnapshot DTO. */
public class UpdateCommandStateResourceSnapshot implements JsonSerializable {
    private String resourceType;
    private String state;
    private String stateStyle;
    private HealthStatus healthStatus;
    private Double exitCode;

    public String getResourceType() { return resourceType; }
    public void setResourceType(String value) { this.resourceType = value; }
    public String getState() { return state; }
    public void setState(String value) { this.state = value; }
    public String getStateStyle() { return stateStyle; }
    public void setStateStyle(String value) { this.stateStyle = value; }
    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus value) { this.healthStatus = value; }
    public Double getExitCode() { return exitCode; }
    public void setExitCode(Double value) { this.exitCode = value; }

    @SuppressWarnings("unchecked")
    public static UpdateCommandStateResourceSnapshot fromMap(Map<String, Object> map) {
        var value = new UpdateCommandStateResourceSnapshot();
        var resourceTypeValue = map.get("ResourceType");
        value.setResourceType((String) resourceTypeValue);
        var stateValue = map.get("State");
        value.setState(stateValue == null ? null : (String) stateValue);
        var stateStyleValue = map.get("StateStyle");
        value.setStateStyle(stateStyleValue == null ? null : (String) stateStyleValue);
        var healthStatusValue = map.get("HealthStatus");
        value.setHealthStatus(healthStatusValue == null ? null : HealthStatus.fromValue((String) healthStatusValue));
        var exitCodeValue = map.get("ExitCode");
        value.setExitCode(exitCodeValue == null ? null : ((Number) exitCodeValue).doubleValue());
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ResourceType", AspireClient.serializeValue(resourceType));
        map.put("State", AspireClient.serializeValue(state));
        map.put("StateStyle", AspireClient.serializeValue(stateStyle));
        map.put("HealthStatus", AspireClient.serializeValue(healthStatus));
        map.put("ExitCode", AspireClient.serializeValue(exitCode));
        return map;
    }
}
