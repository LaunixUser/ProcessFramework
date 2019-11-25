/*
 * The MIT License
 *
 * Copyright 2019 Dr. Matthias Laux.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
