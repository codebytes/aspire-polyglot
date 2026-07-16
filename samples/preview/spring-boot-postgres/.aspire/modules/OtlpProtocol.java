// OtlpProtocol.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** OtlpProtocol enum. */
public enum OtlpProtocol implements WireValueEnum {
    GRPC("Grpc"),
    HTTP_PROTOBUF("HttpProtobuf"),
    HTTP_JSON("HttpJson");

    private final String value;

    OtlpProtocol(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static OtlpProtocol fromValue(String value) {
        for (OtlpProtocol e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
