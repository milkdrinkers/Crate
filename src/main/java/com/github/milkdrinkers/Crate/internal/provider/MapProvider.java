package com.github.milkdrinkers.Crate.internal.provider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface MapProvider {

    default Map<String, Object> getMapImplementation() {
        return new HashMap<>();
    }

    default Map<String, Object> getSortedMapImplementation() {
        return new LinkedHashMap<>();
    }
}
