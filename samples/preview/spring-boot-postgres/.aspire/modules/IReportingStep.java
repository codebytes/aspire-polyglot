// IReportingStep.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.IReportingStep. */
public class IReportingStep extends HandleWrapperBase {
    IReportingStep(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public IReportingTask createTask(String statusText) {
        return createTask(statusText, null);
    }

    /** Creates a reporting task with plain-text status text. */
    public IReportingTask createTask(String statusText, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingStep", AspireClient.serializeValue(getHandle()));
        reqArgs.put("statusText", AspireClient.serializeValue(statusText));
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createTask", reqArgs);
        return (IReportingTask) result;
    }

    public IReportingTask createMarkdownTask(String markdownString) {
        return createMarkdownTask(markdownString, null);
    }

    /** Creates a reporting task with Markdown-formatted status text. */
    public IReportingTask createMarkdownTask(String markdownString, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingStep", AspireClient.serializeValue(getHandle()));
        reqArgs.put("markdownString", AspireClient.serializeValue(markdownString));
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createMarkdownTask", reqArgs);
        return (IReportingTask) result;
    }

    /** Logs a plain-text message for the reporting step. */
    public void logStep(String level, String message) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingStep", AspireClient.serializeValue(getHandle()));
        reqArgs.put("level", AspireClient.serializeValue(level));
        reqArgs.put("message", AspireClient.serializeValue(message));
        getClient().invokeCapability("Aspire.Hosting/logStep", reqArgs);
    }

    /** Logs a Markdown-formatted message for the reporting step. */
    public void logStepMarkdown(String level, String markdownString) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingStep", AspireClient.serializeValue(getHandle()));
        reqArgs.put("level", AspireClient.serializeValue(level));
        reqArgs.put("markdownString", AspireClient.serializeValue(markdownString));
        getClient().invokeCapability("Aspire.Hosting/logStepMarkdown", reqArgs);
    }

    /** Completes the reporting step with plain-text completion text. */
    public void completeStep(String completionText, CompleteStepOptions options) {
        var completionState = options == null ? null : options.getCompletionState();
        var cancellationToken = options == null ? null : options.getCancellationToken();
        completeStepImpl(completionText, completionState, cancellationToken);
    }

    public void completeStep(String completionText) {
        completeStep(completionText, null);
    }

    /** Completes the reporting step with plain-text completion text. */
    private void completeStepImpl(String completionText, String completionState, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingStep", AspireClient.serializeValue(getHandle()));
        reqArgs.put("completionText", AspireClient.serializeValue(completionText));
        if (completionState != null) {
            reqArgs.put("completionState", AspireClient.serializeValue(completionState));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        getClient().invokeCapability("Aspire.Hosting/completeStep", reqArgs);
    }

    /** Completes the reporting step with Markdown-formatted completion text. */
    public void completeStepMarkdown(String markdownString, CompleteStepMarkdownOptions options) {
        var completionState = options == null ? null : options.getCompletionState();
        var cancellationToken = options == null ? null : options.getCancellationToken();
        completeStepMarkdownImpl(markdownString, completionState, cancellationToken);
    }

    public void completeStepMarkdown(String markdownString) {
        completeStepMarkdown(markdownString, null);
    }

    /** Completes the reporting step with Markdown-formatted completion text. */
    private void completeStepMarkdownImpl(String markdownString, String completionState, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("reportingStep", AspireClient.serializeValue(getHandle()));
        reqArgs.put("markdownString", AspireClient.serializeValue(markdownString));
        if (completionState != null) {
            reqArgs.put("completionState", AspireClient.serializeValue(completionState));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        getClient().invokeCapability("Aspire.Hosting/completeStepMarkdown", reqArgs);
    }

}
