// IInteractionService.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.IInteractionService. */
public class IInteractionService extends HandleWrapperBase {
    IInteractionService(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets a value indicating whether the interaction service is available to prompt the user. */
    public boolean isAvailable() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/isAvailable", reqArgs);
        return (Boolean) result;
    }

    /** Prompts the user for confirmation with an OK/Cancel dialog. */
    public BoolInteractionResult promptConfirmation(String title, String message, PromptConfirmationOptions optionsBag) {
        var options = optionsBag == null ? null : optionsBag.getOptions();
        var cancellationToken = optionsBag == null ? null : optionsBag.getCancellationToken();
        return promptConfirmationImpl(title, message, options, cancellationToken);
    }

    public BoolInteractionResult promptConfirmation(String title, String message) {
        return promptConfirmation(title, message, null);
    }

    /** Prompts the user for confirmation with an OK/Cancel dialog. */
    private BoolInteractionResult promptConfirmationImpl(String title, String message, InteractionMessageBoxOptions options, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("title", AspireClient.serializeValue(title));
        reqArgs.put("message", AspireClient.serializeValue(message));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/promptConfirmation", reqArgs);
        return BoolInteractionResult.fromMap((Map<String, Object>) result);
    }

    /** Prompts the user with a message box dialog. */
    public BoolInteractionResult promptMessageBox(String title, String message, PromptMessageBoxOptions optionsBag) {
        var options = optionsBag == null ? null : optionsBag.getOptions();
        var cancellationToken = optionsBag == null ? null : optionsBag.getCancellationToken();
        return promptMessageBoxImpl(title, message, options, cancellationToken);
    }

    public BoolInteractionResult promptMessageBox(String title, String message) {
        return promptMessageBox(title, message, null);
    }

    /** Prompts the user with a message box dialog. */
    private BoolInteractionResult promptMessageBoxImpl(String title, String message, InteractionMessageBoxOptions options, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("title", AspireClient.serializeValue(title));
        reqArgs.put("message", AspireClient.serializeValue(message));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/promptMessageBox", reqArgs);
        return BoolInteractionResult.fromMap((Map<String, Object>) result);
    }

    /** Prompts the user with a notification. */
    public BoolInteractionResult promptNotification(String title, String message, PromptNotificationOptions optionsBag) {
        var options = optionsBag == null ? null : optionsBag.getOptions();
        var cancellationToken = optionsBag == null ? null : optionsBag.getCancellationToken();
        return promptNotificationImpl(title, message, options, cancellationToken);
    }

    public BoolInteractionResult promptNotification(String title, String message) {
        return promptNotification(title, message, null);
    }

    /** Prompts the user with a notification. */
    private BoolInteractionResult promptNotificationImpl(String title, String message, InteractionNotificationOptions options, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("title", AspireClient.serializeValue(title));
        reqArgs.put("message", AspireClient.serializeValue(message));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/promptNotification", reqArgs);
        return BoolInteractionResult.fromMap((Map<String, Object>) result);
    }

    /** Displays a progress dialog with an indeterminate progress indicator. */
    public BoolInteractionResult promptProgress(String message, PromptProgressOptions optionsBag) {
        var options = optionsBag == null ? null : optionsBag.getOptions();
        var cancellationToken = optionsBag == null ? null : optionsBag.getCancellationToken();
        return promptProgressImpl(message, options, cancellationToken);
    }

    public BoolInteractionResult promptProgress(String message) {
        return promptProgress(message, null);
    }

    /** Displays a progress dialog with an indeterminate progress indicator. */
    private BoolInteractionResult promptProgressImpl(String message, InteractionProgressOptions options, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("message", AspireClient.serializeValue(message));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/promptProgress", reqArgs);
        return BoolInteractionResult.fromMap((Map<String, Object>) result);
    }

    /** Prompts the user for a single input. */
    public InputInteractionResult promptInput(String title, String message, InteractionInputBuilder input, PromptInputOptions optionsBag) {
        var options = optionsBag == null ? null : optionsBag.getOptions();
        var cancellationToken = optionsBag == null ? null : optionsBag.getCancellationToken();
        return promptInputImpl(title, message, input, options, cancellationToken);
    }

    public InputInteractionResult promptInput(String title, String message, HandleWrapperBase input, PromptInputOptions options) {
        return promptInput(title, message, new InteractionInputBuilder(input.getHandle(), input.getClient()), options);
    }

