// HttpsCertificateExecutionConfigurationContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** HttpsCertificateExecutionConfigurationContext DTO. */
public class HttpsCertificateExecutionConfigurationContext implements JsonSerializable {
    private ReferenceExpression certificatePath;
    private ReferenceExpression keyPath;
    private ReferenceExpression certificateWithKeyPath;
    private ReferenceExpression pfxPath;

    public ReferenceExpression getCertificatePath() { return certificatePath; }
    public void setCertificatePath(ReferenceExpression value) { this.certificatePath = value; }
    public ReferenceExpression getKeyPath() { return keyPath; }
    public void setKeyPath(ReferenceExpression value) { this.keyPath = value; }
    public ReferenceExpression getCertificateWithKeyPath() { return certificateWithKeyPath; }
    public void setCertificateWithKeyPath(ReferenceExpression value) { this.certificateWithKeyPath = value; }
    public ReferenceExpression getPfxPath() { return pfxPath; }
    public void setPfxPath(ReferenceExpression value) { this.pfxPath = value; }

    @SuppressWarnings("unchecked")
    public static HttpsCertificateExecutionConfigurationContext fromMap(Map<String, Object> map) {
        var value = new HttpsCertificateExecutionConfigurationContext();
        var certificatePathValue = map.get("CertificatePath");
        value.setCertificatePath((ReferenceExpression) certificatePathValue);
        var keyPathValue = map.get("KeyPath");
        value.setKeyPath((ReferenceExpression) keyPathValue);
        var certificateWithKeyPathValue = map.get("CertificateWithKeyPath");
        value.setCertificateWithKeyPath((ReferenceExpression) certificateWithKeyPathValue);
        var pfxPathValue = map.get("PfxPath");
        value.setPfxPath((ReferenceExpression) pfxPathValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("CertificatePath", AspireClient.serializeValue(certificatePath));
        map.put("KeyPath", AspireClient.serializeValue(keyPath));
        map.put("CertificateWithKeyPath", AspireClient.serializeValue(certificateWithKeyPath));
        map.put("PfxPath", AspireClient.serializeValue(pfxPath));
        return map;
    }
}
