// Handle.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle represents a remote object reference.
 */
@SuppressWarnings({"all", "unchecked", "serial"})
public class Handle {
    private final String id;
    private final String typeId;

    Handle(String id, String typeId) {
        this.id = id;
        this.typeId = typeId;
    }

    String getId() { return id; }
    String getTypeId() { return typeId; }

    Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("$handle", id);
        result.put("$type", typeId);
        return result;
    }

    @Override
    public String toString() {
        return "Handle{id='" + id + "', typeId='" + typeId + "'}";
    }
}
