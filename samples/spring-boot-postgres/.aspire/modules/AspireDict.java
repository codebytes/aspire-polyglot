// AspireDict.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;

/**
 * AspireDict is a handle-backed dictionary with lazy handle resolution.
 */
public class AspireDict<K, V> extends HandleWrapperBase {
    private final String getterCapabilityId;
    private Handle resolvedHandle;

    AspireDict(Handle handle, AspireClient client) {
        super(handle, client);
        this.getterCapabilityId = null;
        this.resolvedHandle = handle;
    }

    AspireDict(Handle contextHandle, AspireClient client, String getterCapabilityId) {
        super(contextHandle, client);
        this.getterCapabilityId = getterCapabilityId;
        this.resolvedHandle = null;
    }

    private Handle ensureHandle() {
        if (resolvedHandle != null) {
            return resolvedHandle;
        }
        if (getterCapabilityId != null) {
            Map<String, Object> args = new HashMap<>();
            args.put("context", getHandle().toJson());
            Object result = getClient().invokeCapability(getterCapabilityId, args);
            if (result instanceof Handle handle) {
                resolvedHandle = handle;
            }
        }
        if (resolvedHandle == null) {
            resolvedHandle = getHandle();
        }
        return resolvedHandle;
    }

    public int size() {
        Object result = getClient().invokeCapability("Aspire.Hosting/Dict.count", Map.of("dict", ensureHandle().toJson()));
        return ((Number) result).intValue();
    }

    @SuppressWarnings("unchecked")
    public V get(K key) {
        Map<String, Object> args = new HashMap<>();
        args.put("dict", ensureHandle().toJson());
        args.put("key", AspireClient.serializeValue(key));
        return (V) getClient().invokeCapability("Aspire.Hosting/Dict.get", args);
    }

    public void put(K key, V value) {
        Map<String, Object> args = new HashMap<>();
        args.put("dict", ensureHandle().toJson());
        args.put("key", AspireClient.serializeValue(key));
        args.put("value", AspireClient.serializeValue(value));
        getClient().invokeCapability("Aspire.Hosting/Dict.set", args);
    }

    public boolean remove(K key) {
        Map<String, Object> args = new HashMap<>();
        args.put("dict", ensureHandle().toJson());
        args.put("key", AspireClient.serializeValue(key));
        Object result = getClient().invokeCapability("Aspire.Hosting/Dict.remove", args);
        return Boolean.TRUE.equals(result);
    }

    public boolean containsKey(K key) {
        Map<String, Object> args = new HashMap<>();
        args.put("dict", ensureHandle().toJson());
        args.put("key", AspireClient.serializeValue(key));
        Object result = getClient().invokeCapability("Aspire.Hosting/Dict.has", args);
        return Boolean.TRUE.equals(result);
    }

    @SuppressWarnings("unchecked")
    public List<K> keys() {
        Object result = getClient().invokeCapability("Aspire.Hosting/Dict.keys", Map.of("dict", ensureHandle().toJson()));
        return (List<K>) result;
    }
}
