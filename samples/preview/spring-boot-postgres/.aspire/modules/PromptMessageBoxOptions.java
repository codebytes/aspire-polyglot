// PromptMessageBoxOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for PromptMessageBox. */
public final class PromptMessageBoxOptions {
    private InteractionMessageBoxOptions options;
    private CancellationToken cancellationToken;

    public InteractionMessageBoxOptions getOptions() { return options; }
    public PromptMessageBoxOptions options(InteractionMessageBoxOptions value) {
        this.options = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public PromptMessageBoxOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
