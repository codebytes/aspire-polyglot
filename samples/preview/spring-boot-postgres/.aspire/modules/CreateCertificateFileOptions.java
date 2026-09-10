// CreateCertificateFileOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for CreateCertificateFile. */
public final class CreateCertificateFileOptions {
    private String contents;
    private String sourcePath;
    private Double owner;
    private Double group;
    private Double mode;
    private Boolean continueOnError;

    public String getContents() { return contents; }
    public CreateCertificateFileOptions contents(String value) {
        this.contents = value;
        return this;
    }

    public String getSourcePath() { return sourcePath; }
    public CreateCertificateFileOptions sourcePath(String value) {
        this.sourcePath = value;
        return this;
    }

    public Double getOwner() { return owner; }
    public CreateCertificateFileOptions owner(Double value) {
        this.owner = value;
        return this;
    }

    public Double getGroup() { return group; }
    public CreateCertificateFileOptions group(Double value) {
        this.group = value;
        return this;
    }

    public Double getMode() { return mode; }
    public CreateCertificateFileOptions mode(Double value) {
        this.mode = value;
        return this;
    }

    public Boolean getContinueOnError() { return continueOnError; }
    public CreateCertificateFileOptions continueOnError(Boolean value) {
        this.continueOnError = value;
        return this;
    }

}
