package org.ml.pf.step.impl.tool;

import java.nio.file.Path;

/**
 * This can be used wherever a file path needs to be filtered by some criteria. Typically
 * this is relevant when finding files in a directory in an import step

* @author Dr. Matthias Laux
 */
public interface IFileFilter {

    /**
     * @param path
     * @return
     */
    boolean accept(Path path);
}
