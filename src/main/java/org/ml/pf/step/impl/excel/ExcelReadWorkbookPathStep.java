package org.ml.pf.step.impl.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.ml.pf.step.AbstractTransferProcessStep;
import org.ml.tools.excel.ExcelTools;

import java.nio.file.Path;

/**
 * Read one Excel file form the given path into a workbook instance

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
