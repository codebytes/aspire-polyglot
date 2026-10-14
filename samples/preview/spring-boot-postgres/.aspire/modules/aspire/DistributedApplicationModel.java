// DistributedApplicationModel.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.HashMap;
import java.util.Map;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.DistributedApplicationModel. */
@SuppressWarnings({"all", "unchecked", "serial"})
public class DistributedApplicationModel extends HandleWrapperBase {
    DistributedApplicationModel(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets all resources in the distributed application model. */
    public IResource[] getResources() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("model", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting/getResources", reqArgs);
        return (IResource[]) AspireClient.convertArray(result, IResource[].class.getComponentType(), item0 -> (IResource) item0);
    }

    /** Finds a resource by name. */
    public IResource findResourceByName(String name) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("model", AspireClient.serializeValue(getHandle()));
        reqArgs.put("name", AspireClient.serializeValue(name));
        var result = getClient().invokeCapability("Aspire.Hosting/findResourceByName", reqArgs);
        return (IResource) result;
    }

}
