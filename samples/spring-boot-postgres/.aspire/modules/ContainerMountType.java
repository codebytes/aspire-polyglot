// ContainerMountType.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ContainerMountType enum. */
public enum ContainerMountType implements WireValueEnum {
    BIND_MOUNT("BindMount"),
    VOLUME("Volume");

    private final String value;

    ContainerMountType(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ContainerMountType fromValue(String value) {
        for (ContainerMountType e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
