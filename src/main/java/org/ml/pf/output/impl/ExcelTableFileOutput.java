package org.ml.pf.output.impl;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.ml.pf.output.IFileOutput;
import org.ml.pf.output.TableData;
import org.ml.table.render.RenderingContext;
import org.ml.tools.PropertyHolder;
import org.ml.tools.PropertyManager;
import org.ml.tools.excel.ExcelTools;
import org.ml.tools.logging.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.ml.table.Table;
import org.ml.table.render.IExcelRenderer;
import org.ml.table.render.impl.SimpleExcelRenderer;
import org.ml.tools.FileType;

/**
 * @author osboxes
 */
public class ExcelTableFileOutput extends PropertyHolder implements IFileOutput<TableData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelTableFileOutput.class.getName());
    private FileType excelFileType = FileType.XLSX;

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
            excelFileType = FileType.valueOf(this.propertyManager.getProperty(OptionalKey.excelFileType.toString()));
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

        for (String fileNameKey : tables.keySet()) {

            String rootFileName = fileNameKey.replaceAll("\\..+$", "");   // Remove extension (if any)
            File file = new File(baseDirectory + File.separatorChar + subDirectory + File.separatorChar + rootFileName +  excelFileType.getExtension());

            Workbook workbook = ExcelTools.getNewWorkbook(excelFileType);
            CellStyle cs = workbook.createCellStyle();
            cs.setWrapText(true);
            cs.setVerticalAlignment(VerticalAlignment.TOP);

            TableData tableData = tables.get(fileNameKey);

            //.... Add a renderer to the tables in the tableData
            //     If we want to allow for other renderers, we need to add some generic coding here
            for (String tableKey : tableData.getTables().keySet()) {
                if (!tableData.getTable(tableKey).hasRenderer(RenderingContext.EXCEL)) {
                    tableData.getTable(tableKey).addRenderer(RenderingContext.EXCEL, new SimpleExcelRenderer());
                }
            }

            //.... Add the table to the workbook, one table per sheet
            for (String sheetName : tableData.getTables().keySet()) {
                addTable(workbook, tableData.getTable(sheetName), sheetName);
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
    private Workbook addTables(FileType type, Map<String, Table> tables) {
        if (type == null) {
            throw new NullPointerException("type may not be null");
        }
        return addTables(ExcelTools.getNewWorkbook(type), tables);
    }

    /**
     * @param workbook
     * @param tables Sheet names are the keys here
     * @return
     */
    private Workbook addTables(Workbook workbook, Map<String, Table> tables) {
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
    private Workbook addTable(Workbook workbook, Table table, String sheetName) {
        if (workbook == null) {
            throw new IllegalArgumentException("workbook may not be null");
        }
        if (table == null) {
            throw new IllegalArgumentException("table may not be null");
        }
        if (sheetName == null) {
            throw new NullPointerException("sheetName may not be null");
        }
        if (table.getRenderer(RenderingContext.EXCEL) == null) {
            throw new UnsupportedOperationException("No IexcelRenderer defined for this table - rendering can not be performed");
        }

        IExcelRenderer renderer = (IExcelRenderer) table.getRenderer(RenderingContext.EXCEL);

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
                renderer.renderCell(cell, dataCell);
            }
        }
        return workbook;

    }

}
