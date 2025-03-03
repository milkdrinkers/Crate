package io.github.milkdrinkers.crate.internal.exceptions;

import io.github.milkdrinkers.crate.internal.exception.CrateException;

/**
 * Thrown when a problem occurs during parsing or writing NBT data.
 */
public class TomlException extends CrateException {

    private static final long serialVersionUID = 1L;

    public TomlException(final Throwable cause, final String... messages) {
        super(cause, messages);
    }

    public TomlException(final String... messages) {
        super(messages);
    }
}
