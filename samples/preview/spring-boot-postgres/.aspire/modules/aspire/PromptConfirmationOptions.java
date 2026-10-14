// PromptConfirmationOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for PromptConfirmation. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class PromptConfirmationOptions {
    private InteractionMessageBoxOptions options;
    private CancellationToken cancellationToken;

    public InteractionMessageBoxOptions getOptions() { return options; }
    public PromptConfirmationOptions options(InteractionMessageBoxOptions value) {
        this.options = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public PromptConfirmationOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
