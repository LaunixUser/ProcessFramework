package org.ml.pf.step.impl.excel;

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
 * Read all the Excel files from a given path into Workbook instances. A file
 * filter can be applied before actually reading the files.
 */

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ml.tools.excel.ExcelDatabaseHandler;
import org.ml.tools.logging.LoggerFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.ml.pf.step.AbstractTransferProcessStep;
import org.ml.pf.step.impl.tool.TableQuery;

/**
 * @author osboxes
 */
public class ExcelReadWorkbooksDatabaseStep extends AbstractTransferProcessStep<TableQuery, Map<String, Workbook>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelReadWorkbooksDatabaseStep.class.getName());

    /**
     * @param id
     */
    public ExcelReadWorkbooksDatabaseStep(String id) {
        super(id);
    }

    /**
     *
     */
    public ExcelReadWorkbooksDatabaseStep() {
    }

    /**
     * @param queryData
     * @return
     */
    @Override
    public Map<String, Workbook> convert(TableQuery queryData) {
        if (queryData == null) {
            throw new NullPointerException("queryData may not be null");
        }
        ExcelDatabaseHandler handler = new ExcelDatabaseHandler(queryData.getConnectionData());
        Map<String, Workbook> workbooks = null;
        try {
            workbooks = handler.loadExcelsFromDatabase(queryData.getTableNames());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "{0}:{1}", new Object[]{ex.getClass(), ex.getMessage()});
        }
        return workbooks;

    }

}
