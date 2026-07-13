// IconVariant.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** IconVariant enum. */
public enum IconVariant implements WireValueEnum {
    REGULAR("Regular"),
    FILLED("Filled");

    private final String value;

    IconVariant(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static IconVariant fromValue(String value) {
        for (IconVariant e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
