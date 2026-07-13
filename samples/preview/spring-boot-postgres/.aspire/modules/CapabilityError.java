// CapabilityError.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

/**
 * CapabilityError represents an error from a capability invocation.
 */
public class CapabilityError extends RuntimeException {
    private final String code;
    private final Object data;

    CapabilityError(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    String getCode() { return code; }
    Object getData() { return data; }
}
