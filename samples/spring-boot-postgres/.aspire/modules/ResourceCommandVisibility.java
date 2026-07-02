// ResourceCommandVisibility.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ResourceCommandVisibility enum. */
public enum ResourceCommandVisibility implements WireValueEnum {
    NONE("None"),
    UI("UI"),
    API("Api");

    private final String value;

    ResourceCommandVisibility(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ResourceCommandVisibility fromValue(String value) {
        for (ResourceCommandVisibility e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
