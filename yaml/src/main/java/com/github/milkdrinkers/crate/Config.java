package com.github.milkdrinkers.crate;

import com.github.milkdrinkers.crate.internal.FlatFile;
import com.github.milkdrinkers.crate.internal.settings.ConfigSetting;
import com.github.milkdrinkers.crate.internal.settings.DataType;
import com.github.milkdrinkers.crate.internal.settings.ReloadSetting;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;

@SuppressWarnings({"unused"})
public class Config extends Yaml {

    public Config(@NonNull final Config config) {
        super(config);
    }

    public Config(final String name, final String path) {
        this(name, path, null, null, ConfigSetting.PRESERVE_COMMENTS, DataType.SORTED);
    }

    public Config(
        final String name,
        @Nullable final String path,
        @Nullable final InputStream inputStream) {
        this(name, path, inputStream, null, ConfigSetting.PRESERVE_COMMENTS, DataType.SORTED);
    }

    public Config(
        final String name,
        @Nullable final String path,
        @Nullable final InputStream inputStream,
        @Nullable final ReloadSetting reloadSetting,
        @Nullable final ConfigSetting configSetting,
        @Nullable final DataType dataType) {
        super(name, path, inputStream, reloadSetting, configSetting, dataType);
        setConfigSetting(ConfigSetting.PRESERVE_COMMENTS);
    }

    public Config(
        final String name,
        @Nullable final String path,
        @Nullable final InputStream inputStream,
        @Nullable final ReloadSetting reloadSetting,
        @Nullable final ConfigSetting configSetting,
        @Nullable final DataType dataType,
        @Nullable final Consumer<FlatFile> reloadConsumer) {
        super(name, path, inputStream, reloadSetting, configSetting, dataType, reloadConsumer);
        setConfigSetting(ConfigSetting.PRESERVE_COMMENTS);
    }

    public Config(final File file) {
        super(file);
    }

    // ----------------------------------------------------------------------------------------------------
    // Method overridden from Yaml
    // ----------------------------------------------------------------------------------------------------

    @Override
    public Config addDefaultsFromInputStream() {
        return (Config) super.addDefaultsFromInputStream();
    }

    @Override
    public Config addDefaultsFromInputStream(@Nullable final InputStream inputStream) {
        return (Config) super.addDefaultsFromInputStream(inputStream);
    }
}
