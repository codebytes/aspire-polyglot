// PromptNotificationOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for PromptNotification. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class PromptNotificationOptions {
    private InteractionNotificationOptions options;
    private CancellationToken cancellationToken;

    public InteractionNotificationOptions getOptions() { return options; }
    public PromptNotificationOptions options(InteractionNotificationOptions value) {
        this.options = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public PromptNotificationOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
