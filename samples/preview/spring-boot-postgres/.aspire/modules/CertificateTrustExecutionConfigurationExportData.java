// CertificateTrustExecutionConfigurationExportData.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CertificateTrustExecutionConfigurationExportData DTO. */
public class CertificateTrustExecutionConfigurationExportData implements JsonSerializable {
    private CertificateTrustScope scope;
    private String[] certificateSubjects;
    private String[] customBundlePaths;

    public CertificateTrustScope getScope() { return scope; }
    public void setScope(CertificateTrustScope value) { this.scope = value; }
    public String[] getCertificateSubjects() { return certificateSubjects; }
    public void setCertificateSubjects(String[] value) { this.certificateSubjects = value; }
    public String[] getCustomBundlePaths() { return customBundlePaths; }
    public void setCustomBundlePaths(String[] value) { this.customBundlePaths = value; }

    @SuppressWarnings("unchecked")
    public static CertificateTrustExecutionConfigurationExportData fromMap(Map<String, Object> map) {
        var value = new CertificateTrustExecutionConfigurationExportData();
        var scopeValue = map.get("Scope");
        value.setScope(CertificateTrustScope.fromValue((String) scopeValue));
        var certificateSubjectsValue = map.get("CertificateSubjects");
        value.setCertificateSubjects((String[]) certificateSubjectsValue);
        var customBundlePathsValue = map.get("CustomBundlePaths");
        value.setCustomBundlePaths((String[]) customBundlePathsValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Scope", AspireClient.serializeValue(scope));
        map.put("CertificateSubjects", AspireClient.serializeValue(certificateSubjects));
        map.put("CustomBundlePaths", AspireClient.serializeValue(customBundlePaths));
        return map;
    }
}
