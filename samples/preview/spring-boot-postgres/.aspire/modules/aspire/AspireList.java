// AspireList.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AspireList is a handle-backed list with lazy handle resolution.
 */
@SuppressWarnings({"all", "unchecked", "serial"})
public class AspireList<T> extends HandleWrapperBase {
    private final String getterCapabilityId;
    private Handle resolvedHandle;

    AspireList(Handle handle, AspireClient client) {
        super(handle, client);
        this.getterCapabilityId = null;
        this.resolvedHandle = handle;
    }

    AspireList(Handle contextHandle, AspireClient client, String getterCapabilityId) {
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

    /**
     * Gets the number of elements in the list.
     *
     * @return the element count
     */
    public int size() {
        Object result = getClient().invokeCapability("Aspire.Hosting/List.length", Map.of("list", ensureHandle().toJson()));
        return ((Number) result).intValue();
    }

    /**
     * Gets the element at the specified index.
     *
     * @param index the zero-based index
     * @return the element at {@code index}
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        Map<String, Object> args = new HashMap<>();
        args.put("list", ensureHandle().toJson());
        args.put("index", index);
        return (T) getClient().invokeCapability("Aspire.Hosting/List.get", args);
    }

    /**
     * Appends an element to the end of the list.
     *
     * @param item the element to append
     */
    public void add(T item) {
        Map<String, Object> args = new HashMap<>();
        args.put("list", ensureHandle().toJson());
        args.put("item", AspireClient.serializeValue(item));
        getClient().invokeCapability("Aspire.Hosting/List.add", args);
    }

    /**
     * Removes the element at the specified index.
     *
     * @param index the zero-based index
     * @return {@code true} if an element was removed
     */
    public boolean remove(int index) {
        Map<String, Object> args = new HashMap<>();
        args.put("list", ensureHandle().toJson());
        args.put("index", index);
        Object result = getClient().invokeCapability("Aspire.Hosting/List.removeAt", args);
        return Boolean.TRUE.equals(result);
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        getClient().invokeCapability("Aspire.Hosting/List.clear", Map.of("list", ensureHandle().toJson()));
    }

    /**
     * Copies the list into a plain {@link List}. Later mutations of the underlying
     * app model are not reflected in the returned copy.
     *
     * @return a snapshot of the list contents
     */
    @SuppressWarnings("unchecked")
    public List<T> toList() {
        Object result = getClient().invokeCapability("Aspire.Hosting/List.toArray", Map.of("list", ensureHandle().toJson()));
        return (List<T>) result;
    }
}
