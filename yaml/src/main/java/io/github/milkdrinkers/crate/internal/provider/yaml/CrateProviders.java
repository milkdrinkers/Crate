package io.github.milkdrinkers.crate.internal.provider.yaml;

import io.github.milkdrinkers.crate.internal.provider.ExceptionHandler;
import io.github.milkdrinkers.crate.internal.provider.InputStreamProvider;
import io.github.milkdrinkers.crate.internal.settings.DataType;
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
    private LoadSettings yamlLoaderOptions;
    private DumpSettings yamlDumperOptions;
    private DumpSettings yamlDumperOptionsNoComments;
    private InputStreamProvider inputStreamProvider;
    private ExceptionHandler exceptionHandler;

    public LoadSettings yamlLoaderOptions() {
        if (yamlLoaderOptions == null) {
            yamlLoaderOptions = LoadSettings.builder()
                .setAllowRecursiveKeys(true)
                .setParseComments(true)
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
        if (yamlDumperOptionsNoComments == null) {
            yamlDumperOptionsNoComments = DumpSettings.builder()
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
        return yamlDumperOptionsNoComments;
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
