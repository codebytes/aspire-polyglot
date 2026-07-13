// WithPipelineStepFactoryOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for WithPipelineStepFactory. */
public final class WithPipelineStepFactoryOptions {
    private String[] dependsOn;
    private String[] requiredBy;
    private String[] tags;
    private String description;

    public String[] getDependsOn() { return dependsOn; }
    public WithPipelineStepFactoryOptions dependsOn(String[] value) {
        this.dependsOn = value;
        return this;
    }

    public String[] getRequiredBy() { return requiredBy; }
    public WithPipelineStepFactoryOptions requiredBy(String[] value) {
        this.requiredBy = value;
        return this;
    }

    public String[] getTags() { return tags; }
    public WithPipelineStepFactoryOptions tags(String[] value) {
        this.tags = value;
        return this;
    }

    public String getDescription() { return description; }
    public WithPipelineStepFactoryOptions description(String value) {
        this.description = value;
        return this;
    }

}
