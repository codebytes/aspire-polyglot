// ContainerFilesOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;

/** ContainerFilesOptions DTO. */
@SuppressWarnings({"all", "unchecked", "serial"})
public class ContainerFilesOptions implements JsonSerializable {
    private Number defaultOwner;
    private Number defaultGroup;
    private Number umask;

    public Number getDefaultOwner() { return defaultOwner; }
    public void setDefaultOwner(Number value) { this.defaultOwner = value; }
    public Number getDefaultGroup() { return defaultGroup; }
    public void setDefaultGroup(Number value) { this.defaultGroup = value; }
    public Number getUmask() { return umask; }
    public void setUmask(Number value) { this.umask = value; }

    @SuppressWarnings("unchecked")
    public static ContainerFilesOptions fromMap(Map<String, Object> map) {
        var value = new ContainerFilesOptions();
        var defaultOwnerValue = map.get("DefaultOwner");
        value.setDefaultOwner(defaultOwnerValue == null ? null : ((Number) defaultOwnerValue).doubleValue());
        var defaultGroupValue = map.get("DefaultGroup");
        value.setDefaultGroup(defaultGroupValue == null ? null : ((Number) defaultGroupValue).doubleValue());
        var umaskValue = map.get("Umask");
        value.setUmask(umaskValue == null ? null : ((Number) umaskValue).doubleValue());
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("DefaultOwner", AspireClient.serializeValue(defaultOwner));
        map.put("DefaultGroup", AspireClient.serializeValue(defaultGroup));
        map.put("Umask", AspireClient.serializeValue(umask));
        return map;
    }
}
