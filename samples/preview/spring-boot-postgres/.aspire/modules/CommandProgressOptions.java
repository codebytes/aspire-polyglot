// CommandProgressOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** CommandProgressOptions DTO. */
public class CommandProgressOptions implements JsonSerializable {
    private String message;
    private String title;
    private boolean hideCancelButton;

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }
    public boolean getHideCancelButton() { return hideCancelButton; }
    public void setHideCancelButton(boolean value) { this.hideCancelButton = value; }

    @SuppressWarnings("unchecked")
    public static CommandProgressOptions fromMap(Map<String, Object> map) {
        var value = new CommandProgressOptions();
        var messageValue = map.get("Message");
        value.setMessage(messageValue == null ? null : (String) messageValue);
        var titleValue = map.get("Title");
        value.setTitle(titleValue == null ? null : (String) titleValue);
        var hideCancelButtonValue = map.get("HideCancelButton");
        value.setHideCancelButton((Boolean) hideCancelButtonValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Message", AspireClient.serializeValue(message));
        map.put("Title", AspireClient.serializeValue(title));
        map.put("HideCancelButton", AspireClient.serializeValue(hideCancelButton));
        return map;
    }
}
