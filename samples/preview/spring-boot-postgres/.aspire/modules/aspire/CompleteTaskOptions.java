// CompleteTaskOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for CompleteTask. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class CompleteTaskOptions {
    private String completionMessage;
    private String completionState;
    private CancellationToken cancellationToken;

    public String getCompletionMessage() { return completionMessage; }
    public CompleteTaskOptions completionMessage(String value) {
        this.completionMessage = value;
        return this;
    }

    public String getCompletionState() { return completionState; }
    public CompleteTaskOptions completionState(String value) {
        this.completionState = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public CompleteTaskOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
