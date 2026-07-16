// WellKnownPipelineSteps.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

public final class WellKnownPipelineSteps {
    private WellKnownPipelineSteps() { }

    /** The step that runs before the application starts. */
    public static final String BeforeStart = "before-start";

    /** The well-known step for building resources. */
    public static final String Build = "build";

    /** The prerequisite step that runs before any build operations. */
    public static final String BuildPrereq = "build-prereq";

    /** The step that checks whether the container runtime (e.g., Docker or Podman) is running. Build steps that need a container runtime should depend on this step. */
    public static final String CheckContainerRuntime = "check-container-runtime";

    /** Aggregation step for all deploy operations. All deploy steps should be required by this step. */
    public static final String Deploy = "deploy";

    /** The prerequisite step that runs before any deploy operations. */
    public static final String DeployPrereq = "deploy-prereq";

    /** Aggregation step for all destroy operations. All destroy steps should be required by this step. */
    public static final String Destroy = "destroy";

    /** The prerequisite step that runs before any destroy operations. */
    public static final String DestroyPrereq = "destroy-prereq";

    /** The diagnostic step that dumps dependency graph information for troubleshooting. */
    public static final String Diagnostics = "diagnostics";

    /** The step that prompts for parameter values before build, publish, or deployment operations. */
    public static final String ProcessParameters = "process-parameters";

    /** Aggregation step for all publish operations. All publish steps should be required by this step. */
    public static final String Publish = "publish";

    /** The prerequisite step that runs before any publish operations. */
    public static final String PublishPrereq = "publish-prereq";

    /** The meta-step that coordinates all push operations. All push steps should be required by this step. */
    public static final String Push = "push";

    /** The prerequisite step that runs before any push operations. */
    public static final String PushPrereq = "push-prereq";

    /** The step that validates compute resources are assigned to unambiguous compute environments. */
    public static final String ValidateComputeEnvironments = "validate-compute-environments";

}
