// Aspire.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Main entry point for Aspire SDK. */
public class Aspire {
    /** Connect to the AppHost server. */
    public static AspireClient connect() throws Exception {
        BaseRegistrations.ensureRegistered();
        AspireRegistrations.ensureRegistered();
        String socketPath = System.getenv("REMOTE_APP_HOST_SOCKET_PATH");
        if (socketPath == null || socketPath.isEmpty()) {
            throw new RuntimeException("REMOTE_APP_HOST_SOCKET_PATH environment variable not set. Run this application using `aspire run`.");
        }
        AspireClient client = new AspireClient(socketPath);
        client.connect();
        String authToken = System.getenv("ASPIRE_REMOTE_APPHOST_TOKEN");
        if (authToken == null || authToken.isEmpty()) {
            throw new RuntimeException("ASPIRE_REMOTE_APPHOST_TOKEN environment variable not set. Run this application using `aspire run`.");
        }
        client.authenticate(authToken);
        client.onDisconnect(() -> System.exit(1));
        return client;
    }

    /** Create a new distributed application builder. */
    public static IDistributedApplicationBuilder createBuilder(CreateBuilderOptions options) throws Exception {
        AspireClient client = connect();
        Map<String, Object> resolvedOptions = new HashMap<>();
        if (options != null) {
            resolvedOptions.putAll(options.toMap());
        }
        if (resolvedOptions.get("Args") == null) {
            // Note: Java doesn't have easy access to command line args from here
            resolvedOptions.put("Args", new String[0]);
        }
        if (resolvedOptions.get("ProjectDirectory") == null) {
            String projectDirectory = System.getenv("ASPIRE_PROJECT_DIRECTORY");
            if (projectDirectory == null || projectDirectory.isEmpty()) {
                projectDirectory = System.getProperty("user.dir");
            }
            resolvedOptions.put("ProjectDirectory", projectDirectory);
        }
        if (resolvedOptions.get("AppHostFilePath") == null) {
            String appHostFilePath = System.getenv("ASPIRE_APPHOST_FILEPATH");
            if (appHostFilePath != null && !appHostFilePath.isEmpty()) {
                resolvedOptions.put("AppHostFilePath", appHostFilePath);
            }
        }
        Map<String, Object> args = new HashMap<>();
        args.put("argsOrOptions", resolvedOptions);
        return (IDistributedApplicationBuilder) client.invokeCapability("Aspire.Hosting/createBuilder", args);
    }
}
