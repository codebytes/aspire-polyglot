// IExecutionConfigurationResult.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.IExecutionConfigurationResult. */
public class IExecutionConfigurationResult extends HandleWrapperBase {
    IExecutionConfigurationResult(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets certificate trust execution-configuration data when present. */
    public CertificateTrustExecutionConfigurationExportData getCertificateTrustData() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("configuration", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getCertificateTrustData", reqArgs);
        return CertificateTrustExecutionConfigurationExportData.fromMap((Map<String, Object>) result);
    }

    /** Gets HTTPS certificate execution-configuration data when present. */
    public HttpsCertificateExecutionConfigurationExportData getHttpsCertificateData() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("configuration", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getHttpsCertificateData", reqArgs);
        return HttpsCertificateExecutionConfigurationExportData.fromMap((Map<String, Object>) result);
    }

}
