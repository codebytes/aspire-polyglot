// AddPostgresOptions.java - GENERATED CODE - DO NOT EDIT

package aspire;

/** Options for AddPostgres. */
@SuppressWarnings({"all", "unchecked", "serial"})
public final class AddPostgresOptions {
    private ParameterResource userName;
    private ParameterResource password;
    private Number port;

    public ParameterResource getUserName() { return userName; }
    public AddPostgresOptions userName(ParameterResource value) {
        this.userName = value;
        return this;
    }

    public ParameterResource getPassword() { return password; }
    public AddPostgresOptions password(ParameterResource value) {
        this.password = value;
        return this;
    }

    public Number getPort() { return port; }
    public AddPostgresOptions port(Number value) {
        this.port = value;
        return this;
    }

}
