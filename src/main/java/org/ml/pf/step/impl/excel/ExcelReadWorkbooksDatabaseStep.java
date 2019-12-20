package org.ml.pf.step.impl.excel;

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
 * Read all the Excel files from a given path into Workbook instances. A file
 * filter can be applied before actually reading the files.

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
