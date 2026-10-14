// CancellationToken.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CancellationToken for cancelling operations.
 */
@SuppressWarnings({"all", "unchecked", "serial"})
public class CancellationToken {
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final AtomicInteger remoteReferences = new AtomicInteger(0);
    private final Object cancellationLock = new Object();
    private final List<Runnable> listeners = new ArrayList<>();

    // Remote token id supplied by the AppHost when this token is materialized for a
    // callback argument. Null for locally-created tokens. Retained so cancellation can
    // be correlated back to the AppHost if needed.
    private final String remoteTokenId;
    private final AspireClient remoteClient;

    public CancellationToken() {
        this.remoteTokenId = null;
        this.remoteClient = null;
    }

    CancellationToken(String remoteTokenId, AspireClient remoteClient) {
        this.remoteTokenId = remoteTokenId;
        this.remoteClient = remoteClient;
    }

    /**
     * Materializes a cancellation token from a transport value sent by the AppHost.
     * When the AppHost invokes a callback that accepts a CancellationToken it passes a
     * remote token id (a string); generated code calls this to turn that wire value into
     * a CancellationToken instance. Mirrors the TypeScript/Go SDK behavior.
     */
    public static CancellationToken fromValue(Object value) {
        if (value instanceof CancellationToken token) {
            return token;
        }
        if (value instanceof String tokenId) {
            AspireClient client = AspireClient.currentCallbackClient();
            return client == null
                ? new CancellationToken(tokenId, null)
                : client.getOrCreateRemoteCancellationToken(tokenId);
        }
        return new CancellationToken();
    }

    String getRemoteTokenId() { return remoteTokenId; }

    void retainRemoteReference() {
        remoteReferences.incrementAndGet();
    }

    void releaseRemoteReference() {
        if (remoteTokenId != null && remoteClient != null) {
            remoteClient.releaseRemoteCancellationToken(remoteTokenId, this);
        }
    }

    int decrementRemoteReference() {
        return remoteReferences.decrementAndGet();
    }

    public void cancel() {
        if (!markCancelled()) {
            return;
        }

        if (remoteTokenId != null && remoteClient != null) {
            remoteClient.removeRemoteCancellationToken(remoteTokenId, this);
        }

        notifyCancellationListeners();
    }

    boolean markCancelled() {
        synchronized (cancellationLock) {
            return cancelled.compareAndSet(false, true);
        }
    }

    void notifyCancellationListeners() {
        List<Runnable> listenersToNotify;
        synchronized (cancellationLock) {
            listenersToNotify = new ArrayList<>(listeners);
            listeners.clear();
        }
        RuntimeException listenerFailure = null;
        for (Runnable listener : listenersToNotify) {
            try {
                listener.run();
            } catch (RuntimeException exception) {
                if (listenerFailure == null) {
                    listenerFailure = exception;
                } else if (listenerFailure != exception) {
                    listenerFailure.addSuppressed(exception);
                }
            }
        }
        if (listenerFailure != null) {
            throw listenerFailure;
        }
    }

    public boolean isCancelled() { return cancelled.get(); }

    public void onCancel(Runnable listener) {
        synchronized (cancellationLock) {
            if (!cancelled.get()) {
                listeners.add(listener);
                return;
            }
        }
        listener.run();
    }

    void removeCancelListener(Runnable listener) {
        synchronized (cancellationLock) {
            listeners.remove(listener);
        }
    }

}
