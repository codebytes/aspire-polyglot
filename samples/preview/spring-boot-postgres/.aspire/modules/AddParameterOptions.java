// AddParameterOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for AddParameter. */
public final class AddParameterOptions {
    private String value;
    private Boolean publishValueAsDefault;
    private Boolean secret;

    public String getValue() { return value; }
    public AddParameterOptions value(String value) {
        this.value = value;
        return this;
    }

    public Boolean getPublishValueAsDefault() { return publishValueAsDefault; }
    public AddParameterOptions publishValueAsDefault(Boolean value) {
        this.publishValueAsDefault = value;
        return this;
    }

    public Boolean getSecret() { return secret; }
    public AddParameterOptions secret(Boolean value) {
        this.secret = value;
        return this;
    }

}
