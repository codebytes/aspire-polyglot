// PromptInputOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for PromptInput. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class PromptInputOptions {
    private InteractionInputsDialogOptions options;
    private CancellationToken cancellationToken;

    public InteractionInputsDialogOptions getOptions() { return options; }
    public PromptInputOptions options(InteractionInputsDialogOptions value) {
        this.options = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public PromptInputOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
