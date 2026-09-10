// RunConfiguration.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** RunConfiguration DTO. */
public class RunConfiguration implements JsonSerializable {
    private Boolean watchEnabled;

    public Boolean getWatchEnabled() { return watchEnabled; }
    public void setWatchEnabled(Boolean value) { this.watchEnabled = value; }

    @SuppressWarnings("unchecked")
    public static RunConfiguration fromMap(Map<String, Object> map) {
        var value = new RunConfiguration();
        var watchEnabledValue = map.get("WatchEnabled");
        value.setWatchEnabled(watchEnabledValue == null ? null : (Boolean) watchEnabledValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("WatchEnabled", AspireClient.serializeValue(watchEnabled));
        return map;
    }
}
