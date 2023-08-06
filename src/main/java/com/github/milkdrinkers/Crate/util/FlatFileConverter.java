package com.github.milkdrinkers.Crate.util;

import com.github.milkdrinkers.Crate.internal.FlatFile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FlatFileConverter {

    public void addAllData(final FlatFile source, final FlatFile destination) {
        destination.getFileData().clear();
        destination.getFileData().loadData(source.getFileData().toMap());
        destination.write();
    }
}
