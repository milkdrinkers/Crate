package io.github.milkdrinkers.crate;

import io.github.milkdrinkers.crate.internal.FileData;
import io.github.milkdrinkers.crate.internal.FileType;
import io.github.milkdrinkers.crate.internal.FlatFile;
import io.github.milkdrinkers.crate.internal.editor.toml.TomlManager;
import io.github.milkdrinkers.crate.internal.settings.ReloadSetting;
import io.github.milkdrinkers.crate.util.FileUtils;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

public class Toml extends FlatFile {

    public Toml(@NonNull final Toml toml) {
        super(toml.getFile());
        this.fileData = toml.getFileData();
        this.pathPrefix = toml.getPathPrefix();
    }

    public Toml(final String name, final String path) {
        this(name, path, null);
    }

    public Toml(final String name, final String path, final InputStream inputStream) {
        this(name, path, inputStream, null, null);
    }

    public Toml(
        @NonNull final String name,
        @NonNull final String path,
        @Nullable final InputStream inputStream,
        @Nullable final ReloadSetting reloadSetting,
        @Nullable final Consumer<FlatFile> reloadConsumer
    ) {
        super(name, path, FileType.TOML, reloadConsumer);

        if (create() && inputStream != null) {
            FileUtils.writeToFile(this.file, inputStream);
        }

        if (reloadSetting != null) {
            this.reloadSetting = reloadSetting;
        }

        forceReload();
    }

    public Toml(final File file) {
        super(file, FileType.TOML);
        create();
        forceReload();
    }

    // ----------------------------------------------------------------------------------------------------
    // Abstract methods to implement
    // ----------------------------------------------------------------------------------------------------

    @Override
    protected final Map<String, Object> readToMap() throws IOException {
        return TomlManager.read(getFile());
    }

    @Override
    protected final void write(final FileData data) {
        try {
            TomlManager.write(data.toMap(), getFile());
        } catch (final IOException ioException) {
            System.err.println("Exception while writing fileData to file '" + getName() + "'");
            System.err.println("In '" + FileUtils.getParentDirPath(this.file) + "'");
            ioException.printStackTrace();
        }
    }
}
