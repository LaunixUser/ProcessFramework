package org.ml.pf.step.impl.excel;

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
public class ExcelReadWorkbookDatabaseStep extends AbstractTransferProcessStep<TableQuery, Workbook> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelReadWorkbookDatabaseStep.class.getName());

    /**
     * @param id
     */
    public ExcelReadWorkbookDatabaseStep(String id) {
        super(id);
    }

    /**
     *
     */
    public ExcelReadWorkbookDatabaseStep() {
    }

    /**
     * @param queryData
     * @return
     */
    @Override
    public Workbook convert(TableQuery queryData) {
        if (queryData == null) {
            throw new NullPointerException("queryData may not be null");
        }
        ExcelDatabaseHandler handler = new ExcelDatabaseHandler(queryData.getConnectionData());
        Workbook workbook = null;
        try {
            workbook = handler.loadExcelFromDatabase(queryData.getTableNames());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "{0}:{1}", new Object[]{ex.getClass(), ex.getMessage()});
        }
        return workbook;
    }

}
