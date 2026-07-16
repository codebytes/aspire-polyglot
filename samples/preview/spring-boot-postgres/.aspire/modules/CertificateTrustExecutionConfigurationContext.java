// CertificateTrustExecutionConfigurationContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CertificateTrustExecutionConfigurationContext DTO. */
public class CertificateTrustExecutionConfigurationContext implements JsonSerializable {
    private ReferenceExpression certificateBundlePath;
    private ReferenceExpression certificateDirectoriesPath;
    private String rootCertificatesPath;
    private Boolean isContainer;

    public ReferenceExpression getCertificateBundlePath() { return certificateBundlePath; }
    public void setCertificateBundlePath(ReferenceExpression value) { this.certificateBundlePath = value; }
    public ReferenceExpression getCertificateDirectoriesPath() { return certificateDirectoriesPath; }
    public void setCertificateDirectoriesPath(ReferenceExpression value) { this.certificateDirectoriesPath = value; }
    public String getRootCertificatesPath() { return rootCertificatesPath; }
    public void setRootCertificatesPath(String value) { this.rootCertificatesPath = value; }
    public Boolean getIsContainer() { return isContainer; }
    public void setIsContainer(Boolean value) { this.isContainer = value; }

    @SuppressWarnings("unchecked")
    public static CertificateTrustExecutionConfigurationContext fromMap(Map<String, Object> map) {
        var value = new CertificateTrustExecutionConfigurationContext();
        var certificateBundlePathValue = map.get("CertificateBundlePath");
        value.setCertificateBundlePath((ReferenceExpression) certificateBundlePathValue);
        var certificateDirectoriesPathValue = map.get("CertificateDirectoriesPath");
        value.setCertificateDirectoriesPath((ReferenceExpression) certificateDirectoriesPathValue);
        var rootCertificatesPathValue = map.get("RootCertificatesPath");
        value.setRootCertificatesPath((String) rootCertificatesPathValue);
        var isContainerValue = map.get("IsContainer");
        value.setIsContainer(isContainerValue == null ? null : (Boolean) isContainerValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("CertificateBundlePath", AspireClient.serializeValue(certificateBundlePath));
        map.put("CertificateDirectoriesPath", AspireClient.serializeValue(certificateDirectoriesPath));
        map.put("RootCertificatesPath", AspireClient.serializeValue(rootCertificatesPath));
        map.put("IsContainer", AspireClient.serializeValue(isContainer));
        return map;
    }
}
