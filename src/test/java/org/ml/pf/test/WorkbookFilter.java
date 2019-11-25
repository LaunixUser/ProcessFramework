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
package org.ml.pf.test;

import org.apache.poi.ss.usermodel.Workbook;
import org.ml.pf.filter.IFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author osboxes
 */
public class WorkbookFilter implements IFilter<Map<String, Workbook>> {

    private Set<String> acceptIDs;

    /**
     * @param rootNodeID
     * @param acceptIDs
     */
    public WorkbookFilter(String rootNodeID, Set<String> acceptIDs) {
        if (rootNodeID == null) {
            throw new NullPointerException("rootNodeID may not be null");
        }
        if (acceptIDs == null) {
            throw new NullPointerException("acceptIDs may not be null");
        }
        this.acceptIDs = acceptIDs;
        acceptIDs.add(rootNodeID);
    }

    /**
     * @param data
     * @return
     */
    @Override
    public Map<String, Workbook> apply(Map<String, Workbook> data) {
        Map<String, Workbook> response = new HashMap<>();
        for (String fileName : data.keySet()) {
            for (String acceptID : acceptIDs) {
                if (fileName.startsWith(acceptID)) {
                    response.put(fileName, data.get(fileName));
                    break;
                }
            }
        }
        return response;
    }

}
