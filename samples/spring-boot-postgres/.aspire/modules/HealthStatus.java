// HealthStatus.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** HealthStatus enum. */
public enum HealthStatus implements WireValueEnum {
    UNHEALTHY("Unhealthy"),
    DEGRADED("Degraded"),
    HEALTHY("Healthy");

    private final String value;

    HealthStatus(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static HealthStatus fromValue(String value) {
        for (HealthStatus e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
