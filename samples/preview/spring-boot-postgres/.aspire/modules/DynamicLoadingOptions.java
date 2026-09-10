// DynamicLoadingOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** DynamicLoadingOptions DTO. */
public class DynamicLoadingOptions implements JsonSerializable {
    private Boolean alwaysLoadOnStart;
    private String[] dependsOnInputs;

    public Boolean getAlwaysLoadOnStart() { return alwaysLoadOnStart; }
    public void setAlwaysLoadOnStart(Boolean value) { this.alwaysLoadOnStart = value; }
    public String[] getDependsOnInputs() { return dependsOnInputs; }
    public void setDependsOnInputs(String[] value) { this.dependsOnInputs = value; }

    @SuppressWarnings("unchecked")
    public static DynamicLoadingOptions fromMap(Map<String, Object> map) {
        var value = new DynamicLoadingOptions();
        var alwaysLoadOnStartValue = map.get("AlwaysLoadOnStart");
        value.setAlwaysLoadOnStart(alwaysLoadOnStartValue == null ? null : (Boolean) alwaysLoadOnStartValue);
        var dependsOnInputsValue = map.get("DependsOnInputs");
        value.setDependsOnInputs((String[]) dependsOnInputsValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("AlwaysLoadOnStart", AspireClient.serializeValue(alwaysLoadOnStart));
        map.put("DependsOnInputs", AspireClient.serializeValue(dependsOnInputs));
        return map;
    }
}
