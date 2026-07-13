// CommandResultFormat.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CommandResultFormat enum. */
public enum CommandResultFormat implements WireValueEnum {
    TEXT("Text"),
    JSON("Json"),
    MARKDOWN("Markdown");

    private final String value;

    CommandResultFormat(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static CommandResultFormat fromValue(String value) {
        for (CommandResultFormat e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
