// HttpsCertificateInfo.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** HttpsCertificateInfo DTO. */
public class HttpsCertificateInfo implements JsonSerializable {
    private String subject;
    private String issuer;
    private String thumbprint;

    public String getSubject() { return subject; }
    public void setSubject(String value) { this.subject = value; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String value) { this.issuer = value; }
    public String getThumbprint() { return thumbprint; }
    public void setThumbprint(String value) { this.thumbprint = value; }

    @SuppressWarnings("unchecked")
    public static HttpsCertificateInfo fromMap(Map<String, Object> map) {
        var value = new HttpsCertificateInfo();
        var subjectValue = map.get("Subject");
        value.setSubject((String) subjectValue);
        var issuerValue = map.get("Issuer");
        value.setIssuer((String) issuerValue);
        var thumbprintValue = map.get("Thumbprint");
        value.setThumbprint(thumbprintValue == null ? null : (String) thumbprintValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Subject", AspireClient.serializeValue(subject));
        map.put("Issuer", AspireClient.serializeValue(issuer));
        map.put("Thumbprint", AspireClient.serializeValue(thumbprint));
        return map;
    }
}
