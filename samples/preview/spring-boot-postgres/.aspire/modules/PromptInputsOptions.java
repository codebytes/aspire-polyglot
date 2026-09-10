// PromptInputsOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for PromptInputs. */
public final class PromptInputsOptions {
    private InteractionInputsDialogOptions options;
    private CancellationToken cancellationToken;

    public InteractionInputsDialogOptions getOptions() { return options; }
    public PromptInputsOptions options(InteractionInputsDialogOptions value) {
        this.options = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public PromptInputsOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
