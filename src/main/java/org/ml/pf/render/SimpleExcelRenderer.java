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
package org.ml.pf.render;

import org.apache.poi.ss.usermodel.Cell;
import org.ml.table.content.EmailContent;
import org.ml.table.content.UrlContent;
import org.ml.table.render.IExcelRenderer;

/**
 * @author mlaux
 */
public class SimpleExcelRenderer implements IExcelRenderer {

    /**
     * @param cell
     * @param dataCell
     */
    @Override
    public void renderCell(Cell cell, org.ml.table.Cell dataCell) {
        if (cell == null) {
            throw new NullPointerException("cell may not be null");
        }
        if (dataCell == null) {
            throw new NullPointerException("dataCell may not be null");
        }

        try {

            if (dataCell.getContent() != null) {

                Object content = dataCell.getContent();

                if (content instanceof Integer) {
                    cell.setCellValue((Integer) content);
                } else if (content instanceof Float) {
                    cell.setCellValue((Float) content);
                } else if (content instanceof Double) {
                    cell.setCellValue((Double) content);
                } else if (content instanceof Boolean) {
                    cell.setCellValue((Boolean) content);
                } else if (content instanceof String) {
                    cell.setCellValue((String) content);
                } else if (content instanceof EmailContent) {
                    String address = ((EmailContent) content).getAddress();
                    cell.setCellValue("mailto:" + address);
                } else if (content instanceof UrlContent) {
                    UrlContent urlContent = (UrlContent) content;
                    cell.setCellValue(urlContent.getText());
                } else {
                    cell.setCellValue(content.toString());
                }

            }

        } catch (ClassCastException ex) {
            throw new UnsupportedOperationException(ex.getMessage() + " / Content value : " + dataCell.getContent());
        }
    }
}
