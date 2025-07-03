package io.github.milkdrinkers.crate.internal.exception;

import lombok.NonNull;

/**
 * Every exception which is thrown in Crate internally extends this exception.
 * <p>
 * It describes the basic format of exceptions we use. See implementations in {@link io.github.milkdrinkers.crate.internal.exceptions}
 */
public class CrateException extends RuntimeException {
    private static final long serialVersionUID = 4815788455395994435L;

    protected CrateException(
        @NonNull final Throwable throwable,
        @NonNull final String... messages) {
        super(String.join("\n", messages), throwable, false, true);
    }

    protected CrateException(final String... messages) {
        super(String.join("\n", messages), null, false, true);
    }

    protected CrateException(
        @NonNull final Throwable cause,
        final boolean enableSuppression,
        final boolean writableStackTrace,
        @NonNull final String... messages) {
        super(String.join("\n", messages), cause, enableSuppression, writableStackTrace);
    }
}
