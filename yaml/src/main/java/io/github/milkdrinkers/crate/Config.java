package io.github.milkdrinkers.crate;

import io.github.milkdrinkers.crate.internal.FileType;
import io.github.milkdrinkers.crate.internal.FlatFile;
import io.github.milkdrinkers.crate.internal.provider.yaml.CrateProviders;
import io.github.milkdrinkers.crate.internal.settings.ConfigSetting;
import io.github.milkdrinkers.crate.internal.settings.DataType;
import io.github.milkdrinkers.crate.internal.settings.ReloadSetting;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.function.Consumer;

@SuppressWarnings({"unused"})
public class Config extends Yaml {
    Config(
        final String name,
        @Nullable final String path,
        @Nullable final InputStream inputStream,
        @Nullable final ReloadSetting reloadSetting,
        @Nullable final ConfigSetting configSetting,
        @Nullable final DataType dataType,
        @Nullable final Consumer<FlatFile> reloadConsumer) {
        super(name, path, inputStream, reloadSetting, configSetting, dataType, reloadConsumer);
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

    /**
     * A builder to build a new Config instance.
     *
     * @return A new Builder instance.
     */
    public static Builder builderConfig() {
        return new Builder().config(ConfigSetting.PRESERVE_COMMENTS).dataType(DataType.SORTED);
    }

    public static class Builder extends AbstractConfigurationBuilder<Builder, Config> {
        private Builder() {
            super(CrateProviders.inputStreamProvider(), FileType.YAML);
        }

        public Config build() {
            return new Config(
                super.getFileName(),
                super.getDirectoryPath(),
                super.getDefaultDataStream(),
                super.getReloadSetting(),
                super.getConfigSetting(),
                super.getDataType(),
                super.getReloadCallback()
            );
        }
    }
}
