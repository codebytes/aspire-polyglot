// WithPgWebOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for WithPgWeb. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class WithPgWebOptions {
    private AspireAction1<PgWebContainerResource> configureContainer;
    private String containerName;

    public AspireAction1<PgWebContainerResource> getConfigureContainer() { return configureContainer; }
    public WithPgWebOptions configureContainer(AspireAction1<PgWebContainerResource> value) {
        this.configureContainer = value;
        return this;
    }

    public String getContainerName() { return containerName; }
    public WithPgWebOptions containerName(String value) {
        this.containerName = value;
        return this;
    }

}
