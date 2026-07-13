// IAspireStore.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.ApplicationModel.IAspireStore. */
public class IAspireStore extends HandleWrapperBase {
    IAspireStore(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Gets the base path of this store. */
    public String basePath() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.ApplicationModel/IAspireStore.basePath", reqArgs);
        return (String) result;
    }

    /** Gets a deterministic file path that is a copy of the `sourceFilename`. The resulting file name will depend on the content of the file. */
    public String getFileNameWithContent(String filenameTemplate, String sourceFilename) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("aspireStore", AspireClient.serializeValue(getHandle()));
        reqArgs.put("filenameTemplate", AspireClient.serializeValue(filenameTemplate));
        reqArgs.put("sourceFilename", AspireClient.serializeValue(sourceFilename));
        var result = getClient().invokeCapability("Aspire.Hosting/getFileNameWithContent", reqArgs);
        return (String) result;
    }

}
