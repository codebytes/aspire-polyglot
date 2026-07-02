// IExecutionConfigurationBuilder.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.IExecutionConfigurationBuilder. */
public class IExecutionConfigurationBuilder extends HandleWrapperBase {
    IExecutionConfigurationBuilder(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Builds the execution configuration for the specified builder. */
    public IExecutionConfigurationResult build(DistributedApplicationExecutionContext executionContext, BuildOptions options) {
        var resourceLogger = options == null ? null : options.getResourceLogger();
        var cancellationToken = options == null ? null : options.getCancellationToken();
        return buildImpl(executionContext, resourceLogger, cancellationToken);
    }

    public IExecutionConfigurationResult build(DistributedApplicationExecutionContext executionContext) {
        return build(executionContext, null);
    }

    /** Builds the execution configuration for the specified builder. */
    private IExecutionConfigurationResult buildImpl(DistributedApplicationExecutionContext executionContext, ILogger resourceLogger, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        reqArgs.put("executionContext", AspireClient.serializeValue(executionContext));
        if (resourceLogger != null) {
            reqArgs.put("resourceLogger", AspireClient.serializeValue(resourceLogger));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/buildExecutionConfiguration", reqArgs);
        return (IExecutionConfigurationResult) result;
    }

    /** Adds an HTTPS certificate configuration gatherer using certificate metadata instead of a raw X509 certificate. */
    public IExecutionConfigurationBuilder withHttpsCertificateConfig(AspireFunc1<HttpsCertificateInfo, HttpsCertificateExecutionConfigurationContext> configContextFactory) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var configContextFactoryId = getClient().registerCallback(args -> {
            var arg = HttpsCertificateInfo.fromMap((Map<String, Object>) args[0]);
            return AspireClient.awaitValue(configContextFactory.invoke(arg));
        });
        if (configContextFactoryId != null) {
            reqArgs.put("configContextFactory", configContextFactoryId);
        }
        var result = getClient().invokeCapability("Aspire.Hosting/withHttpsCertificateConfigExport", reqArgs);
        return (IExecutionConfigurationBuilder) result;
    }

    /** Adds a command line arguments configuration gatherer to the builder. */
    public IExecutionConfigurationBuilder withArgumentsConfig() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/withArgumentsConfig", reqArgs);
        return (IExecutionConfigurationBuilder) result;
    }

    /** Adds an environment variables configuration gatherer to the builder. */
    public IExecutionConfigurationBuilder withEnvironmentVariablesConfig() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/withEnvironmentVariablesConfig", reqArgs);
        return (IExecutionConfigurationBuilder) result;
    }

    /** Adds a certificate trust configuration gatherer to the builder. */
    public IExecutionConfigurationBuilder withCertificateTrustConfig(AspireFunc1<CertificateTrustScope, CertificateTrustExecutionConfigurationContext> configContextFactory) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("builder", AspireClient.serializeValue(getHandle()));
        var configContextFactoryId = getClient().registerCallback(args -> {
            var arg = CertificateTrustScope.fromValue((String) args[0]);
            return AspireClient.awaitValue(configContextFactory.invoke(arg));
        });
        if (configContextFactoryId != null) {
            reqArgs.put("configContextFactory", configContextFactoryId);
        }
        var result = getClient().invokeCapability("Aspire.Hosting/withCertificateTrustConfig", reqArgs);
        return (IExecutionConfigurationBuilder) result;
    }

}
