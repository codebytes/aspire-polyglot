// HttpsCertificateExecutionConfigurationExportData.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** HttpsCertificateExecutionConfigurationExportData DTO. */
public class HttpsCertificateExecutionConfigurationExportData implements JsonSerializable {
    private String subject;
    private String thumbprint;
    private String keyPathExpression;
    private String pfxPathExpression;
    private boolean isKeyPathReferenced;
    private boolean isPfxPathReferenced;
    private String password;

    public String getSubject() { return subject; }
    public void setSubject(String value) { this.subject = value; }
    public String getThumbprint() { return thumbprint; }
    public void setThumbprint(String value) { this.thumbprint = value; }
    public String getKeyPathExpression() { return keyPathExpression; }
    public void setKeyPathExpression(String value) { this.keyPathExpression = value; }
    public String getPfxPathExpression() { return pfxPathExpression; }
    public void setPfxPathExpression(String value) { this.pfxPathExpression = value; }
    public boolean getIsKeyPathReferenced() { return isKeyPathReferenced; }
    public void setIsKeyPathReferenced(boolean value) { this.isKeyPathReferenced = value; }
    public boolean getIsPfxPathReferenced() { return isPfxPathReferenced; }
    public void setIsPfxPathReferenced(boolean value) { this.isPfxPathReferenced = value; }
    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

    @SuppressWarnings("unchecked")
    public static HttpsCertificateExecutionConfigurationExportData fromMap(Map<String, Object> map) {
        var value = new HttpsCertificateExecutionConfigurationExportData();
        var subjectValue = map.get("Subject");
        value.setSubject((String) subjectValue);
        var thumbprintValue = map.get("Thumbprint");
        value.setThumbprint(thumbprintValue == null ? null : (String) thumbprintValue);
        var keyPathExpressionValue = map.get("KeyPathExpression");
        value.setKeyPathExpression((String) keyPathExpressionValue);
        var pfxPathExpressionValue = map.get("PfxPathExpression");
        value.setPfxPathExpression((String) pfxPathExpressionValue);
        var isKeyPathReferencedValue = map.get("IsKeyPathReferenced");
        value.setIsKeyPathReferenced((Boolean) isKeyPathReferencedValue);
        var isPfxPathReferencedValue = map.get("IsPfxPathReferenced");
        value.setIsPfxPathReferenced((Boolean) isPfxPathReferencedValue);
        var passwordValue = map.get("Password");
        value.setPassword(passwordValue == null ? null : (String) passwordValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Subject", AspireClient.serializeValue(subject));
        map.put("Thumbprint", AspireClient.serializeValue(thumbprint));
        map.put("KeyPathExpression", AspireClient.serializeValue(keyPathExpression));
        map.put("PfxPathExpression", AspireClient.serializeValue(pfxPathExpression));
        map.put("IsKeyPathReferenced", AspireClient.serializeValue(isKeyPathReferenced));
        map.put("IsPfxPathReferenced", AspireClient.serializeValue(isPfxPathReferenced));
        map.put("Password", AspireClient.serializeValue(password));
        return map;
    }
}
