// WithPgAdminOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for WithPgAdmin. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class WithPgAdminOptions {
    private AspireAction1<PgAdminContainerResource> configureContainer;
    private String containerName;

    public AspireAction1<PgAdminContainerResource> getConfigureContainer() { return configureContainer; }
    public WithPgAdminOptions configureContainer(AspireAction1<PgAdminContainerResource> value) {
        this.configureContainer = value;
        return this;
    }

    public String getContainerName() { return containerName; }
    public WithPgAdminOptions containerName(String value) {
        this.containerName = value;
        return this;
    }

}
