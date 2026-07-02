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