    public InputInteractionResult promptInput(String title, String message, InteractionInputBuilder input) {
        return promptInput(title, message, input, null);
    }

    public InputInteractionResult promptInput(String title, String message, HandleWrapperBase input) {
        return promptInput(title, message, new InteractionInputBuilder(input.getHandle(), input.getClient()));
    }

    /** Prompts the user for a single input. */
    private InputInteractionResult promptInputImpl(String title, String message, InteractionInputBuilder input, InteractionInputsDialogOptions options, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("title", AspireClient.serializeValue(title));
        reqArgs.put("message", AspireClient.serializeValue(message));
        reqArgs.put("input", AspireClient.serializeValue(input));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/promptInput", reqArgs);
        return InputInteractionResult.fromMap((Map<String, Object>) result);
    }

    /** Prompts the user for multiple inputs. */
    public InputsInteractionResult promptInputs(String title, String message, InteractionInputBuilder[] inputs, PromptInputsOptions optionsBag) {
        var options = optionsBag == null ? null : optionsBag.getOptions();
        var cancellationToken = optionsBag == null ? null : optionsBag.getCancellationToken();
        return promptInputsImpl(title, message, inputs, options, cancellationToken);
    }

    public InputsInteractionResult promptInputs(String title, String message, InteractionInputBuilder[] inputs) {
        return promptInputs(title, message, inputs, null);
    }

    /** Prompts the user for multiple inputs. */
    private InputsInteractionResult promptInputsImpl(String title, String message, InteractionInputBuilder[] inputs, InteractionInputsDialogOptions options, CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("title", AspireClient.serializeValue(title));
        reqArgs.put("message", AspireClient.serializeValue(message));
        reqArgs.put("inputs", AspireClient.serializeValue(inputs));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/promptInputs", reqArgs);
        return (InputsInteractionResult) result;
    }

    public InteractionInputBuilder createTextInput(String name) {
        return createTextInput(name, null);
    }

    /** Creates a single-line text input. */
    public InteractionInputBuilder createTextInput(String name, CreateInteractionInputOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createTextInput", reqArgs);
        return (InteractionInputBuilder) result;
    }

    public InteractionInputBuilder createSecretInput(String name) {
        return createSecretInput(name, null);
    }

    /** Creates a secret (masked) text input. */
    public InteractionInputBuilder createSecretInput(String name, CreateInteractionInputOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createSecretInput", reqArgs);
        return (InteractionInputBuilder) result;
    }

    public InteractionInputBuilder createBooleanInput(String name) {
        return createBooleanInput(name, null);
    }

    /** Creates a boolean (checkbox) input. */
    public InteractionInputBuilder createBooleanInput(String name, CreateInteractionInputOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createBooleanInput", reqArgs);
        return (InteractionInputBuilder) result;
    }

    public InteractionInputBuilder createNumberInput(String name) {
        return createNumberInput(name, null);
    }

    /** Creates a numeric input. */
    public InteractionInputBuilder createNumberInput(String name, CreateInteractionInputOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createNumberInput", reqArgs);
        return (InteractionInputBuilder) result;
    }

    public InteractionInputBuilder createFileInput(String name) {
        return createFileInput(name, null);
    }

    /** Creates a file input. */
    public InteractionInputBuilder createFileInput(String name, CreateInteractionInputOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createFileInput", reqArgs);
        return (InteractionInputBuilder) result;
    }

    /** Creates a choice input that selects from a list of options. */
    public InteractionInputBuilder createChoiceInput(String name, CreateChoiceInputOptions optionsBag) {
        var choices = optionsBag == null ? null : optionsBag.getChoices();
        var options = optionsBag == null ? null : optionsBag.getOptions();
        return createChoiceInputImpl(name, choices, options);
    }

    public InteractionInputBuilder createChoiceInput(String name) {
        return createChoiceInput(name, null);
    }

    /** Creates a choice input that selects from a list of options. */
    private InteractionInputBuilder createChoiceInputImpl(String name, InteractionChoiceOption[] choices, CreateInteractionInputOptions options) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("interactionService", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        if (choices != null) {
            reqArgs.put("choices", AspireClient.serializeValue(choices));
        }
        if (options != null) {
            reqArgs.put("options", AspireClient.serializeValue(options));
        }
        var result = getClient().invokeCapability("Aspire.Hosting/createChoiceInput", reqArgs);
        return (InteractionInputBuilder) result;
    }

}
