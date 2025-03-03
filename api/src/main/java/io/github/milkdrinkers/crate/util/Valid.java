package io.github.milkdrinkers.crate.util;

import io.github.milkdrinkers.crate.internal.exceptions.CrateValidationException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Valid {

    public void error(final Throwable cause, final String... messages) {
        throw new CrateValidationException(cause, messages);
    }

    public void error(final String... errorMessage) {
        throw new CrateValidationException(errorMessage);
    }

    public void checkBoolean(final boolean condition) {
        checkBoolean(condition, "Valid.checkBoolean(): Condition is False.");
    }

    public void checkBoolean(final boolean condition, final String... errorMessage) {
        if (!condition) {
            throw new CrateValidationException(errorMessage);
        }
    }

    public <T> void checkEqualityNull(@NonNull final T first, final T second) {
        checkBoolean(first.equals(second), "Valid.checkEquality(): first and second must be equal");
    }

    public <T> void checkEquality(@NonNull final T first, final T second, String... messages) {
        checkBoolean(first.equals(second), messages);
    }

    public <T> T notNull(@Nullable final T object) {
        return notNull(object, "Valid.notNull(): Validated Object is null");
    }

    public <T> T notNull(@Nullable final T object, @Nullable final String... message) {
        if (object != null) {
            return object;
        }
        throw new CrateValidationException(message);
    }
}
