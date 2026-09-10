// ContainerImageDestination.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ContainerImageDestination enum. */
public enum ContainerImageDestination implements WireValueEnum {
    REGISTRY("Registry"),
    ARCHIVE("Archive");

    private final String value;

    ContainerImageDestination(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ContainerImageDestination fromValue(String value) {
        for (ContainerImageDestination e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
