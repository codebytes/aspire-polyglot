// WithContainerCertificatePathsOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for WithContainerCertificatePaths. */
public final class WithContainerCertificatePathsOptions {
    private String customCertificatesDestination;
    private String[] defaultCertificateBundlePaths;
    private String[] defaultCertificateDirectoryPaths;

    public String getCustomCertificatesDestination() { return customCertificatesDestination; }
    public WithContainerCertificatePathsOptions customCertificatesDestination(String value) {
        this.customCertificatesDestination = value;
        return this;
    }

    public String[] getDefaultCertificateBundlePaths() { return defaultCertificateBundlePaths; }
    public WithContainerCertificatePathsOptions defaultCertificateBundlePaths(String[] value) {
        this.defaultCertificateBundlePaths = value;
        return this;
    }

    public String[] getDefaultCertificateDirectoryPaths() { return defaultCertificateDirectoryPaths; }
    public WithContainerCertificatePathsOptions defaultCertificateDirectoryPaths(String[] value) {
        this.defaultCertificateDirectoryPaths = value;
        return this;
    }

}
