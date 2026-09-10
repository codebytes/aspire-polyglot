// ContainerImageFormat.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ContainerImageFormat enum. */
public enum ContainerImageFormat implements WireValueEnum {
    DOCKER("Docker"),
    OCI("Oci");

    private final String value;

    ContainerImageFormat(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ContainerImageFormat fromValue(String value) {
        for (ContainerImageFormat e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
