// AddContainerOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** AddContainerOptions DTO. */
public class AddContainerOptions implements JsonSerializable {
    private String image;
    private String tag;

    public String getImage() { return image; }
    public void setImage(String value) { this.image = value; }
    public String getTag() { return tag; }
    public void setTag(String value) { this.tag = value; }

    @SuppressWarnings("unchecked")
    public static AddContainerOptions fromMap(Map<String, Object> map) {
        var value = new AddContainerOptions();
        var imageValue = map.get("Image");
        value.setImage((String) imageValue);
        var tagValue = map.get("Tag");
        value.setTag(tagValue == null ? null : (String) tagValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Image", AspireClient.serializeValue(image));
        map.put("Tag", AspireClient.serializeValue(tag));
        return map;
    }
}
