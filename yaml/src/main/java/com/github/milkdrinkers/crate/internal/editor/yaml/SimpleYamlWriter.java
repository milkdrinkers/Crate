package com.github.milkdrinkers.crate.internal.editor.yaml;

import com.github.milkdrinkers.crate.internal.provider.CrateProviders;
import com.github.milkdrinkers.crate.util.FileUtils;

import java.io.File;
import java.io.Writer;

/**
 * Enhanced Version of YamlWriter of EsotericSoftware, which implements {@link AutoCloseable}
 */
/*public class SimpleYamlWriter extends YamlWriter implements AutoCloseable {

    public SimpleYamlWriter(final Writer writer) {
        super(writer, CrateProviders.yamlConfig());
    }

    public SimpleYamlWriter(final File file) {
        this(FileUtils.createWriter(file));
    }
}*/
