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
package org.ml.pf.output.impl;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.ml.pf.output.IFileOutput;
import org.ml.pf.output.TableData;
import org.ml.pf.render.SimpleExcelRenderer;
import org.ml.table.render.RenderingContext;
import org.ml.tools.PropertyHolder;
import org.ml.tools.PropertyManager;
import org.ml.tools.excel.ExcelFileType;
import org.ml.tools.excel.ExcelTools;
import org.ml.tools.logging.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author osboxes
 */
public class ExcelTableFileOutput extends PropertyHolder implements IFileOutput<TableData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelTableFileOutput.class.getName());
    private ExcelFileType excelFileType = ExcelFileType.xlsx;

    /**
     *
     */
    public enum RequiredKey {
        baseDirectory, subDirectory
    }

    /**
     *
     */
    public enum OptionalKey {
        excelFileType
    }

    /**
     * @param propertyManager
     */
    public ExcelTableFileOutput(PropertyManager propertyManager) {
        super(propertyManager);
        this.propertyManager.validateAllPropertyNames(RequiredKey.baseDirectory);
        if (this.propertyManager.containsProperty(OptionalKey.excelFileType.toString())) {
            excelFileType = ExcelFileType.valueOf(this.propertyManager.getProperty(OptionalKey.excelFileType.toString()));
        }
    }

    /**
     * @param tables
     */
    @Override
    public void put(Map<String, TableData> tables) {
        if (tables == null) {
            throw new NullPointerException("tableData may not be null");
        }
        String baseDirectory = propertyManager.getProperty(RequiredKey.baseDirectory.toString());
        String subDirectory = propertyManager.getProperty(RequiredKey.subDirectory.toString());
        ExcelHelper excelHelper = new ExcelHelper();

        for (String fileNameKey : tables.keySet()) {

            String rootFileName = fileNameKey.replaceAll("\\..+$", "");   // Remove extension (if any)
            File file = new File(baseDirectory + File.separatorChar + subDirectory + File.separatorChar + rootFileName + "." + excelFileType.toString());

            Workbook workbook = ExcelTools.getNewWorkbook(excelFileType);
            CellStyle cs = workbook.createCellStyle();
            cs.setWrapText(true);
            cs.setVerticalAlignment(VerticalAlignment.TOP);

            TableData tableData = tables.get(fileNameKey);

            //.... Add a renderer to the tables in the tableData
            //     If we want to allow for other renderers, we need to add some generic coding here
            for (String tableKey : tableData.getTables().keySet()) {
                if (!tableData.getTable(tableKey).hasRenderer(RenderingContext.excel)) {
                    tableData.getTable(tableKey).addRenderer(RenderingContext.excel, new SimpleExcelRenderer());
                }
            }

            //.... Add the table to the workbook, one table per sheet
            for (String sheetName : tableData.getTables().keySet()) {
                excelHelper.addTable(workbook, tableData.getTable(sheetName), sheetName);
            }

            //.... Check if the base directory exists; create it if not
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            //.... Write the data to the file now
            try (FileOutputStream stream = new FileOutputStream(file)) {

                LOGGER.log(Level.INFO, "Writing output file {0}", file);
                workbook.write(stream);
                stream.flush();

            } catch (IOException ex) {

                LOGGER.log(Level.SEVERE, "{0}:{1}", new Object[]{ex.getClass(), ex.getMessage()});

            }

        }

    }
}
