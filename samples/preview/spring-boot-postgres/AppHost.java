import aspire.*;

public class AppHost {
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);

            var pg = builder.addContainer("pg", "postgres:16");
            pg.withEnvironment("POSTGRES_USER", "postgres");
            pg.withEnvironment("POSTGRES_PASSWORD", "postgres");
            pg.withEnvironment("POSTGRES_DB", "notesdb");

            var api = builder.addDockerfile("api", "./src");
            api.withOtlpExporter();
            api.withEnvironment("NOTESDB_JDBCCONNECTIONSTRING", "jdbc:postgresql://pg:5432/notesdb");
            api.withEnvironment("NOTESDB_USERNAME", "postgres");
            api.withEnvironment("NOTESDB_PASSWORD", "postgres");
            // Declare the HTTP endpoint on the container's target port (8080, where
            // Spring Boot's embedded Tomcat listens). withExternalHttpEndpoints() only
            // marks EXISTING endpoints as external — without an explicit withHttpEndpoint
            // the api container has no endpoint at all, so Aspire exposes no host URL and
            // the API is unreachable from the browser/host.
            api.withHttpEndpoint(new WithHttpEndpointOptions().targetPort(8080.0).name("http"));
            api.withExternalHttpEndpoints();
            // Raw container resources provide startup ordering. Spring Boot then
            // establishes its datasource connection before serving requests.
            api.waitFor(pg);

            DistributedApplication app = builder.build();
            app.run(null);
        } catch (Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
