package com.github.milkdrinkers.crate.internal.serialize;

import com.github.milkdrinkers.crate.util.Valid;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to register serializable's
 */
@UtilityClass
public class CrateSerializer {

    private final List<CrateSerializable<?>> serializables = Collections
        .synchronizedList(new ArrayList<>());

    public boolean isSerializable(final Class<?> clazz) {
        return findSerializable(clazz) != null;
    }

    /**
     * Register a serializable to our list
     *
     * @param serializable Serializable to register
     */
    public void registerSerializable(@NonNull final CrateSerializable<?> serializable) {
        Valid.notNull(
            serializable.getClazz(),
            "Class of serializable mustn't be null");
        serializables.add(serializable);
    }

    @Nullable
    public CrateSerializable<?> findSerializable(final Class<?> clazz) {
        for (final CrateSerializable<?> serializable : serializables) {
            if (serializable.getClazz().equals(clazz)) {
                return serializable;
            }
        }
        return null;
    }

    @SuppressWarnings("ALL")
    /**e
     * Method to save an object
     */
    public Object serialize(final Object obj) {
        final CrateSerializable serializable = findSerializable(obj.getClass());

        Valid.notNull(
            serializable,
            "No serializable found for '" + obj.getClass().getSimpleName() + "'");
        return serializable.serialize(obj);
    }

    public <T> T deserialize(final Object raw, Class<T> type, final String key) {
        final CrateSerializable<?> serializable = findSerializable(type);
        Valid.notNull(
            serializable,
            "No serializable found for '" + type.getSimpleName() + "'",
            "Raw: '" + raw.getClass().getSimpleName() + "'");
        return (T) serializable.deserialize(raw, key.substring(key.lastIndexOf(".") + 1));
    }
}
