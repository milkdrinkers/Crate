package io.github.milkdrinkers.crate.internal.provider;

import io.github.milkdrinkers.crate.internal.exceptions.CrateValidationException;
import lombok.NonNull;

public abstract class ExceptionHandler {

    public RuntimeException create(
        @NonNull final Throwable throwable,
        @NonNull final String... messages) {
        return new CrateValidationException(
            throwable,
            messages);
    }
}
