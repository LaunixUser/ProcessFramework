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

import java.util.Map;

import org.ml.tools.excel.ExcelFileType;
import org.ml.tools.excel.ExcelTools;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.ml.table.Table;
import org.ml.table.render.IExcelRenderer;
import org.ml.table.render.RenderingContext;

/**
 * @author mlaux
 */
public class ExcelHelper {

    /**
     * This does a rough conversion of one or more tables to sheets a POI
     * workbook. Each table is written to a sheet with the given sheet name. The
     * content is stored in the table cells as content, and the content written
     * to the Excel cell is selected via the given key (only one content element
     * can be selected).
     * <p>
     * A cell type can be assigned to the table cell, and this type will be
     * picked up during output to do formatting. Only the types represented by
     * the DataType enum are supported, and the default is #DataType.STRING (if
     * no type has been specified). The idea is that the content is picked up
     * from the contents area of the cell via the key given here and some
     * formatting is applied if the cell is of a given type (which is one of
     * DataType).
     * <p>
     * The effect is that, for example, if a type of #DataType.DOUBLE is
     * assigned to the cell, the content will be written as double to the Excel
     * cell such that it also appears in the sheet as a real number, not a
     * string.
     *
     * @param type
     * @param tables The table(s) to convert with the desired sheet name as key
     * @return The POI workbook
     */
    public Workbook addTables(ExcelFileType type, Map<String, Table> tables) {
        if (type == null) {
            throw new NullPointerException("type may not be null");
        }
        return addTables(ExcelTools.getNewWorkbook(type), tables);
    }

    /**
     * @param workbook
     * @param tables   Sheet names are the keys here
     * @return
     */
    public Workbook addTables(Workbook workbook, Map<String, Table> tables) {
        if (workbook == null) {
            throw new IllegalArgumentException("workbook may not be null");
        }
        if (tables == null) {
            throw new IllegalArgumentException("tables may not be null");
        }
        for (String sheetName : tables.keySet()) {
            addTable(workbook, tables.get(sheetName), sheetName);
        }
        return workbook;

    }

    /**
     * @param workbook
     * @param table
     * @param sheetName
     * @return
     */
    public Workbook addTable(Workbook workbook, Table table, String sheetName) {
        if (workbook == null) {
            throw new IllegalArgumentException("workbook may not be null");
        }
        if (table == null) {
            throw new IllegalArgumentException("table may not be null");
        }
        if (sheetName == null) {
            throw new NullPointerException("sheetName may not be null");
        }

        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);
        cs.setVerticalAlignment(VerticalAlignment.TOP);

        Sheet sheet = workbook.createSheet(sheetName);

        for (int r = table.getRow0(); r <= table.getRowEnd(); r++) {
            Row row = sheet.createRow(r);
            for (int c = table.getCol0(); c <= table.getColEnd(); c++) {

                Cell cell = row.createCell(c);
                cell.setCellStyle(cs);
                org.ml.table.Cell dataCell = table.getCell(r, c);
                ((IExcelRenderer) table.getRenderer(RenderingContext.excel)).renderCell(cell, dataCell);

                /**
                 * try {
                 *
                 * if (dataCell.getContent() != null) {
                 *
                 * Object content = dataCell.getContent();
                 *
                 * if (content instanceof Integer) { cell.setCellValue((Integer)
                 * content); } else if (content instanceof Float) {
                 * cell.setCellValue((Float) content); } else if (content
                 * instanceof Double) { cell.setCellValue((Double) content); }
                 * else if (content instanceof Boolean) {
                 * cell.setCellValue((Boolean) content); } else if (content
                 * instanceof String) { cell.setCellValue((String) content); }
                 * else if (content instanceof EmailContent) { String address =
                 * ((EmailContent) content).getAddress();
                 * cell.setCellValue("mailto:" + address); } else if (content
                 * instanceof UrlContent) { UrlContent urlContent = (UrlContent)
                 * content; cell.setCellValue(urlContent.getText()); } else {
                 * cell.setCellValue(content.toString()); }
                 *
                 * }
                 *
                 * } catch (ClassCastException ex) { throw new
                 * UnsupportedOperationException(ex.getMessage() + " / Content
                 * value : " + dataCell.getContents()); }
                 */
            }
        }
        return workbook;

    }

}
