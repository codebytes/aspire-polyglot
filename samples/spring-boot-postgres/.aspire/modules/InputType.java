// InputType.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** InputType enum. */
public enum InputType implements WireValueEnum {
    TEXT("Text"),
    SECRET_TEXT("SecretText"),
    CHOICE("Choice"),
    BOOLEAN("Boolean"),
    NUMBER("Number");

    private final String value;

    InputType(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static InputType fromValue(String value) {
        for (InputType e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
