package com.github.milkdrinkers.Crate;

import com.github.milkdrinkers.Crate.internal.FlatFile;
import com.github.milkdrinkers.Crate.internal.provider.InputStreamProvider;
import com.github.milkdrinkers.Crate.internal.provider.CrateProviders;
import com.github.milkdrinkers.Crate.internal.settings.ConfigSettings;
import com.github.milkdrinkers.Crate.internal.settings.DataType;
import com.github.milkdrinkers.Crate.internal.settings.ReloadSettings;
import com.github.milkdrinkers.Crate.util.FileUtils;
import com.github.milkdrinkers.Crate.util.Valid;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class CrateBuilder {

    private final InputStreamProvider inputStreamProvider;

    private final String path;
    private String name;
    private InputStream inputStream;
    private ReloadSettings reloadSettings;
    private ConfigSettings configSettings;
    private DataType dataType;

    private @Nullable Consumer<FlatFile> reloadConsumer = null;

    private CrateBuilder(
        final String name, final String path, final InputStreamProvider inputStreamProvider) {
        this.name = name;
        this.path = path;
        this.inputStreamProvider = inputStreamProvider;
    }

    // ----------------------------------------------------------------------------------------------------
    // Creating our Builder
    // ----------------------------------------------------------------------------------------------------

    public static CrateBuilder fromPath(@NonNull final String name, @NonNull final String path) {
        return new CrateBuilder(name, path, CrateProviders.inputStreamProvider());
    }

    public static CrateBuilder fromPath(@NonNull final Path path) {
        return fromFile(path.toFile());
    }

    public static CrateBuilder fromFile(@NonNull final File file) {
        // File shouldn't be a directory
        Valid.checkBoolean(
            !file.isDirectory(),
            "File mustn't be a directory.",
            "Please use from Directory to use a directory",
            "This is due to Java-Internals");

        return new CrateBuilder(
            FileUtils.replaceExtensions(file.getName()),
            FileUtils.getParentDirPath(file),
            CrateProviders.inputStreamProvider());
    }

    public static CrateBuilder fromDirectory(@NonNull final File file) {
        Valid.checkBoolean(!file.getName().contains("."), "File-Name mustn't contain '.'");

        if (!file.exists()) {
            file.mkdirs();
        }

        // Will return the name of the folder as default name
        return new CrateBuilder(
            file.getName(), file.getAbsolutePath(), CrateProviders.inputStreamProvider());
    }

    // ----------------------------------------------------------------------------------------------------
    // Adding out settings
    // ----------------------------------------------------------------------------------------------------

    public CrateBuilder reloadCallback(@Nullable final Consumer<FlatFile> reloadConsumer) {
        this.reloadConsumer = reloadConsumer;
        return this;
    }

    public CrateBuilder addInputStreamFromFile(@NonNull final File file) {
        this.inputStream = FileUtils.createInputStream(file);
        return this;
    }

    public CrateBuilder addInputStreamFromResource(@NonNull final String resource) {
        this.inputStream = this.inputStreamProvider.createInputStreamFromInnerResource(resource);

        Valid.notNull(
            this.inputStream, "InputStream is null.", "No inbuilt resource '" + resource + "' found: ");
        return this;
    }

    public CrateBuilder setName(@NonNull final String name) {
        this.name = name;
        return this;
    }

    public CrateBuilder addInputStream(@Nullable final InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public CrateBuilder setConfigSettings(@NonNull final ConfigSettings configSettings) {
        this.configSettings = configSettings;
        return this;
    }

    public CrateBuilder setReloadSettings(@NonNull final ReloadSettings reloadSettings) {
        this.reloadSettings = reloadSettings;
        return this;
    }

    public CrateBuilder setDataType(@NonNull final DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    // ----------------------------------------------------------------------------------------------------
    // Create the objects of our FileTypes
    // ----------------------------------------------------------------------------------------------------

    public Config createConfig() {
        return new Config(
            this.name,
            this.path,
            this.inputStream,
            this.reloadSettings,
            this.configSettings,
            this.dataType,
            reloadConsumer);
    }

    public Yaml createYaml() {
        return new Yaml(
            this.name,
            this.path,
            this.inputStream,
            this.reloadSettings,
            this.configSettings,
            this.dataType,
            reloadConsumer);
    }

    public Toml createToml() {
        return new Toml(
            this.name,
            this.path,
            this.inputStream,
            this.reloadSettings,
            reloadConsumer);
    }

    public Json createJson() {
        return new Json(
            this.name,
            this.path,
            this.inputStream,
            this.reloadSettings,
            reloadConsumer);
    }
}
