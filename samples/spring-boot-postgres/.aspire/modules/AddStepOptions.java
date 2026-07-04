// AddStepOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for AddStep. */
public final class AddStepOptions {
    private String[] dependsOn;
    private String[] requiredBy;

    public String[] getDependsOn() { return dependsOn; }
    public AddStepOptions dependsOn(String[] value) {
        this.dependsOn = value;
        return this;
    }

    public String[] getRequiredBy() { return requiredBy; }
    public AddStepOptions requiredBy(String[] value) {
        this.requiredBy = value;
        return this;
    }

}
