// HealthCheckResult.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** HealthCheckResult DTO. */
public class HealthCheckResult implements JsonSerializable {
    private HealthStatus status;
    private String description;
    private Map<String, String> data;

    public HealthStatus getStatus() { return status; }
    public void setStatus(HealthStatus value) { this.status = value; }
    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
    public Map<String, String> getData() { return data; }
    public void setData(Map<String, String> value) { this.data = value; }

    @SuppressWarnings("unchecked")
    public static HealthCheckResult fromMap(Map<String, Object> map) {
        var value = new HealthCheckResult();
        var statusValue = map.get("Status");
        value.setStatus(HealthStatus.fromValue((String) statusValue));
        var descriptionValue = map.get("Description");
        value.setDescription(descriptionValue == null ? null : (String) descriptionValue);
        var dataValue = map.get("Data");
        value.setData(dataValue == null ? null : (Map<String, String>) dataValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Status", AspireClient.serializeValue(status));
        map.put("Description", AspireClient.serializeValue(description));
        map.put("Data", AspireClient.serializeValue(data));
        return map;
    }
}
