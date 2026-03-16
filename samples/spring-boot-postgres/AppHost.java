package aspire;

public class AppHost {
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);

            var pg = builder.addPostgres("pg", null, null);
            pg.withPgAdmin();
            var db = pg.addDatabase("notesdb");

            builder.addDockerfile("api", "./src")
                .withReference(db)
                .withHttpEndpoint(8080, "PORT")
                .withExternalHttpEndpoints();

            DistributedApplication app = builder.build();
            app.run(null);
        } catch (Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
