// WaitBehavior.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** WaitBehavior enum. */
public enum WaitBehavior implements WireValueEnum {
    WAIT_ON_RESOURCE_UNAVAILABLE("WaitOnResourceUnavailable"),
    STOP_ON_RESOURCE_UNAVAILABLE("StopOnResourceUnavailable");

    private final String value;

    WaitBehavior(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static WaitBehavior fromValue(String value) {
        for (WaitBehavior e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
