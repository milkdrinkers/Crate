package com.github.milkdrinkers.Crate.internal.provider;

import com.github.milkdrinkers.Crate.internal.exceptions.CrateValidationException;
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
