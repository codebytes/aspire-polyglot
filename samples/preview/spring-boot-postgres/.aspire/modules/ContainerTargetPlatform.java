// ContainerTargetPlatform.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ContainerTargetPlatform enum. */
public enum ContainerTargetPlatform implements WireValueEnum {
    LINUX_AMD64("LinuxAmd64"),
    LINUX_ARM64("LinuxArm64"),
    ALL_LINUX("AllLinux"),
    LINUX_ARM("LinuxArm"),
    LINUX386("Linux386"),
    WINDOWS_AMD64("WindowsAmd64"),
    WINDOWS_ARM64("WindowsArm64");

    private final String value;

    ContainerTargetPlatform(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static ContainerTargetPlatform fromValue(String value) {
        for (ContainerTargetPlatform e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
