// CertificateTrustScope.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CertificateTrustScope enum. */
public enum CertificateTrustScope implements WireValueEnum {
    NONE("None"),
    APPEND("Append"),
    OVERRIDE("Override"),
    SYSTEM("System");

    private final String value;

    CertificateTrustScope(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static CertificateTrustScope fromValue(String value) {
        for (CertificateTrustScope e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
