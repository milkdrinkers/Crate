package com.github.milkdrinkers.Crate.internal.editor.yaml;

import com.github.milkdrinkers.Crate.util.FileUtils;
import com.osiris.dyml.exceptions.YamlReaderException;

import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced Version of YamlReader of EsotericSoftware, which implements {@link AutoCloseable}
 */
public class DymlYamlReader
    extends YamlR
    implements AutoCloseable {

    public DymlYamlReader(final Reader reader) {
        super(reader);
    }

    public DymlYamlReader(final File file) {
        super(FileUtils.createReader(file));
    }

    public DymlYamlReader(final String yaml) {
        super(yaml);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> readToMap() throws YamlReaderException {
        final Object obj = read();
        return obj == null ? new HashMap<>() : (Map<String, Object>) obj;
    }
}
