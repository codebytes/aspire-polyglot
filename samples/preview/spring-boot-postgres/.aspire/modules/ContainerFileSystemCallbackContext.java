// ContainerFileSystemCallbackContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerFileSystemCallbackContext. */
public class ContainerFileSystemCallbackContext extends HandleWrapperBase {
    ContainerFileSystemCallbackContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** A `IServiceProvider` that can be used to resolve services in the callback. */
    public IServiceProvider services() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerFileSystemCallbackContext.services", reqArgs);
        return (IServiceProvider) result;
    }

    /** The app model resource the callback is associated with. */
    public IResource model() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/ContainerFileSystemCallbackContext.model", reqArgs);
        return (IResource) result;
    }

    /** Creates a container file entry with inline contents or a host source path. */
    public ContainerFileSystemItem createFile(String name, CreateFileOptions optionsBag) {
        var contents = optionsBag == null ? null : optionsBag.getContents();
        var sourcePath = optionsBag == null ? null : optionsBag.getSourcePath();
        var owner = optionsBag == null ? null : optionsBag.getOwner();
        var group = optionsBag == null ? null : optionsBag.getGroup();
        var mode = optionsBag == null ? null : optionsBag.getMode();
        var continueOnError = optionsBag == null ? null : optionsBag.getContinueOnError();
        return createFileImpl(name, contents, sourcePath, owner, group, mode, continueOnError);
    }

    public ContainerFileSystemItem createFile(String name) {
        return createFile(name, null);
    }

    /** Creates a container file entry with inline contents or a host source path. */
    private ContainerFileSystemItem createFileImpl(String name, String contents, String sourcePath, Double owner, Double group, Double mode, Boolean continueOnError) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (contents != null) {
            reqArgs.put("contents", AspireClient.serializeValue(contents));
        }
        if (sourcePath != null) {
            reqArgs.put("sourcePath", AspireClient.serializeValue(sourcePath));
        }
        if (owner != null) {
            reqArgs.put("owner", AspireClient.serializeValue(owner));
        }
        if (group != null) {
            reqArgs.put("group", AspireClient.serializeValue(group));
        }
        if (mode != null) {
            reqArgs.put("mode", AspireClient.serializeValue(mode));
        }
        if (continueOnError != null) {
            reqArgs.put("continueOnError", AspireClient.serializeValue(continueOnError));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createFile", reqArgs);
        return (ContainerFileSystemItem) result;
    }

    /** Creates a PEM container certificate file entry with the OpenSSL subject-hash symlink. */
    public ContainerFileSystemItem createCertificateFile(String name, CreateCertificateFileOptions optionsBag) {
        var contents = optionsBag == null ? null : optionsBag.getContents();
        var sourcePath = optionsBag == null ? null : optionsBag.getSourcePath();
        var owner = optionsBag == null ? null : optionsBag.getOwner();
        var group = optionsBag == null ? null : optionsBag.getGroup();
        var mode = optionsBag == null ? null : optionsBag.getMode();
        var continueOnError = optionsBag == null ? null : optionsBag.getContinueOnError();
        return createCertificateFileImpl(name, contents, sourcePath, owner, group, mode, continueOnError);
    }

    public ContainerFileSystemItem createCertificateFile(String name) {
        return createCertificateFile(name, null);
    }

    /** Creates a PEM container certificate file entry with the OpenSSL subject-hash symlink. */
    private ContainerFileSystemItem createCertificateFileImpl(String name, String contents, String sourcePath, Double owner, Double group, Double mode, Boolean continueOnError) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (contents != null) {
            reqArgs.put("contents", AspireClient.serializeValue(contents));
        }
        if (sourcePath != null) {
            reqArgs.put("sourcePath", AspireClient.serializeValue(sourcePath));
        }
        if (owner != null) {
            reqArgs.put("owner", AspireClient.serializeValue(owner));
        }
        if (group != null) {
            reqArgs.put("group", AspireClient.serializeValue(group));
        }
        if (mode != null) {
            reqArgs.put("mode", AspireClient.serializeValue(mode));
        }
        if (continueOnError != null) {
            reqArgs.put("continueOnError", AspireClient.serializeValue(continueOnError));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createCertificateFile", reqArgs);
        return (ContainerFileSystemItem) result;
    }

    /** Creates a container directory entry containing the specified child entries. */
    public ContainerFileSystemItem createDirectory(String name, ContainerFileSystemItem[] entries, CreateDirectoryOptions optionsBag) {
        var owner = optionsBag == null ? null : optionsBag.getOwner();
        var group = optionsBag == null ? null : optionsBag.getGroup();
        var mode = optionsBag == null ? null : optionsBag.getMode();
        return createDirectoryImpl(name, entries, owner, group, mode);
    }

    public ContainerFileSystemItem createDirectory(String name, ContainerFileSystemItem[] entries) {
        return createDirectory(name, entries, null);
    }

    /** Creates a container directory entry containing the specified child entries. */
    private ContainerFileSystemItem createDirectoryImpl(String name, ContainerFileSystemItem[] entries, Double owner, Double group, Double mode) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        reqArgs.put("entries", AspireClient.serializeValue(entries));
        if (owner != null) {
            reqArgs.put("owner", AspireClient.serializeValue(owner));
        }
        if (group != null) {
            reqArgs.put("group", AspireClient.serializeValue(group));
        }
        if (mode != null) {
            reqArgs.put("mode", AspireClient.serializeValue(mode));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createDirectory", reqArgs);
        return (ContainerFileSystemItem) result;
    }

}
