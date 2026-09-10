// PromptProgressOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for PromptProgress. */
public final class PromptProgressOptions {
    private InteractionProgressOptions options;
    private CancellationToken cancellationToken;

    public InteractionProgressOptions getOptions() { return options; }
    public PromptProgressOptions options(InteractionProgressOptions value) {
        this.options = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public PromptProgressOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
