// ExecuteCommandAsyncOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for ExecuteCommandAsync. */
public final class ExecuteCommandAsyncOptions {
    private Map<String, String> arguments;
    private CancellationToken cancellationToken;

    public Map<String, String> getArguments() { return arguments; }
    public ExecuteCommandAsyncOptions arguments(Map<String, String> value) {
        this.arguments = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public ExecuteCommandAsyncOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
