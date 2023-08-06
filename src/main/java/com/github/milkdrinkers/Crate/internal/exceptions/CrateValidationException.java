package com.github.milkdrinkers.Crate.internal.exceptions;

import com.github.milkdrinkers.Crate.internal.exception.CrateException;

/**
 * Thrown to indicate that something went wrong, or just to end our code
 */
public class CrateValidationException extends CrateException {

    private static final long serialVersionUID = -7961367314553460325L;

    public CrateValidationException(
        final Throwable throwable,
        final String... messages) {
        super(throwable, messages);
    }

    public CrateValidationException(final String... messages) {
        super(messages);
    }
}
