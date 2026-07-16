// PipelineSummary.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineSummary. */
public class PipelineSummary extends HandleWrapperBase {
    PipelineSummary(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Adds a key-value pair to the pipeline summary with a plain-text value. */
    public void add(String key, String value) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        reqArgs.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting.Pipelines/PipelineSummary.add", reqArgs);
    }

    /** Adds a key-value pair to the pipeline summary with a Markdown-formatted value. */
    public void addMarkdown(String key, String markdownString) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("summary", AspireClient.serializeValue(getHandle()));
        reqArgs.put("key", AspireClient.serializeValue(key));
        reqArgs.put("markdownString", AspireClient.serializeValue(markdownString));
        getClient().invokeCapability("Aspire.Hosting/addMarkdown", reqArgs);
    }

}
