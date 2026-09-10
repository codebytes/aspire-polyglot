// CreateFileOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for CreateFile. */
public final class CreateFileOptions {
    private String contents;
    private String sourcePath;
    private Double owner;
    private Double group;
    private Double mode;
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

    public Double getOwner() { return owner; }
    public CreateFileOptions owner(Double value) {
        this.owner = value;
        return this;
    }

    public Double getGroup() { return group; }
    public CreateFileOptions group(Double value) {
        this.group = value;
        return this;
    }

    public Double getMode() { return mode; }
    public CreateFileOptions mode(Double value) {
        this.mode = value;
        return this;
    }

    public Boolean getContinueOnError() { return continueOnError; }
    public CreateFileOptions continueOnError(Boolean value) {
        this.continueOnError = value;
        return this;
    }

}
