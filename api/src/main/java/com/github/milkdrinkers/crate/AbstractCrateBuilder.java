package com.github.milkdrinkers.crate;

import com.github.milkdrinkers.crate.internal.FlatFile;
import com.github.milkdrinkers.crate.internal.provider.InputStreamProvider;
import com.github.milkdrinkers.crate.internal.settings.ConfigSetting;
import com.github.milkdrinkers.crate.internal.settings.DataType;
import com.github.milkdrinkers.crate.internal.settings.ReloadSetting;
import com.github.milkdrinkers.crate.util.FileUtils;
import com.github.milkdrinkers.crate.util.Valid;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;

public abstract class AbstractCrateBuilder<T, Y> {
    private final InputStreamProvider inputStreamProvider;
    private final String path;
    private String name;
    private InputStream inputStream;
    private ReloadSetting reloadSetting;
    private ConfigSetting configSetting;
    private DataType dataType;

    private @Nullable Consumer<FlatFile> reloadConsumer = null;

    protected AbstractCrateBuilder(final String name, final String path, final InputStreamProvider inputStreamProvider) {
        this.name = name;
        this.path = path;
        this.inputStreamProvider = inputStreamProvider;
    }

    // ----------------------------------------------------------------------------------------------------
    // Adding out settings
    // ----------------------------------------------------------------------------------------------------

    public AbstractCrateBuilder<T, Y> reloadCallback(@Nullable final Consumer<FlatFile> reloadConsumer) {
        this.reloadConsumer = reloadConsumer;
        return this;
    }

    public AbstractCrateBuilder<T, Y> addInputStreamFromFile(@NonNull final File file) {
        this.inputStream = FileUtils.createInputStream(file);
        return this;
    }

    public AbstractCrateBuilder<T, Y> addInputStreamFromResource(@NonNull final String resource) {
        this.inputStream = this.inputStreamProvider.createInputStreamFromInnerResource(resource);

        Valid.notNull(
            this.inputStream, "InputStream is null.", "No inbuilt resource '" + resource + "' found: ");
        return this;
    }

    public AbstractCrateBuilder<T, Y> setName(@NonNull final String name) {
        this.name = name;
        return this;
    }

    public AbstractCrateBuilder<T, Y> addInputStream(@Nullable final InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public AbstractCrateBuilder<T, Y> setConfigSetting(@NonNull final ConfigSetting configSetting) {
        this.configSetting = configSetting;
        return this;
    }

    public AbstractCrateBuilder<T, Y> setReloadSetting(@NonNull final ReloadSetting reloadSetting) {
        this.reloadSetting = reloadSetting;
        return this;
    }

    public AbstractCrateBuilder<T, Y> setDataType(@NonNull final DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    // Internal getters

    protected InputStreamProvider getInputStreamProvider() {
        return inputStreamProvider;
    }

    protected String getPath() {
        return path;
    }

    protected String getName() {
        return name;
    }

    protected InputStream getInputStream() {
        return inputStream;
    }

    protected ReloadSetting getReloadSetting() {
        return reloadSetting;
    }

    protected ConfigSetting getConfigSetting() {
        return configSetting;
    }

    protected DataType getDataType() {
        return dataType;
    }

    protected @Nullable Consumer<FlatFile> getReloadConsumer() {
        return reloadConsumer;
    }

    // Create object

    public abstract Y create();
}
