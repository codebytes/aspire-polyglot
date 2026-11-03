// WithPostgresMcpOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for WithPostgresMcp. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class WithPostgresMcpOptions {
    private AspireAction1<PostgresMcpContainerResource> configureContainer;
    private String containerName;

    public AspireAction1<PostgresMcpContainerResource> getConfigureContainer() { return configureContainer; }
    public WithPostgresMcpOptions configureContainer(AspireAction1<PostgresMcpContainerResource> value) {
        this.configureContainer = value;
        return this;
    }

    public String getContainerName() { return containerName; }
    public WithPostgresMcpOptions containerName(String value) {
        this.containerName = value;
        return this;
    }

}
