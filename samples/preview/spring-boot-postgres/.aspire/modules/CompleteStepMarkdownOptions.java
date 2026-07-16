// CompleteStepMarkdownOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for CompleteStepMarkdown. */
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
