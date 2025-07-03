package io.github.milkdrinkers.crate.internal.provider;

import io.github.milkdrinkers.crate.internal.settings.DataType;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.common.ScalarStyle;

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
    private LoadSettings yamlLoaderOptions;
    @Setter
    private LoadSettings yamlLoaderOptionsNoComments;
    @Setter
    private DumpSettings yamlDumperOptions;
    @Setter
    private DumpSettings yamlDumperOptionsNoComments;
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

    public LoadSettings yamlLoaderOptions() {
        if (yamlLoaderOptions == null) {
            yamlLoaderOptions = LoadSettings.builder()
                .setAllowRecursiveKeys(true)
                .setParseComments(true)
                .build();
        }
        return yamlLoaderOptions;
    }

    public LoadSettings yamlLoaderOptionsNoComments() {
        if (yamlLoaderOptions == null) {
            yamlLoaderOptions = LoadSettings.builder()
                .setAllowRecursiveKeys(true)
                .setParseComments(false)
                .build();
        }
        return yamlLoaderOptions;
    }

    public DumpSettings yamlDumperOptions() {
        if (yamlDumperOptions == null) {
            yamlDumperOptions = DumpSettings.builder()
                .setDumpComments(true)
                .setUseUnicodeEncoding(true)
                .setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED)
                .setIndentWithIndicator(false)
                .setIndent(2)
                .setIndicatorIndent(0)
                .setDereferenceAliases(true)
                .setSplitLines(false)
                .build();
        }
        return yamlDumperOptions;
    }

    public DumpSettings yamlDumperOptionsNoComments() {
        if (yamlDumperOptions == null) {
            yamlDumperOptions = DumpSettings.builder()
                .setDumpComments(false)
                .setUseUnicodeEncoding(true)
                .setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED)
                .setIndentWithIndicator(false)
                .setIndent(2)
                .setIndicatorIndent(0)
                .setDereferenceAliases(true)
                .setSplitLines(false)
                .build();
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
