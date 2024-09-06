package com.github.milkdrinkers.crate.internal.editor.yaml;

import com.github.milkdrinkers.crate.util.FileUtils;
import com.github.milkdrinkers.crate.util.YamlUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class YamlEditor {

    private final File file;

    public List<String> read() {
        return FileUtils.readAllLines(file);
    }

    // ----------------------------------------------------------------------------------------------------
    // Reading specific things from File
    // ----------------------------------------------------------------------------------------------------

    public List<String> readKeys() {
        return YamlUtils.getKeys(read());
    }

    public List<String> readComments() {
        return YamlUtils.getKeys(read());
    }

    public List<String> readHeader() {
        return YamlUtils.getHeaderFromLines(read());
    }

    public List<String> readFooter() {
        return YamlUtils.getFooterFromLines(read());
    }

    public List<String> readPureComments() {
        return YamlUtils.getPureCommentsFromLines(read());
    }

    public List<String> readWithoutHeaderAndFooter() {
        return YamlUtils.getLinesWithoutFooterAndHeaderFromLines(read());
    }

    // ----------------------------------------------------------------------------------------------------
    // Writing specific things from File
    // ----------------------------------------------------------------------------------------------------
    public void write(final List<String> lines) {
        FileUtils.write(file, lines);
    }

    public void setHeader(final List<String> header) {
        final List<String> lines = read();

        // Remove old header
        lines.removeAll(readHeader());

        // Adding new header in front
        for (int i = 0; i < header.size(); i++) {
            final String toAdd = header.get(i);
            lines.add(i, toAdd.startsWith("#") ? toAdd : "#" + toAdd);
        }

        // Write to file
        write(lines);
    }

    public void addHeader(final List<String> header) {
        final List<String> lines = read();
        for (int i = 0; i < header.size(); i++) {
            final String toAdd = header.get(i);
            lines.add(i, toAdd.startsWith("#") ? toAdd : "#" + toAdd);
        }

        // Write to file
        write(lines);
    }
}
