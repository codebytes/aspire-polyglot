// WithReferenceOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for WithReference. */
public final class WithReferenceOptions {
    private String connectionName;
    private Boolean optional;
    private String name;

    public String getConnectionName() { return connectionName; }
    public WithReferenceOptions connectionName(String value) {
        this.connectionName = value;
        return this;
    }

    public Boolean getOptional() { return optional; }
    public WithReferenceOptions optional(Boolean value) {
        this.optional = value;
        return this;
    }

    public String getName() { return name; }
    public WithReferenceOptions name(String value) {
        this.name = value;
        return this;
    }

}
