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
/**
 * Read one Excel file form the given path into a workbook instance
 */
package org.ml.pf.step.impl.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.ml.pf.step.AbstractTransferProcessStep;
import org.ml.tools.excel.ExcelTools;

import java.nio.file.Path;

/**
 * @author osboxes
 */
public class ExcelReadWorkbookPathStep extends AbstractTransferProcessStep<Path, Workbook> {

    /**
     * @param id
     */
    public ExcelReadWorkbookPathStep(String id) {
        super(id);
    }

    /**
     *
     */
    public ExcelReadWorkbookPathStep() {
    }

    /**
     * @param path
     * @return
     */
    @Override
    public Workbook convert(Path path) {
        if (path == null) {
            throw new NullPointerException("path may not be null");
        }
        return ExcelTools.getWorkbook(path);
    }

}
