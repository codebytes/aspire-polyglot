// GenerateParameterDefault.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** GenerateParameterDefault DTO. */
public class GenerateParameterDefault implements JsonSerializable {
    private double minLength;
    private boolean lower;
    private boolean upper;
    private boolean numeric;
    private boolean special;
    private double minLower;
    private double minUpper;
    private double minNumeric;
    private double minSpecial;

    public double getMinLength() { return minLength; }
    public void setMinLength(double value) { this.minLength = value; }
    public boolean getLower() { return lower; }
    public void setLower(boolean value) { this.lower = value; }
    public boolean getUpper() { return upper; }
    public void setUpper(boolean value) { this.upper = value; }
    public boolean getNumeric() { return numeric; }
    public void setNumeric(boolean value) { this.numeric = value; }
    public boolean getSpecial() { return special; }
    public void setSpecial(boolean value) { this.special = value; }
    public double getMinLower() { return minLower; }
    public void setMinLower(double value) { this.minLower = value; }
    public double getMinUpper() { return minUpper; }
    public void setMinUpper(double value) { this.minUpper = value; }
    public double getMinNumeric() { return minNumeric; }
    public void setMinNumeric(double value) { this.minNumeric = value; }
    public double getMinSpecial() { return minSpecial; }
    public void setMinSpecial(double value) { this.minSpecial = value; }

    @SuppressWarnings("unchecked")
    public static GenerateParameterDefault fromMap(Map<String, Object> map) {
        var value = new GenerateParameterDefault();
        var minLengthValue = map.get("MinLength");
        value.setMinLength(((Number) minLengthValue).doubleValue());
        var lowerValue = map.get("Lower");
        value.setLower((Boolean) lowerValue);
        var upperValue = map.get("Upper");
        value.setUpper((Boolean) upperValue);
        var numericValue = map.get("Numeric");
        value.setNumeric((Boolean) numericValue);
        var specialValue = map.get("Special");
        value.setSpecial((Boolean) specialValue);
        var minLowerValue = map.get("MinLower");
        value.setMinLower(((Number) minLowerValue).doubleValue());
        var minUpperValue = map.get("MinUpper");
        value.setMinUpper(((Number) minUpperValue).doubleValue());
        var minNumericValue = map.get("MinNumeric");
        value.setMinNumeric(((Number) minNumericValue).doubleValue());
        var minSpecialValue = map.get("MinSpecial");
        value.setMinSpecial(((Number) minSpecialValue).doubleValue());
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("MinLength", AspireClient.serializeValue(minLength));
        map.put("Lower", AspireClient.serializeValue(lower));
        map.put("Upper", AspireClient.serializeValue(upper));
        map.put("Numeric", AspireClient.serializeValue(numeric));
        map.put("Special", AspireClient.serializeValue(special));
        map.put("MinLower", AspireClient.serializeValue(minLower));
        map.put("MinUpper", AspireClient.serializeValue(minUpper));
        map.put("MinNumeric", AspireClient.serializeValue(minNumeric));
        map.put("MinSpecial", AspireClient.serializeValue(minSpecial));
        return map;
    }
}
