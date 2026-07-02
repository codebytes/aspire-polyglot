// CompleteTaskMarkdownOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for CompleteTaskMarkdown. */
public final class CompleteTaskMarkdownOptions {
    private String completionState;
    private CancellationToken cancellationToken;

    public String getCompletionState() { return completionState; }
    public CompleteTaskMarkdownOptions completionState(String value) {
        this.completionState = value;
        return this;
    }

    public CancellationToken getCancellationToken() { return cancellationToken; }
    public CompleteTaskMarkdownOptions cancellationToken(CancellationToken value) {
        this.cancellationToken = value;
        return this;
    }

}
