package io.github.milkdrinkers.crate.internal.provider;

import io.github.milkdrinkers.crate.internal.settings.DataType;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;

/**
 * Interface for registering more powerful Map/List implementation than the default JDK one's
 * examples for these implementations are FastUtils {@literal &} Trove Used in {@link DataType} Enum
 */
@UtilityClass
@Accessors(fluent = true, chain = true)
public class CrateProviders {

    @Setter
    private MapProvider mapProvider;
    @Setter
    private InputStreamProvider inputStreamProvider;
    @Setter
    private ExceptionHandler exceptionHandler;

    public MapProvider mapProvider() {
        if (mapProvider == null) {
            mapProvider = new MapProvider() {
            };
        }
        return mapProvider;
    }

    public InputStreamProvider inputStreamProvider() {
        if (inputStreamProvider == null) {
            inputStreamProvider = new InputStreamProvider() {
            };
        }

        return inputStreamProvider;
    }

    public ExceptionHandler exceptionHandler() {
        if (exceptionHandler == null) {
            exceptionHandler = new ExceptionHandler() {
            };
        }

        return exceptionHandler;
    }
}
