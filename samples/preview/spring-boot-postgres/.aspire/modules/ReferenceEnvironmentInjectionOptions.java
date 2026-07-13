// ReferenceEnvironmentInjectionOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** ReferenceEnvironmentInjectionOptions DTO. */
public class ReferenceEnvironmentInjectionOptions implements JsonSerializable {
    private boolean connectionString;
    private boolean connectionProperties;
    private boolean serviceDiscovery;
    private boolean endpoints;

    public boolean getConnectionString() { return connectionString; }
    public void setConnectionString(boolean value) { this.connectionString = value; }
    public boolean getConnectionProperties() { return connectionProperties; }
    public void setConnectionProperties(boolean value) { this.connectionProperties = value; }
    public boolean getServiceDiscovery() { return serviceDiscovery; }
    public void setServiceDiscovery(boolean value) { this.serviceDiscovery = value; }
    public boolean getEndpoints() { return endpoints; }
    public void setEndpoints(boolean value) { this.endpoints = value; }

    @SuppressWarnings("unchecked")
    public static ReferenceEnvironmentInjectionOptions fromMap(Map<String, Object> map) {
        var value = new ReferenceEnvironmentInjectionOptions();
        var connectionStringValue = map.get("ConnectionString");
        value.setConnectionString((Boolean) connectionStringValue);
        var connectionPropertiesValue = map.get("ConnectionProperties");
        value.setConnectionProperties((Boolean) connectionPropertiesValue);
        var serviceDiscoveryValue = map.get("ServiceDiscovery");
        value.setServiceDiscovery((Boolean) serviceDiscoveryValue);
        var endpointsValue = map.get("Endpoints");
        value.setEndpoints((Boolean) endpointsValue);
        return value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ConnectionString", AspireClient.serializeValue(connectionString));
        map.put("ConnectionProperties", AspireClient.serializeValue(connectionProperties));
        map.put("ServiceDiscovery", AspireClient.serializeValue(serviceDiscovery));
        map.put("Endpoints", AspireClient.serializeValue(endpoints));
        return map;
    }
}
