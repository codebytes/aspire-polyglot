// CreateChoiceInputOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Options for CreateChoiceInput. */
public final class CreateChoiceInputOptions {
    private InteractionChoiceOption[] choices;
    private CreateInteractionInputOptions options;

    public InteractionChoiceOption[] getChoices() { return choices; }
    public CreateChoiceInputOptions choices(InteractionChoiceOption[] value) {
        this.choices = value;
        return this;
    }

    public CreateInteractionInputOptions getOptions() { return options; }
    public CreateChoiceInputOptions options(CreateInteractionInputOptions value) {
        this.options = value;
        return this;
    }

}
