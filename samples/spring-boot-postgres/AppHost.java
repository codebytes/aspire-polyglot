package aspire;

public class AppHost {
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);

            var pg = builder.addContainer("pg", "postgres:16");
            pg.withEnvironment("POSTGRES_USER", "postgres");
            pg.withEnvironment("POSTGRES_PASSWORD", "postgres");
            pg.withEnvironment("POSTGRES_DB", "notesdb");

            var api = builder.addDockerfile("api", "./src", null, null);
            api.withEnvironment("NOTESDB_JDBCCONNECTIONSTRING", "jdbc:postgresql://pg:5432/notesdb");
            api.withEnvironment("NOTESDB_USERNAME", "postgres");
            api.withEnvironment("NOTESDB_PASSWORD", "postgres");
            api.withExternalHttpEndpoints();

            DistributedApplication app = builder.build();
            app.run(null);
        } catch (Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
