package io.github.milkdrinkers.crate.util;

import io.github.milkdrinkers.crate.internal.FlatFile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FlatFileConverter {

    public void addAllData(final FlatFile source, final FlatFile destination) {
        destination.getFileData().clear();
        destination.getFileData().loadData(source.getFileData().toMap());
        destination.write();
    }
}
