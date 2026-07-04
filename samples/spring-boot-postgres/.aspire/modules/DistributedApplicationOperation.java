// DistributedApplicationOperation.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** DistributedApplicationOperation enum. */
public enum DistributedApplicationOperation implements WireValueEnum {
    RUN("Run"),
    PUBLISH("Publish");

    private final String value;

    DistributedApplicationOperation(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static DistributedApplicationOperation fromValue(String value) {
        for (DistributedApplicationOperation e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
