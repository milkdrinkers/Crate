package com.github.milkdrinkers.crate;

import com.github.milkdrinkers.crate.internal.provider.CrateProviders;
import com.github.milkdrinkers.crate.internal.provider.InputStreamProvider;
import com.github.milkdrinkers.crate.util.FileUtils;
import com.github.milkdrinkers.crate.util.Valid;
import lombok.NonNull;

import java.io.File;
import java.nio.file.Path;

public class TomlBuilder extends AbstractCrateBuilder<TomlBuilder, Toml> {
    private TomlBuilder(String name, String path, InputStreamProvider inputStreamProvider) {
        super(name, path, inputStreamProvider);
    }

    public static TomlBuilder fromPath(@NonNull final String name, @NonNull final String path) {
        return new TomlBuilder(name, path, CrateProviders.inputStreamProvider());
    }

    public static TomlBuilder fromPath(@NonNull final Path path) {
        return fromFile(path.toFile());
    }

    public static TomlBuilder fromFile(@NonNull final File file) {
        // File shouldn't be a directory
        Valid.checkBoolean(
            !file.isDirectory(),
            "File mustn't be a directory.",
            "Please use from Directory to use a directory",
            "This is due to Java-Internals");

        return new TomlBuilder(
            FileUtils.replaceExtensions(file.getName()),
            FileUtils.getParentDirPath(file),
            CrateProviders.inputStreamProvider()
        );
    }

    public static TomlBuilder fromDirectory(@NonNull final File file) {
        Valid.checkBoolean(!file.getName().contains("."), "File-Name mustn't contain '.'");

        if (!file.exists()) {
            file.mkdirs();
        }

        // Will return the name of the folder as default name
        return new TomlBuilder(file.getName(), file.getAbsolutePath(), CrateProviders.inputStreamProvider());
    }

    public Toml create() {
        return new Toml(
            super.getName(),
            super.getPath(),
            super.getInputStream(),
            super.getReloadSetting(),
            super.getReloadConsumer()
        );
    }
}
