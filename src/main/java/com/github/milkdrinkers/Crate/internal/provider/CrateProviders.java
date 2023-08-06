package com.github.milkdrinkers.Crate.internal.provider;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.github.milkdrinkers.Crate.internal.settings.DataType;
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
    private YamlConfig yamlConfig;
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

    public YamlConfig yamlConfig() {
        if (yamlConfig == null) {
            final YamlConfig config = new YamlConfig();
            // Use unicode
            config.writeConfig.setEscapeUnicode(false);
            // Don't use anchors
            config.writeConfig.setAutoAnchor(false);
            // Never use write the classname above keys
            config.writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);
            yamlConfig = config;
        }
        return yamlConfig;
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
