package io.github.milkdrinkers.crate;

import io.github.milkdrinkers.crate.internal.FileType;
import io.github.milkdrinkers.crate.internal.FlatFile;
import io.github.milkdrinkers.crate.internal.provider.InputStreamProvider;
import io.github.milkdrinkers.crate.internal.settings.ConfigSetting;
import io.github.milkdrinkers.crate.internal.settings.DataType;
import io.github.milkdrinkers.crate.internal.settings.ReloadSetting;
import io.github.milkdrinkers.crate.util.FileUtils;
import io.github.milkdrinkers.crate.util.Valid;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Abstract builder class for creating configuration file instances.
 * <p>
 * This builder provides a fluent API for configuring various aspects of configuration files,
 * including file location, data source, reload behavior, and other settings. The builder
 * pattern allows for flexible and readable configuration creation.
 * </p>
 * <p>
 * <strong>Implementation Notes:</strong>
 * <ul>
 *   <li>File locations can be specified as directories or complete file paths</li>
 *   <li>When a directory is specified, the file extension is automatically appended</li>
 *   <li>Data sources can be files, input streams, or embedded resources</li>
 *   <li>Null values are handled gracefully, typically by clearing the associated setting</li>
 * </ul>
 * </p>
 *
 * @param <T> the builder type (for method chaining)
 * @param <R> the result type produced by {@link #build()}
 * @author Crate Configuration Library
 * @since 4.0.0
 */
public abstract class AbstractConfigurationBuilder<T extends AbstractConfigurationBuilder<T, R>, R> {
    /**
     * Provider for creating input streams from various sources.
     */
    private final InputStreamProvider inputStreamProvider;

    /**
     * The file type/format this builder handles.
     */
    private final FileType fileType;

    /**
     * The configuration file name including extension.
     */
    private String fileName;

    /**
     * The directory path where the configuration file will be located.
     */
    private String directoryPath;

    /**
     * Optional input stream providing default/template data.
     */
    private InputStream defaultDataStream;

    /**
     * Configuration for automatic reloading behavior.
     */
    private ReloadSetting reloadSetting;

    /**
     * General configuration settings.
     */
    private ConfigSetting configSetting;

    /**
     * Data type handling configuration.
     */
    private DataType dataType;

    /**
     * Optional callback invoked when the configuration is reloaded.
     */
    private @Nullable Consumer<FlatFile> reloadCallback = null;

    /**
     * Constructs a new configuration builder.
     *
     * @param inputStreamProvider the provider for creating input streams
     * @param fileType the type of configuration file this builder handles
     * @throws IllegalArgumentException if either parameter is null
     * @since 4.0.0
     */
    protected AbstractConfigurationBuilder(final InputStreamProvider inputStreamProvider, final FileType fileType) {
        Valid.notNull(inputStreamProvider, "InputStreamProvider cannot be null");
        Valid.notNull(fileType, "FileType cannot be null");

        this.inputStreamProvider = inputStreamProvider;
        this.fileType = fileType;
    }

    // ====================================================================================================
    // Location Configuration Methods
    // ====================================================================================================

    /**
     * Sets the configuration file location using a string path.
     * <p>
     * The path can point to either a file or directory. If it's a directory,
     * the appropriate file extension will be automatically appended.
     * </p>
     *
     * @param path the file or directory path
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if path is null or empty
     * @since 4.0.0
     */
    public T path(String path) {
        Valid.notNull(path, "Path cannot be null or empty");
        Valid.checkBoolean(!path.isEmpty(), "Path cannot be null or empty");
        return path(new File(path));
    }

    /**
     * Sets the configuration file location using a Path object.
     *
     * @param path the file or directory path
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if path is null
     * @since 4.0.0
     */
    public T path(Path path) {
        Valid.notNull(path, "Path cannot be null");
        return path(path.toFile());
    }

    /**
     * Sets the configuration file location using a File object.
     * <p>
     * If the file represents a directory, the configuration file will be created
     * within that directory with the appropriate extension. If it represents a file,
     * that exact location will be used.
     * </p>
     *
     * @param file the file or directory
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if file is null or if a directory name contains dots
     * @since 4.0.0
     */
    public T path(File file) {
        Valid.notNull(file, "File cannot be null");

        if (!file.isDirectory()) {
            this.fileName = file.getName();
            this.directoryPath = FileUtils.getParentDirPath(file);
        } else {
            Valid.checkBoolean(!file.getName().contains("."),
                "Directory name must not contain '.' when used as file location");

            if (!file.exists()) {
                file.mkdirs();
            }

            this.fileName = file.getName() + "." + fileType.getExtension();
            this.directoryPath = file.getAbsolutePath();
        }
        return self();
    }

    /**
     * Sets the configuration file location using a URI.
     *
     * @param uri the file or directory URI
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if uri is null
     * @since 4.0.0
     */
    public T path(URI uri) {
        Valid.notNull(uri, "URI cannot be null");
        return path(Paths.get(uri));
    }

    /**
     * Sets the configuration file location in the specified directory with the given filename.
     *
     * @param directory the directory path
     * @param filename the configuration file name (extension will be added if missing)
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if directory or filename is null/empty
     * @since 4.0.0
     */
    public T path(String directory, String filename) {
        Valid.notNull(directory, "Directory cannot be null or empty");
        Valid.checkBoolean(!directory.isEmpty(), "Directory cannot be null or empty");
        Valid.notNull(filename, "Filename cannot be null or empty");
        Valid.checkBoolean(!filename.isEmpty(), "Filename cannot be null or empty");

        return path(new File(directory, ensureExtension(filename)));
    }

    /**
     * Sets the configuration file location in the specified directory with the given filename.
     *
     * @param directory the directory
     * @param filename the configuration file name (extension will be added if missing)
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if directory or filename is null/empty
     * @since 4.0.0
     */
    public T path(File directory, String filename) {
        Valid.notNull(directory, "Directory cannot be null");
        Valid.notNull(filename, "Filename cannot be null or empty");
        Valid.checkBoolean(!filename.isEmpty(), "Filename cannot be null or empty");

        return path(new File(directory, ensureExtension(filename)));
    }

    // ====================================================================================================
    // Default Data Source Methods
    // ====================================================================================================

    /**
     * Sets the default data source from a file path.
     * <p>
     * The default data will be used to populate the configuration if the target file
     * doesn't exist or is empty.
     * </p>
     *
     * @param path the path to the default data file, or null to clear
     * @return this builder instance for method chaining
     * @since 4.0.0
     */
    public T defaults(@Nullable final Path path) {
        if (path == null) {
            this.defaultDataStream = null;
            return self();
        }
        return defaults(path.toFile());
    }

    /**
     * Sets the default data source from a File object.
     *
     * @param file the default data file, or null to clear
     * @return this builder instance for method chaining
     * @since 4.0.0
     */
    public T defaults(@Nullable final File file) {
        if (file == null) {
            this.defaultDataStream = null;
            return self();
        }

        this.defaultDataStream = FileUtils.createInputStream(file);
        return self();
    }

    /**
     * Sets the default data source from a classpath resource.
     * <p>
     * The resource path should be relative to the classpath root.
     * </p>
     *
     * @param resourcePath the classpath resource path, or null/empty to clear
     * @return this builder instance for method chaining
     * @since 4.0.0
     */
    public T defaults(@Nullable final String resourcePath) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            this.defaultDataStream = null;
            return self();
        }

        final InputStream inputStream = this.inputStreamProvider.createInputStreamFromInnerResource(resourcePath);
        if (inputStream == null)
            throw new IllegalArgumentException("Resource not found: " + resourcePath);

        return defaults(inputStream);
    }

    /**
     * Sets the default data source from an InputStream.
     * <p>
     * <strong>Note:</strong> The caller is responsible for managing the lifecycle
     * of the provided InputStream. The stream will be read during configuration
     * creation and should remain open until then.
     * </p>
     *
     * @param inputStream the input stream providing default data, or null to clear
     * @return this builder instance for method chaining
     * @since 4.0.0
     */
    public T defaults(@Nullable final InputStream inputStream) {
        this.defaultDataStream = inputStream;
        return self();
    }

    /**
     * Sets the default data source from a URL.
     *
     * @param url the URL to read default data from, or null to clear
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if url is malformed
     * @since 4.0.0
     */
    public T defaults(@Nullable final URL url) {
        if (url == null) {
            this.defaultDataStream = null;
            return self();
        }

        try {
            this.defaultDataStream = url.openStream();
            return self();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to open stream from URL: " + url, e);
        }
    }

    // ====================================================================================================
    // Configuration Settings Methods
    // ====================================================================================================

    /**
     * Sets a callback to be invoked when the configuration is reloaded.
     * <p>
     * The callback receives a reference to the reloaded FlatFile instance,
     * allowing for custom post-reload processing.
     * </p>
     *
     * @param reloadCallback the callback to invoke on reload, or null to clear
     * @return this builder instance for method chaining
     * @since 4.0.0
     */
    public T reloadCallback(@Nullable final Consumer<FlatFile> reloadCallback) {
        this.reloadCallback = reloadCallback;
        return self();
    }

    /**
     * Configures general configuration behavior settings.
     *
     * @param configSetting the configuration settings to apply
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if configSetting is null
     * @since 4.0.0
     */
    public T config(@NonNull final ConfigSetting configSetting) {
        Valid.notNull(configSetting, "ConfigSetting cannot be null");
        this.configSetting = configSetting;
        return self();
    }

    /**
     * Configures automatic reload behavior settings.
     *
     * @param reloadSetting the reload settings to apply
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if reloadSetting is null
     * @since 4.0.0
     */
    public T reload(@NonNull final ReloadSetting reloadSetting) {
        Valid.notNull(reloadSetting, "ReloadSetting cannot be null");
        this.reloadSetting = reloadSetting;
        return self();
    }

    /**
     * Configures data type handling behavior.
     *
     * @param dataType the data type configuration to apply
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if dataType is null
     * @since 4.0.0
     */
    public T dataType(@NonNull final DataType dataType) {
        Valid.notNull(dataType, "DataType cannot be null");
        this.dataType = dataType;
        return self();
    }

    // ====================================================================================================
    // Protected Accessor Methods
    // ====================================================================================================

    /**
     * Returns the input stream provider for this builder.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the input stream provider
     * @since 4.0.0
     */
    protected InputStreamProvider getInputStreamProvider() {
        return inputStreamProvider;
    }

    /**
     * Returns the configured directory path.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the directory path, or null if not set
     * @since 4.0.0
     */
    protected String getDirectoryPath() {
        return directoryPath;
    }

    /**
     * Returns the configured file name.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the file name, or null if not set
     * @since 4.0.0
     */
    protected String getFileName() {
        return fileName;
    }

    /**
     * Returns the default data input stream.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the default data stream, or null if not set
     * @since 4.0.0
     */
    protected InputStream getDefaultDataStream() {
        return defaultDataStream;
    }

    /**
     * Returns the configured reload settings.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the reload settings, or null if not set
     * @since 4.0.0
     */
    protected ReloadSetting getReloadSetting() {
        return reloadSetting;
    }

    /**
     * Returns the configured general settings.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the config settings, or null if not set
     * @since 4.0.0
     */
    protected ConfigSetting getConfigSetting() {
        return configSetting;
    }

    /**
     * Returns the configured data type settings.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the data type settings, or null if not set
     * @since 4.0.0
     */
    protected DataType getDataType() {
        return dataType;
    }

    /**
     * Returns the configured reload callback.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the reload callback, or null if not set
     * @since 4.0.0
     */
    protected @Nullable Consumer<FlatFile> getReloadCallback() {
        return reloadCallback;
    }

    /**
     * Returns the file type this builder handles.
     * <p>
     * This method is intended for use by subclass implementations during
     * the configuration creation process.
     * </p>
     *
     * @return the file type
     * @since 4.0.0
     */
    protected FileType getFileType() {
        return fileType;
    }

    // ====================================================================================================
    // Abstract and Utility Methods
    // ====================================================================================================

    /**
     * Creates and returns the final configuration instance.
     * <p>
     * This method must be implemented by concrete builder subclasses to
     * instantiate the appropriate configuration type using the settings
     * configured through this builder.
     * </p>
     *
     * @return the created configuration instance
     * @throws IllegalStateException if required settings are missing
     * @since 4.0.0
     */
    public abstract R build();

    /**
     * Returns this builder instance with the correct generic type.
     * <p>
     * This method enables proper method chaining in subclasses by
     * ensuring the correct builder type is returned.
     * </p>
     *
     * @return this builder instance
     * @since 4.0.0
     */
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    /**
     * Ensures the given filename has the appropriate file extension.
     * <p>
     * If the filename already has the correct extension, it's returned unchanged.
     * Otherwise, the appropriate extension is appended.
     * </p>
     *
     * @param filename the filename to check
     * @return the filename with the correct extension
     * @since 4.0.0
     */
    private String ensureExtension(String filename) {
        String extension = "." + fileType.getExtension();
        return filename.endsWith(extension) ? filename : filename + extension;
    }
}
