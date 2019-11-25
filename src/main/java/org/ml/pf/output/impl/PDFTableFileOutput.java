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
 * Note: this is an EXPERIMENTAL feature only (as of April 2019) with several know issues:
 * <p>
 * 1. Not fully validated ... right now it's more of a POC really
 * 2. No support for tables spanning multiple pages
 * 3. No support for multiple tables per document ... no idea yet how to handle this
 * 4. No concept yet of page width and how to set column widths to make sure all columns fit onto the page (potentially
 * going for landscape mode where necessary)
 * 5. No support for PDF page parameters (spacings, fonts, font sizes, colors, ...)
 * 6. It seems to be awfully slow ... probably due to the use of the builder pattern below; this is based on the examples of the
 * Java library used and may not be the smartest way to do this
 * <p>
 * A full-featured version would require a lot of work - and maybe just using a PDF printer together with HTML print views
 * is the better approach here.
 */
package org.ml.pf.output.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.ml.pf.output.IFileOutput;
import org.ml.pf.output.TableData;
import org.ml.table.Cell;
import org.ml.tools.Namespace;
import org.ml.tools.PropertyHolder;
import org.ml.tools.PropertyManager;
import org.ml.tools.logging.LoggerFactory;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;

/**
 * @author osboxes
 */
public class PDFTableFileOutput extends PropertyHolder implements IFileOutput<TableData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(PDFTableFileOutput.class.getName());
    public final static Namespace NAMESPACE = new Namespace("PDFTableFileOutput");
    private PageData pageData;
    private final static Color BLUE_DARK = new Color(76, 129, 190);
    private final static Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private final static Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    private final static Color ORANGE = new Color(255, 180, 0);
    private final static Color GRAY_LIGHT_1 = new Color(245, 245, 245);
    private final static Color GRAY_LIGHT_2 = new Color(240, 240, 240);
    private final static Color GRAY_LIGHT_3 = new Color(216, 216, 216);

    /**
     *
     */
    private enum fileType {
        pdf
    }

    /**
     *
     */
    public enum RequiredKey {
        baseDirectory
    }

    /**
     * @param propertyManager
     */
    public PDFTableFileOutput(PropertyManager propertyManager) {
        super(propertyManager);
        this.propertyManager.validateAllPropertyNames(NAMESPACE, RequiredKey.baseDirectory);
        pageData = new PageData();
    }

    /**
     * @param propertyManager
     * @param pageData
     */
    public PDFTableFileOutput(PropertyManager propertyManager, PageData pageData) {
        super(propertyManager);
        if (pageData == null) {
            throw new NullPointerException("pageData may not be null");
        }
        this.propertyManager.validateAllPropertyNames(NAMESPACE, RequiredKey.baseDirectory);
        this.pageData = pageData;
    }

    /**
     * @param tables
     */
    @Override
    public void put(Map<String, TableData> tables) {
        if (tables == null) {
            throw new NullPointerException("tables may not be null");
        }

        String baseDirectory = propertyManager.getProperty(NAMESPACE, RequiredKey.baseDirectory.toString());

        for (String fileNameKey : tables.keySet()) {

            //.... We need to make sure we have the proper file extension here
            String actualFileName = fileNameKey.replaceAll("\\..+$", "");
            File file = new File(baseDirectory + File.separatorChar + actualFileName + "." + fileType.pdf.toString());

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float PADDING = 50f;
            float startY = page.getMediaBox().getHeight() - PADDING;

            List<Table> tableList = createTables(tables.get(fileNameKey));

            try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                TableDrawer.builder()
                        .contentStream(contentStream)
                        .table(tableList.get(0))
                        .startX(PADDING)
                        .startY(startY)
                        .build()
                        .draw();

                startY -= (tableList.get(0).getHeight() + PADDING);

            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "{0}:{1}", new Object[]{ex.getClass(), ex.getMessage()});
            }

            try {
                document.save(file);
                document.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "{0}:{1}", new Object[]{ex.getClass(), ex.getMessage()});
            }

        }
    }

    /**
     * @return
     */
    private List<Table> createTables(TableData tableData) {
        if (tableData == null) {
            throw new NullPointerException("tableData may not be null");
        }

        List<Table> tableList = new ArrayList<>();

        for (String tableKey : tableData.getTables().keySet()) {
            org.ml.table.Table table = tableData.getTable(tableKey);
            Table.TableBuilder tableBuilder = Table.builder();
            for (int i = 0; i < table.getColNumber(); i++) {
                tableBuilder.addColumnOfWidth(50).fontSize(8).font(HELVETICA).borderColor(Color.WHITE);
            }

            //.... Add the header row
            Row.RowBuilder headerRow = Row.builder();
            int row = 0;
            int col = 0;
            for (int i = 0; i < table.getColNumber(); i++) {
                headerRow.add(CellText.builder().text(getText(table.getCell(row, col++))).horizontalAlignment(LEFT).borderWidth(1).build())
                        .backgroundColor(BLUE_DARK)
                        .textColor(Color.WHITE)
                        .font(PDType1Font.HELVETICA_BOLD).fontSize(6)
                        .horizontalAlignment(CENTER);
            }
            tableBuilder.addRow(headerRow.build());

            // ... Add data rows
            for (int r = 1; r < table.getRowNumber(); r++) {
                Row.RowBuilder dataRow = Row.builder();

                row++;
                col = 0;
                for (int i = 0; i < table.getColNumber(); i++) {
                    dataRow.add(CellText.builder().text(getText(table.getCell(row, col++))).horizontalAlignment(LEFT).borderWidth(1).build())
                            .backgroundColor(row % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
                            .textColor(Color.BLACK)
                            .font(PDType1Font.HELVETICA).fontSize(6)
                            .horizontalAlignment(CENTER);
                }
                tableBuilder.addRow(dataRow.build());
            }

            tableList.add(tableBuilder.build());

        }
        return tableList;
    }

    /**
     * @param cell
     * @return
     */
    private String getText(Cell cell) {
        if (cell == null) {
            throw new NullPointerException("cell may not be null");
        }
        try {

            Object content = cell.getContents();

            if (content instanceof Integer) {
                return String.valueOf((Integer) content);
            } else if (content instanceof Float) {
                return String.valueOf((Float) content);
            } else if (content instanceof Double) {
                return String.valueOf((Double) content);
            } else if (content instanceof Boolean) {
                return String.valueOf((Boolean) content);
            } else if (content instanceof String) {
                return (String) content;
            } else if (content != null) {
                return content.toString();
            }

        } catch (ClassCastException ex) {
            throw new UnsupportedOperationException(ex.getMessage() + " / Content value : " + cell.getContents());
        }
        return null;

    }
}
