package org.ml.pf.output;

import java.util.Map;

/**
 * Implementations accept a map of file names as keys and data objects (as values)
 * to be written to these files
 *
 * @param <U> The type of data to be written to each of the files
 * @author osboxes
 */
public interface IFileOutput<U> extends IOutput<Map<String, U>> {

    /**
     * @param data
     */
    @Override
    void put(Map<String, U> data);
}
