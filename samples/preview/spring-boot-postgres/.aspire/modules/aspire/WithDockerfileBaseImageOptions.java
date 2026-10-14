// WithDockerfileBaseImageOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for WithDockerfileBaseImage. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class WithDockerfileBaseImageOptions {
    private String buildImage;
    private String runtimeImage;

    public String getBuildImage() { return buildImage; }
    public WithDockerfileBaseImageOptions buildImage(String value) {
        this.buildImage = value;
        return this;
    }

    public String getRuntimeImage() { return runtimeImage; }
    public WithDockerfileBaseImageOptions runtimeImage(String value) {
        this.runtimeImage = value;
        return this;
    }

}
