// HttpCommandRequestExportData.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** HttpCommandRequestExportData DTO. */
public class HttpCommandRequestExportData implements JsonSerializable {
    private String methodName;
    private Map<String, String> headers;
    private String content;
    private String contentType;

    public String getMethodName() { return methodName; }
    public void setMethodName(String value) { this.methodName = value; }
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> value) { this.headers = value; }
    public String getContent() { return content; }
    public void setContent(String value) { this.content = value; }
    public String getContentType() { return contentType; }
    public void setContentType(String value) { this.contentType = value; }

    @SuppressWarnings("unchecked")
    public static HttpCommandRequestExportData fromMap(Map<String, Object> map) {
        var value = new HttpCommandRequestExportData();
        var methodNameValue = map.get("MethodName");
        value.setMethodName(methodNameValue == null ? null : (String) methodNameValue);
        var headersValue = map.get("Headers");
        value.setHeaders(headersValue == null ? null : (Map<String, String>) headersValue);
        var contentValue = map.get("Content");
        value.setContent(contentValue == null ? null : (String) contentValue);
        var contentTypeValue = map.get("ContentType");
        value.setContentType(contentTypeValue == null ? null : (String) contentTypeValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("MethodName", AspireClient.serializeValue(methodName));
        map.put("Headers", AspireClient.serializeValue(headers));
        map.put("Content", AspireClient.serializeValue(content));
        map.put("ContentType", AspireClient.serializeValue(contentType));
        return map;
    }
}
