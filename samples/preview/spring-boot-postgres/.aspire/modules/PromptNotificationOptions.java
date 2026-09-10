// PromptNotificationOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for PromptNotification. */
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
