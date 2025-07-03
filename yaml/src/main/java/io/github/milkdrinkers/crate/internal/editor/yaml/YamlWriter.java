package io.github.milkdrinkers.crate.internal.editor.yaml;

import org.snakeyaml.engine.v2.api.StreamDataWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * A writer for YAML data that implements the StreamDataWriter interface from SnakeYAML.
 * It provides methods to write strings to a YAML output stream and handle IOExceptions.
 * @since 4.0.0
 */
public final class YamlWriter implements StreamDataWriter {
    private final Writer writer;

    public YamlWriter(Writer writer) {
        this.writer = writer;
    }

    private void processIOException(IOException e) {
        // TODO Implement internal exception handler
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            processIOException(e);
        }
    }

    @Override
    public void write(String str, int off, int len) {
        try {
            writer.write(str, off, len);
        } catch (IOException e) {
            processIOException(e);
        }
    }

    @Override
    public void write(String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            processIOException(e);
        }
    }
}
