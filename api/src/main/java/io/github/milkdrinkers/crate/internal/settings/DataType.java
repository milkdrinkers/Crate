package io.github.milkdrinkers.crate.internal.settings;

import io.github.milkdrinkers.crate.internal.provider.MapProvider;
import io.github.milkdrinkers.crate.internal.provider.CrateProviders;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * An Enum defining how the Data should be stored
 */
@RequiredArgsConstructor
public enum DataType {
    SORTED {
        @Override
        public Map<String, Object> getMapImplementation() {
            return mapProvider.getSortedMapImplementation();
        }
    },

    UNSORTED {
        @Override
        public Map<String, Object> getMapImplementation() {
            return mapProvider.getMapImplementation();
        }
    };

    private static final MapProvider mapProvider = CrateProviders.mapProvider();

    public static DataType forConfigSetting(final ConfigSetting configSetting) {
        // Only Configs needs the preservation of the order of the keys
        if (ConfigSetting.PRESERVE_COMMENTS.equals(configSetting)) {
            return SORTED;
        }
        // In all other cases using the normal HashMap is better to save memory.
        return UNSORTED;
    }

    public Map<String, Object> getMapImplementation() {
        throw new AbstractMethodError("Not implemented");
    }
}
