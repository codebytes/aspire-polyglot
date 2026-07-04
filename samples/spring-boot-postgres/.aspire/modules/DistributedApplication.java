// DistributedApplication.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.DistributedApplication. */
public class DistributedApplication extends HandleWrapperBase {
    DistributedApplication(Handle handle, AspireClient client) {
        super(handle, client);
    }

    public void run() {
        run(null);
    }

    /** Runs the distributed application */
    public void run(CancellationToken cancellationToken) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        if (cancellationToken != null) {
            reqArgs.put("cancellationToken", getClient().registerCancellation(cancellationToken));
        }
        getClient().invokeCapability("Aspire.Hosting/run", reqArgs);
    }

    /** Create a new distributed application builder. */
    public static IDistributedApplicationBuilder CreateBuilder() throws Exception {
        return CreateBuilder((String[]) null);
    }

    /** Create a new distributed application builder. */
    public static IDistributedApplicationBuilder CreateBuilder(String[] args) throws Exception {
        CreateBuilderOptions options = new CreateBuilderOptions();
        if (args != null) {
            options.setArgs(args);
        }
        return CreateBuilder(options);
    }

    /** Create a new distributed application builder. */
    public static IDistributedApplicationBuilder CreateBuilder(CreateBuilderOptions options) throws Exception {
        return Aspire.createBuilder(options);
    }

}
