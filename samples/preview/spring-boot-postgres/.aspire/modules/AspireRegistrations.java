// AspireRegistrations.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Static initializer to register handle wrappers. */
public class AspireRegistrations {
    static {
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.IDistributedApplicationBuilder", (h, c) -> new IDistributedApplicationBuilder(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.IDistributedApplicationPipeline", (h, c) -> new IDistributedApplicationPipeline(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.DistributedApplication", (h, c) -> new DistributedApplication(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.EndpointReference", (h, c) -> new EndpointReference(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IAspireStore", (h, c) -> new IAspireStore(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IExecutionConfigurationBuilder", (h, c) -> new IExecutionConfigurationBuilder(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IExecutionConfigurationResult", (h, c) -> new IExecutionConfigurationResult(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IResource", (h, c) -> new IResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IResourceWithEnvironment", (h, c) -> new IResourceWithEnvironment(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IResourceWithEndpoints", (h, c) -> new IResourceWithEndpoints(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IResourceWithArgs", (h, c) -> new IResourceWithArgs(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IResourceWithConnectionString", (h, c) -> new IResourceWithConnectionString(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IResourceWithWaitSupport", (h, c) -> new IResourceWithWaitSupport(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IResourceWithParent", (h, c) -> new IResourceWithParent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerResource", (h, c) -> new ContainerResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ExecutableResource", (h, c) -> new ExecutableResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ProjectResource", (h, c) -> new ProjectResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ParameterResource", (h, c) -> new ParameterResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerMountAnnotation", (h, c) -> new ContainerMountAnnotation(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerImageReference", (h, c) -> new ContainerImageReference(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerPortReference", (h, c) -> new ContainerPortReference(h, c));
        AspireClient.registerHandleWrapper("System.ComponentModel/System.IServiceProvider", (h, c) -> new IServiceProvider(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceNotificationService", (h, c) -> new ResourceNotificationService(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceLoggerService", (h, c) -> new ResourceLoggerService(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceCommandService", (h, c) -> new ResourceCommandService(h, c));
        AspireClient.registerHandleWrapper("Microsoft.Extensions.Configuration.Abstractions/Microsoft.Extensions.Configuration.IConfiguration", (h, c) -> new IConfiguration(h, c));
        AspireClient.registerHandleWrapper("Microsoft.Extensions.Configuration.Abstractions/Microsoft.Extensions.Configuration.IConfigurationSection", (h, c) -> new IConfigurationSection(h, c));
        AspireClient.registerHandleWrapper("Microsoft.Extensions.Hosting.Abstractions/Microsoft.Extensions.Hosting.IHostEnvironment", (h, c) -> new IHostEnvironment(h, c));
        AspireClient.registerHandleWrapper("Microsoft.Extensions.Logging.Abstractions/Microsoft.Extensions.Logging.ILogger", (h, c) -> new ILogger(h, c));
        AspireClient.registerHandleWrapper("Microsoft.Extensions.Logging.Abstractions/Microsoft.Extensions.Logging.ILoggerFactory", (h, c) -> new ILoggerFactory(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.IReportingStep", (h, c) -> new IReportingStep(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.IReportingTask", (h, c) -> new IReportingTask(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Eventing.DistributedApplicationEventSubscription", (h, c) -> new DistributedApplicationEventSubscription(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.DistributedApplicationExecutionContext", (h, c) -> new DistributedApplicationExecutionContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.DistributedApplicationExecutionContextOptions", (h, c) -> new DistributedApplicationExecutionContextOptions(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.InteractionInputCollection", (h, c) -> new InteractionInputCollection(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.InputsDialogValidationContext", (h, c) -> new InputsDialogValidationContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ProjectResourceOptions", (h, c) -> new ProjectResourceOptions(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.IUserSecretsManager", (h, c) -> new IUserSecretsManager(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Publishing.AfterPublishEvent", (h, c) -> new AfterPublishEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Publishing.BeforePublishEvent", (h, c) -> new BeforePublishEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineConfigurationContext", (h, c) -> new PipelineConfigurationContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineContext", (h, c) -> new PipelineContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineEditor", (h, c) -> new PipelineEditor(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineStep", (h, c) -> new PipelineStep(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineStepContext", (h, c) -> new PipelineStepContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineStepFactoryContext", (h, c) -> new PipelineStepFactoryContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Pipelines.PipelineSummary", (h, c) -> new PipelineSummary(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Eventing.DistributedApplicationResourceEventSubscription", (h, c) -> new DistributedApplicationResourceEventSubscription(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Eventing.IDistributedApplicationEvent", (h, c) -> new IDistributedApplicationEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Eventing.IDistributedApplicationResourceEvent", (h, c) -> new IDistributedApplicationResourceEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Eventing.IDistributedApplicationEventing", (h, c) -> new IDistributedApplicationEventing(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.Ats.EventingSubscriberRegistrationContext", (h, c) -> new EventingSubscriberRegistrationContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.AfterResourcesCreatedEvent", (h, c) -> new AfterResourcesCreatedEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.BeforeResourceStartedEvent", (h, c) -> new BeforeResourceStartedEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.BeforeStartEvent", (h, c) -> new BeforeStartEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.CommandLineArgsCallbackContext", (h, c) -> new CommandLineArgsCallbackContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.CommandLineArgsEditor", (h, c) -> new CommandLineArgsEditor(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ConnectionStringAvailableEvent", (h, c) -> new ConnectionStringAvailableEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerImagePushOptions", (h, c) -> new ContainerImagePushOptions(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerImagePushOptionsCallbackContext", (h, c) -> new ContainerImagePushOptionsCallbackContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.DistributedApplicationModel", (h, c) -> new DistributedApplicationModel(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.DockerfileBuilderCallbackContext", (h, c) -> new DockerfileBuilderCallbackContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.DockerfileFactoryContext", (h, c) -> new DockerfileFactoryContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.EndpointReferenceExpression", (h, c) -> new EndpointReferenceExpression(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.EndpointUpdateContext", (h, c) -> new EndpointUpdateContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.EnvironmentCallbackContext", (h, c) -> new EnvironmentCallbackContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.EnvironmentEditor", (h, c) -> new EnvironmentEditor(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IExpressionValue", (h, c) -> new IExpressionValue(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.InitializeResourceEvent", (h, c) -> new InitializeResourceEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.LogFacade", (h, c) -> new LogFacade(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ReferenceExpressionBuilder", (h, c) -> new ReferenceExpressionBuilder(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.UpdateCommandStateContext", (h, c) -> new UpdateCommandStateContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ExecuteCommandContext", (h, c) -> new ExecuteCommandContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceEndpointsAllocatedEvent", (h, c) -> new ResourceEndpointsAllocatedEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceReadyEvent", (h, c) -> new ResourceReadyEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceStoppedEvent", (h, c) -> new ResourceStoppedEvent(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceUrlsCallbackContext", (h, c) -> new ResourceUrlsCallbackContext(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ResourceUrlsEditor", (h, c) -> new ResourceUrlsEditor(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.Docker.DockerfileBuilder", (h, c) -> new DockerfileBuilder(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.Docker.DockerfileStage", (h, c) -> new DockerfileStage(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.ContainerRegistryResource", (h, c) -> new ContainerRegistryResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.DotnetToolResource", (h, c) -> new DotnetToolResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ExternalServiceResource", (h, c) -> new ExternalServiceResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.CSharpAppResource", (h, c) -> new CSharpAppResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.IResourceWithContainerFiles", (h, c) -> new IResourceWithContainerFiles(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IComputeEnvironmentResource", (h, c) -> new IComputeEnvironmentResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IContainerFilesDestinationResource", (h, c) -> new IContainerFilesDestinationResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Aspire.Hosting.ApplicationModel.IComputeResource", (h, c) -> new IComputeResource(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/List<string>", (h, c) -> new AspireList(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/Dict<string,any>", (h, c) -> new AspireDict(h, c));
        AspireClient.registerHandleWrapper("Aspire.Hosting/List<any>", (h, c) -> new AspireList(h, c));
    }

    static void ensureRegistered() {
        // Called to trigger static initializer
    }
}
