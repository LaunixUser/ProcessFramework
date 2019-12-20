package org.ml.pf.step.impl.tool;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dr. Matthias Laux
 */
public class FileNameStartsWithFilter implements IFileFilter {

    private Set<String> acceptedStrings;

    /**
     * @param acceptedStrings
     */
    public FileNameStartsWithFilter(String... acceptedStrings) {
        if (acceptedStrings == null) {
            throw new NullPointerException("acceptedStrings may not be null");
        }
        this.acceptedStrings = new HashSet<>();
        Collections.addAll(this.acceptedStrings, acceptedStrings);
    }

    /**
     * @param acceptedStrings
     */
    public FileNameStartsWithFilter(Set<String> acceptedStrings) {
        if (acceptedStrings == null) {
            throw new NullPointerException("acceptedStrings may not be null");
        }
        this.acceptedStrings = acceptedStrings;
    }

    /**
     * @param path
     * @return
     */
    @Override
    public boolean accept(Path path) {
        if (path == null) {
            throw new NullPointerException("path may not be null");
        }
        String fileName = path.getFileName().toString();
        for (String s : acceptedStrings) {
            if (fileName.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

}
