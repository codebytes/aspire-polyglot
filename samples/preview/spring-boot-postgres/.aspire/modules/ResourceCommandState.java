// ResourceCommandState.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ResourceCommandState enum. */
public enum ResourceCommandState implements WireValueEnum {
    ENABLED("Enabled"),
    DISABLED("Disabled"),
    HIDDEN("Hidden");

    private final String value;

    ResourceCommandState(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ResourceCommandState fromValue(String value) {
        for (ResourceCommandState e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
