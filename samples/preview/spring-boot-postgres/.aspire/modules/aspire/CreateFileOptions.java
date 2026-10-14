// CreateFileOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for CreateFile. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class CreateFileOptions {
    private String contents;
    private String sourcePath;
    private Number owner;
    private Number group;
    private Number mode;
    private Boolean continueOnError;

    public String getContents() { return contents; }
    public CreateFileOptions contents(String value) {
        this.contents = value;
        return this;
    }

    public String getSourcePath() { return sourcePath; }
    public CreateFileOptions sourcePath(String value) {
        this.sourcePath = value;
        return this;
    }

    public Number getOwner() { return owner; }
    public CreateFileOptions owner(Number value) {
        this.owner = value;
        return this;
    }

    public Number getGroup() { return group; }
    public CreateFileOptions group(Number value) {
        this.group = value;
        return this;
    }

    public Number getMode() { return mode; }
    public CreateFileOptions mode(Number value) {
        this.mode = value;
        return this;
    }

    public Boolean getContinueOnError() { return continueOnError; }
    public CreateFileOptions continueOnError(Boolean value) {
        this.continueOnError = value;
        return this;
    }

}
