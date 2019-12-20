package org.ml.pf.step.impl.tool;

import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * @author Dr. Matthias Laux
 */
public class FileNamePatternMatchFilter implements IFileFilter {

    private Pattern pattern;

    /**
     * @param matchString
     */
    public FileNamePatternMatchFilter(String matchString) {
        if (matchString == null) {
            throw new NullPointerException("matchString may not be null");
        }
        pattern = Pattern.compile(matchString);
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
        return pattern.matcher(path.getFileName().toString()).matches();
    }

}
