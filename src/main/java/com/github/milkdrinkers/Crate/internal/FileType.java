package com.github.milkdrinkers.Crate.internal;

import com.github.milkdrinkers.Crate.util.FileUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public enum FileType {
    JSON("json"),
    YAML("yml"),
    TOML("toml");

    private final String extension;

    public static FileType fromFile(final File file) {
        return fromExtension(FileUtils.getExtension(file));
    }

    public static FileType fromExtension(final String type) {
        for (final FileType value : values()) {
            if (!value.extension.equalsIgnoreCase(type)) {
                continue;
            }
            return value;
        }
        return null;
    }

    public static FileType fromExtension(final File file) {
        return fromExtension(FileUtils.getExtension(file));
    }
}
