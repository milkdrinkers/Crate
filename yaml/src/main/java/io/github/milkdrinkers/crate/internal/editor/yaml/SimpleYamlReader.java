package io.github.milkdrinkers.crate.internal.editor.yaml;

/**
 * Enhanced Version of YamlReader of EsotericSoftware, which implements {@link AutoCloseable}
 */
/*public class SimpleYamlReader
    extends YamlReader
    implements AutoCloseable {

    public SimpleYamlReader(final Reader reader) {
        super(reader);
    }

    public SimpleYamlReader(final File file) {
        super(FileUtils.createReader(file));
    }

    public SimpleYamlReader(final String yaml) {
        super(yaml);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> readToMap() throws YamlException {
        final Object obj = read();
        return obj == null ? new HashMap<>() : (Map<String, Object>) obj;
    }
}*/
