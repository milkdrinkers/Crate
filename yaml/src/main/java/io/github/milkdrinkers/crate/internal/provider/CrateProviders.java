package io.github.milkdrinkers.crate.internal.provider;

import io.github.milkdrinkers.crate.internal.settings.DataType;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;

/**
 * Interface for registering more powerful Map/List implementation than the default JDK one's
 * examples for these implementations are FastUtils {@literal &} Trove Used in {@link DataType} Enum
 */
@UtilityClass
@Accessors(fluent = true, chain = true)
public class CrateProviders {

    @Setter
    private MapProvider mapProvider;
    @Setter
    private LoaderOptions yamlLoaderOptions;
    @Setter
    private LoaderOptions yamlLoaderOptionsNoComments;
    @Setter
    private DumperOptions yamlDumperOptions;
    @Setter
    private DumperOptions yamlDumperOptionsNoComments;
    @Setter
    private InputStreamProvider inputStreamProvider;
    @Setter
    private ExceptionHandler exceptionHandler;

    public MapProvider mapProvider() {
        if (mapProvider == null) {
            mapProvider = new MapProvider() {
            };
        }
        return mapProvider;
    }
    public LoaderOptions yamlLoaderOptions() {
        if (yamlLoaderOptions == null) {
            LoaderOptions options = new LoaderOptions();
            options.setAllowRecursiveKeys(true);
            options.setProcessComments(true);
            yamlLoaderOptions = options;
        }
        return yamlLoaderOptions;
    }

    public LoaderOptions yamlLoaderOptionsNoComments() {
        if (yamlLoaderOptions == null) {
            LoaderOptions options = new LoaderOptions();
            options.setAllowRecursiveKeys(true);
            options.setProcessComments(false);
            yamlLoaderOptions = options;
        }
        return yamlLoaderOptions;
    }

    public DumperOptions yamlDumperOptions() {
        if (yamlDumperOptions == null) {
            DumperOptions options = new DumperOptions();
            options.setProcessComments(true);
            options.setAllowUnicode(true);
            options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            options.setIndentWithIndicator(false);
            options.setIndent(2);
            options.setIndicatorIndent(0);
            options.setDereferenceAliases(true);
            options.setSplitLines(false);
            yamlDumperOptions = options;
        }
        return yamlDumperOptions;
    }

    public DumperOptions yamlDumperOptionsNoComments() {
        if (yamlDumperOptions == null) {
            DumperOptions options = new DumperOptions();
            options.setProcessComments(false);
            options.setAllowUnicode(true);
            options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            options.setIndentWithIndicator(false);
            options.setIndent(2);
            options.setIndicatorIndent(0);
            options.setDereferenceAliases(true);
            options.setSplitLines(false);
            yamlDumperOptions = options;
        }
        return yamlDumperOptions;
    }

    public InputStreamProvider inputStreamProvider() {
        if (inputStreamProvider == null) {
            inputStreamProvider = new InputStreamProvider() {
            };
        }

        return inputStreamProvider;
    }

    public ExceptionHandler exceptionHandler() {
        if (exceptionHandler == null) {
            exceptionHandler = new ExceptionHandler() {
            };
        }

        return exceptionHandler;
    }
}
