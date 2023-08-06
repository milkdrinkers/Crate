package com.github.milkdrinkers.Crate.internal.serialize;

import lombok.NonNull;

public interface CrateSerializable<T> {

    /**
     * Get our serializable from data in data-structure.
     *
     * @param obj Data to deserialize our class from.
     * @param key The key of the object.
     * @throws ClassCastException Exception thrown when deserialization failed.
     */
    T deserialize(@NonNull final Object obj, final String key) throws ClassCastException;

    /**
     * Save our serializable to data-structure.
     *
     * @throws ClassCastException Exception thrown when serialization failed.
     */
    Object serialize(@NonNull final T t) throws ClassCastException;

    Class<T> getClazz();
}
