// CompleteStepOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for CompleteStep. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class CompleteStepOptions {
    private String completionState;
    private CancellationToken cancellationToken;

    public String getCompletionState() { return completionState; }
    public CompleteStepOptions completionState(String value) {
        this.completionState = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public CompleteStepOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
