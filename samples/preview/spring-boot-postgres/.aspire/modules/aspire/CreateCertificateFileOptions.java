// CreateCertificateFileOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for CreateCertificateFile. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class CreateCertificateFileOptions {
    private String contents;
    private String sourcePath;
    private Number owner;
    private Number group;
    private Number mode;
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

    public Number getOwner() { return owner; }
    public CreateCertificateFileOptions owner(Number value) {
        this.owner = value;
        return this;
    }

    public Number getGroup() { return group; }
    public CreateCertificateFileOptions group(Number value) {
        this.group = value;
        return this;
    }

    public Number getMode() { return mode; }
    public CreateCertificateFileOptions mode(Number value) {
        this.mode = value;
        return this;
    }

    public Boolean getContinueOnError() { return continueOnError; }
    public CreateCertificateFileOptions continueOnError(Boolean value) {
        this.continueOnError = value;
        return this;
    }

}
