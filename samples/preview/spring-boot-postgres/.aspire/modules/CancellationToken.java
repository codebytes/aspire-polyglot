// CancellationToken.java - GENERATED CODE - DO NOT EDIT

package aspire;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

/**
 * CancellationToken for cancelling operations.
 */
public class CancellationToken {
    private volatile boolean cancelled = false;
    private final List<Runnable> listeners = new CopyOnWriteArrayList<>();

    // Remote token id supplied by the AppHost when this token is materialized for a
    // callback argument. Null for locally-created tokens. Retained so cancellation can
    // be correlated back to the AppHost if needed.
    private final String remoteTokenId;

    CancellationToken() {
        this.remoteTokenId = null;
    }

    private CancellationToken(String remoteTokenId) {
        this.remoteTokenId = remoteTokenId;
    }

    /**
     * Materializes a cancellation token from a transport value sent by the AppHost.
     * When the AppHost invokes a callback that accepts a CancellationToken it passes a
     * remote token id (a string); generated code calls this to turn that wire value into
     * a CancellationToken instance. Mirrors the TypeScript/Go SDK behavior.
     */
    static CancellationToken fromValue(Object value) {
        if (value instanceof CancellationToken token) {
            return token;
        }
        if (value instanceof String tokenId) {
            return new CancellationToken(tokenId);
        }
        return new CancellationToken();
    }

    String getRemoteTokenId() { return remoteTokenId; }

    void cancel() {
        cancelled = true;
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    boolean isCancelled() { return cancelled; }

    void onCancel(Runnable listener) {
        listeners.add(listener);
        if (cancelled) {
            listener.run();
        }
    }
}
