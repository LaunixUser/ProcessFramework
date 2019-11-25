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

import org.ml.tools.velocity.VelocityFileType;
import org.ml.table.Cell;
import org.ml.table.content.EmailContent;
import org.ml.table.content.UrlContent;
import org.ml.table.render.IVelocityRenderer;

/**
 * @author mlaux
 */
public class SimpleVelocityRenderer implements IVelocityRenderer {

    public final static String DEFAULT_DOUBLE_FORMAT = "%.2f";
    public final static String DEFAULT_PERCENTAGE_FORMAT = "%.2f";

    private String doubleFormat = DEFAULT_DOUBLE_FORMAT;
    private String percentageFormat = DEFAULT_PERCENTAGE_FORMAT;

    /**
     *
     */
    public SimpleVelocityRenderer() {

    }

    /**
     * @param doubleFormat
     * @param percentageFormat
     */
    public SimpleVelocityRenderer(String doubleFormat, String percentageFormat) {
        if (doubleFormat == null) {
            throw new NullPointerException("doubleFormat may not be null");
        }
        if (percentageFormat == null) {
            throw new NullPointerException("percentageFormat may not be null");
        }
        this.doubleFormat = doubleFormat;
        this.percentageFormat = percentageFormat;
    }

    /**
     * For most CellDataTypes, the content is expected with the Key.CONTENT. For
     * CellDataTypes URL and EMAIL, the actual URL / Email addressed is expected
     * in Key.URL and Key.EMAIL, respectively. The link names (page reference /
     * email addressee) is expected in Key.CONTENT
     *
     * @param cell
     * @return
     */
    @Override
    public String renderCell(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell may not be null");
        }

        if (cell.getContent() != null) {

            Object content = cell.getContent();

            if (cell.isOfType(HINT_PERCENTAGE)) {

                double val = 0.0;
                if (content instanceof Double) {
                    val = 100.0 * (Double) content;
                } else if (content instanceof Integer) {
                    val = 100.0 * (Integer) content;
                } else {
                    throw new UnsupportedOperationException("content is of type " + HINT_PERCENTAGE + " and instance of " + content.getClass() + " - don't know how to handle this");
                }
                return String.format(percentageFormat, val) + "%";

            } else {

                if (content instanceof Integer) {
                    return String.valueOf((Integer) content);
                } else if (content instanceof String) {
                    return ((String) content).replaceAll("\n", "<br/>");
                } else if (content instanceof Float) {
                    return String.format(doubleFormat, (Float) content);
                } else if (content instanceof Double) {
                    return String.format(doubleFormat, (Double) content);
                } else if (content instanceof Boolean) {
                    return String.valueOf((Boolean) content);
                } else if (content instanceof EmailContent) {
                    String address = ((EmailContent) content).getAddress();
                    return "<a href=\"mailto:" + address + "\">" + address + "</a>";
                } else if (content instanceof UrlContent) {
                    UrlContent urlContent = (UrlContent) content;
                    return "<a href=\"" + urlContent.getAddress() + VelocityFileType.html.getExtension() + "\">" + urlContent.getText() + "</a> " + urlContent.getDescription();
                } else {
                    return content.toString();
                }
            }
        } else {

            return "";

        }
    }

}
