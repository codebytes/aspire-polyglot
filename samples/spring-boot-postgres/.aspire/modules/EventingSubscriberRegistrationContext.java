// EventingSubscriberRegistrationContext.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.*;
import java.util.function.*;

/** Wrapper for Aspire.Hosting/Aspire.Hosting.Ats.EventingSubscriberRegistrationContext. */
public class EventingSubscriberRegistrationContext extends HandleWrapperBase {
    EventingSubscriberRegistrationContext(Handle handle, AspireClient client) {
        super(handle, client);
    }

    /** Subscribes to the BeforeStart event from an eventing subscriber registration context. */
    public DistributedApplicationEventSubscription onBeforeStart(AspireAction1<BeforeStartEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (BeforeStartEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        var result = getClient().invokeCapability("Aspire.Hosting/eventingSubscriberOnBeforeStart", reqArgs);
        return (DistributedApplicationEventSubscription) result;
    }

    /** Subscribes to the BeforePublish event from an eventing subscriber registration context. */
    public DistributedApplicationEventSubscription onBeforePublish(AspireAction1<BeforePublishEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (BeforePublishEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        var result = getClient().invokeCapability("Aspire.Hosting/eventingSubscriberOnBeforePublish", reqArgs);
        return (DistributedApplicationEventSubscription) result;
    }

    /** Subscribes to the AfterPublish event from an eventing subscriber registration context. */
    public DistributedApplicationEventSubscription onAfterPublish(AspireAction1<AfterPublishEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (AfterPublishEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        var result = getClient().invokeCapability("Aspire.Hosting/eventingSubscriberOnAfterPublish", reqArgs);
        return (DistributedApplicationEventSubscription) result;
    }

    /** Subscribes to the AfterResourcesCreated event from an eventing subscriber registration context. */
    public DistributedApplicationEventSubscription onAfterResourcesCreated(AspireAction1<AfterResourcesCreatedEvent> callback) {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var callbackId = getClient().registerCallback(args -> {
            var arg = (AfterResourcesCreatedEvent) args[0];
            callback.invoke(arg);
            return null;
        });
        if (callbackId != null) {
            reqArgs.put("callback", callbackId);
        }
        var result = getClient().invokeCapability("Aspire.Hosting/eventingSubscriberOnAfterResourcesCreated", reqArgs);
        return (DistributedApplicationEventSubscription) result;
    }

    /** The execution context for the AppHost invocation. */
    public DistributedApplicationExecutionContext executionContext() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/EventingSubscriberRegistrationContext.executionContext", reqArgs);
        return (DistributedApplicationExecutionContext) result;
    }

    /** The cancellation token associated with the subscriber registration. */
    public CancellationToken cancellationToken() {
        Map<String, Object> reqArgs = new HashMap<>();
        reqArgs.put("context", AspireClient.serializeValue(getHandle()));
        var result = getClient().invokeCapability("Aspire.Hosting.Ats/EventingSubscriberRegistrationContext.cancellationToken", reqArgs);
        return (CancellationToken) result;
    }

}
