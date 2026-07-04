// ResourceUrlAnnotation.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ResourceUrlAnnotation DTO. */
public class ResourceUrlAnnotation implements JsonSerializable {
    private String url;
    private String displayText;
    private EndpointReference endpoint;
    private UrlDisplayLocation displayLocation;

    public String getUrl() { return url; }
    public void setUrl(String value) { this.url = value; }
    public String getDisplayText() { return displayText; }
    public void setDisplayText(String value) { this.displayText = value; }
    public EndpointReference getEndpoint() { return endpoint; }
    public void setEndpoint(EndpointReference value) { this.endpoint = value; }
    public UrlDisplayLocation getDisplayLocation() { return displayLocation; }
    public void setDisplayLocation(UrlDisplayLocation value) { this.displayLocation = value; }

    @SuppressWarnings("unchecked")
    public static ResourceUrlAnnotation fromMap(Map<String, Object> map) {
        var value = new ResourceUrlAnnotation();
        var urlValue = map.get("Url");
        value.setUrl((String) urlValue);
        var displayTextValue = map.get("DisplayText");
        value.setDisplayText(displayTextValue == null ? null : (String) displayTextValue);
        var endpointValue = map.get("Endpoint");
        value.setEndpoint((EndpointReference) endpointValue);
        var displayLocationValue = map.get("DisplayLocation");
        value.setDisplayLocation(UrlDisplayLocation.fromValue((String) displayLocationValue));
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Url", AspireClient.serializeValue(url));
        map.put("DisplayText", AspireClient.serializeValue(displayText));
        map.put("Endpoint", AspireClient.serializeValue(endpoint));
        map.put("DisplayLocation", AspireClient.serializeValue(displayLocation));
        return map;
    }
}
