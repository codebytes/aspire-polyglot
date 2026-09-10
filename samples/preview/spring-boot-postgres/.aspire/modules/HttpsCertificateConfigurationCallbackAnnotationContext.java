// HttpsCertificateConfigurationCallbackAnnotationContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.HttpsCertificateConfigurationCallbackAnnotationContext. */
public class HttpsCertificateConfigurationCallbackAnnotationContext extends HandleWrapperBase {
    HttpsCertificateConfigurationCallbackAnnotationContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the `DistributedApplicationExecutionContext` for this session. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

    /** Gets the resource to which the annotation is applied. */
    public IResource resource() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.resource", reqArgs);
        return (IResource) result;
    }

    /** A value provider that will resolve to a path to the certificate file. */
    public ReferenceExpression certificatePath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.certificatePath", reqArgs);
        return (ReferenceExpression) result;
    }

    /** A value provider that will resolve to a path to the private key for the certificate. */
    public ReferenceExpression keyPath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.keyPath", reqArgs);
        return (ReferenceExpression) result;
    }

    /** A value provider that will resolve to a path to the certificate and key concatenated together in PEM format. */
    public ReferenceExpression certificateWithKeyPath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.certificateWithKeyPath", reqArgs);
        return (ReferenceExpression) result;
    }

    /** A value provider that will resolve to a path to a PFX file for the key pair. */
    public ReferenceExpression pfxPath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.pfxPath", reqArgs);
        return (ReferenceExpression) result;
    }

    /** Gets the `CancellationToken` that can be used to cancel the operation. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

    /** Gets the editor used to manipulate the command-line arguments in polyglot callbacks. */
    public CommandLineArgsEditor arguments() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.arguments", reqArgs);
        return (CommandLineArgsEditor) result;
    }

    /** Gets the editor used to set environment variables in polyglot callbacks. */
    public EnvironmentEditor environment() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/HttpsCertificateConfigurationCallbackAnnotationContext.environment", reqArgs);
        return (EnvironmentEditor) result;
    }

}
