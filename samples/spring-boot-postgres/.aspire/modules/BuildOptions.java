// BuildOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for Build. */
public final class BuildOptions {
    private ILogger resourceLogger;
    private CancellationToken cancellationToken;

    public ILogger getResourceLogger() { return resourceLogger; }
    public BuildOptions resourceLogger(ILogger value) {
        this.resourceLogger = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public BuildOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
