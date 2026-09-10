// MessageIntent.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** MessageIntent enum. */
public enum MessageIntent implements WireValueEnum {
    NONE("None"),
    SUCCESS("Success"),
    WARNING("Warning"),
    ERROR("Error"),
    INFORMATION("Information"),
    CONFIRMATION("Confirmation");

    private final String value;

    MessageIntent(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static MessageIntent fromValue(String value) {
        for (MessageIntent e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
