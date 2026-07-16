// IReportingTask.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.IReportingTask. */
public class IReportingTask extends HandleWrapperBase {
    IReportingTask(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public void updateTask(String statusText) {
        updateTask(statusText, null);
    }

    /** Updates the reporting task with plain-text status text. */
    public void updateTask(String statusText, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingTask", AspireClient.serializeValue(getHandle()));
        reqArgs.put("statusText", AspireClient.serializeValue(statusText));
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        getClient().invokeCapability("Aspire.Hosting/updateTask", reqArgs);
    }

    public void updateTaskMarkdown(String markdownString) {
        updateTaskMarkdown(markdownString, null);
    }

    /** Updates the reporting task with Markdown-formatted status text. */
    public void updateTaskMarkdown(String markdownString, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingTask", AspireClient.serializeValue(getHandle()));
        reqArgs.put("markdownString", AspireClient.serializeValue(markdownString));
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        getClient().invokeCapability("Aspire.Hosting/updateTaskMarkdown", reqArgs);
    }

    /** Completes the reporting task with plain-text completion text. */
    public void completeTask(CompleteTaskOptions options) {
        var completionMessage = options == null ? null : options.getCompletionMessage();
        var completionState = options == null ? null : options.getCompletionState();
        var cancellationToken = options == null ? null : options.getCancellationToken();
        completeTaskImpl(completionMessage, completionState, cancellationToken);
    }

    public void completeTask() {
        completeTask(null);
    }

    /** Completes the reporting task with plain-text completion text. */
    private void completeTaskImpl(String completionMessage, String completionState, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingTask", AspireClient.serializeValue(getHandle()));
        if (completionMessage != null) {
            reqArgs.put("completionMessage", AspireClient.serializeValue(completionMessage));
        }
        if (completionState != null) {
            reqArgs.put("completionState", AspireClient.serializeValue(completionState));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        getClient().invokeCapability("Aspire.Hosting/completeTask", reqArgs);
    }

    /** Completes the reporting task with Markdown-formatted completion text. */
    public void completeTaskMarkdown(String markdownString, CompleteTaskMarkdownOptions options) {
        var completionState = options == null ? null : options.getCompletionState();
        var cancellationToken = options == null ? null : options.getCancellationToken();
        completeTaskMarkdownImpl(markdownString, completionState, cancellationToken);
    }

    public void completeTaskMarkdown(String markdownString) {
        completeTaskMarkdown(markdownString, null);
    }

    /** Completes the reporting task with Markdown-formatted completion text. */
    private void completeTaskMarkdownImpl(String markdownString, String completionState, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingTask", AspireClient.serializeValue(getHandle()));
        reqArgs.put("markdownString", AspireClient.serializeValue(markdownString));
        if (completionState != null) {
            reqArgs.put("completionState", AspireClient.serializeValue(completionState));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        getClient().invokeCapability("Aspire.Hosting/completeTaskMarkdown", reqArgs);
    }

}
