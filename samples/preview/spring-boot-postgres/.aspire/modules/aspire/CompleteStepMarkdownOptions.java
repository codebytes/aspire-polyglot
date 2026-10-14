// CompleteStepMarkdownOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for CompleteStepMarkdown. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class CompleteStepMarkdownOptions {
    private String completionState;
    private CancellationToken cancellationToken;

    public String getCompletionState() { return completionState; }
    public CompleteStepMarkdownOptions completionState(String value) {
        this.completionState = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public CompleteStepMarkdownOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
