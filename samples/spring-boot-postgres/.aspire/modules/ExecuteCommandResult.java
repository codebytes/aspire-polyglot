// ExecuteCommandResult.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ExecuteCommandResult DTO. */
public class ExecuteCommandResult implements JsonSerializable {
    private boolean success;
    private Boolean canceled;
    private String errorMessage;
    private String message;
    private CommandResultData data;

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }
    public Boolean getCanceled() { return canceled; }
    public void setCanceled(Boolean value) { this.canceled = value; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String value) { this.errorMessage = value; }
    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
    public CommandResultData getData() { return data; }
    public void setData(CommandResultData value) { this.data = value; }

    @SuppressWarnings("unchecked")
    public static ExecuteCommandResult fromMap(Map<String, Object> map) {
        var value = new ExecuteCommandResult();
        var successValue = map.get("Success");
        value.setSuccess((Boolean) successValue);
        var canceledValue = map.get("Canceled");
        value.setCanceled(canceledValue == null ? null : (Boolean) canceledValue);
        var errorMessageValue = map.get("ErrorMessage");
        value.setErrorMessage(errorMessageValue == null ? null : (String) errorMessageValue);
        var messageValue = map.get("Message");
        value.setMessage(messageValue == null ? null : (String) messageValue);
        var dataValue = map.get("Data");
        value.setData(dataValue == null ? null : CommandResultData.fromMap((Map<String, Object>) dataValue));
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Success", AspireClient.serializeValue(success));
        map.put("Canceled", AspireClient.serializeValue(canceled));
        map.put("ErrorMessage", AspireClient.serializeValue(errorMessage));
        map.put("Message", AspireClient.serializeValue(message));
        map.put("Data", AspireClient.serializeValue(data));
        return map;
    }
}
