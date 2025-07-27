package io.github.milkdrinkers.crate;

import io.github.milkdrinkers.crate.internal.provider.json.CrateProviders;
import io.github.milkdrinkers.crate.internal.provider.InputStreamProvider;
import io.github.milkdrinkers.crate.util.FileUtils;
import io.github.milkdrinkers.crate.util.Valid;
import lombok.NonNull;

import java.io.File;
import java.nio.file.Path;

public class JsonBuilder extends AbstractCrateBuilder<JsonBuilder, Json> {
    private JsonBuilder(String name, String path, InputStreamProvider inputStreamProvider) {
        super(name, path, inputStreamProvider);
    }

    public static JsonBuilder fromPath(@NonNull final String name, @NonNull final String path) {
        return new JsonBuilder(name, path, CrateProviders.inputStreamProvider());
    }

    public static JsonBuilder fromPath(@NonNull final Path path) {
        return fromFile(path.toFile());
    }

    public static JsonBuilder fromFile(@NonNull final File file) {
        // File shouldn't be a directory
        Valid.checkBoolean(
            !file.isDirectory(),
            "File mustn't be a directory.",
            "Please use from Directory to use a directory",
            "This is due to Java-Internals");

        return new JsonBuilder(
            FileUtils.replaceExtensions(file.getName()),
            FileUtils.getParentDirPath(file),
            CrateProviders.inputStreamProvider()
        );
    }

    public static JsonBuilder fromDirectory(@NonNull final File file) {
        Valid.checkBoolean(!file.getName().contains("."), "File-Name mustn't contain '.'");

        if (!file.exists()) {
            file.mkdirs();
        }

        // Will return the name of the folder as default name
        return new JsonBuilder(file.getName(), file.getAbsolutePath(), CrateProviders.inputStreamProvider());
    }

    public Json create() {
        return new Json(
            super.getName(),
            super.getPath(),
            super.getInputStream(),
            super.getReloadSetting(),
            super.getReloadConsumer()
        );
    }
}
