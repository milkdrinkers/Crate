package io.github.milkdrinkers.crate;

import io.github.milkdrinkers.crate.internal.FileData;
import io.github.milkdrinkers.crate.internal.FileType;
import io.github.milkdrinkers.crate.internal.FlatFile;
import io.github.milkdrinkers.crate.internal.provider.json.CrateProviders;
import io.github.milkdrinkers.crate.internal.settings.ReloadSetting;
import io.github.milkdrinkers.crate.util.FileUtils;
import io.github.milkdrinkers.crate.util.JsonUtils;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public class Json extends FlatFile {
    Json(final String name,
                @Nullable final String path,
                @Nullable final InputStream inputStream,
                @Nullable final ReloadSetting reloadSetting,
                @Nullable final Consumer<FlatFile> reloadConsumer) {
        super(name, path, FileType.JSON, reloadConsumer);

        if ((create() || this.file.length() == 0) && inputStream != null) {
            FileUtils.writeToFile(this.file, inputStream);
        }

        if (reloadSetting != null) {
            this.reloadSetting = reloadSetting;
        }
        forceReload();
    }

    // ----------------------------------------------------------------------------------------------------
    // Methods to override (Points where JSON is unspecific for typical FlatFiles)
    // ----------------------------------------------------------------------------------------------------

    /**
     * Gets a Map by key Although used to get nested objects {@link Json}
     *
     * @param key Path to Map-List in JSON
     * @return Map
     */
    @Override
    public final Map<?, ?> getMap(final String key) {
        val finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
        if (!contains(finalKey)) {
            return new HashMap<>();
        } else {
            val map = get(key);
            if (map instanceof Map) {
                return (Map<?, ?>) this.fileData.get(key);
            } else if (map instanceof JSONObject) {
                return ((JSONObject) map).toMap();
            }
            // Exception in casting
            throw new IllegalArgumentException(
                "ClassCastEx: Json contains key: '" + key + "' but it is not a Map");
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // Abstract methods to implement
    // ----------------------------------------------------------------------------------------------------

    @Override
    protected final Map<String, Object> readToMap() throws IOException {
        if (this.file.length() == 0) {
            Files.write(this.file.toPath(), Collections.singletonList("{}"));
        }

        JSONTokener jsonTokener = new JSONTokener(FileUtils.createInputStream(this.file));
        return new JSONObject(jsonTokener).toMap();
    }

    @Override
    protected final void write(final FileData data) throws IOException {
        FileUtils.writer(this.file, writer -> {
            writer.write(JsonUtils.getJsonFromMap(data.toMap()).toString(3));
            writer.flush();
        });
    }

    /**
     * A builder to build a new Json instance.
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractConfigurationBuilder<Builder, Json> {
        private Builder() {
            super(CrateProviders.inputStreamProvider(), FileType.JSON);
        }

        public Json build() {
            return new Json(
                super.getFileName(),
                super.getDirectoryPath(),
                super.getDefaultDataStream(),
                super.getReloadSetting(),
                super.getReloadCallback()
            );
        }
    }
}
