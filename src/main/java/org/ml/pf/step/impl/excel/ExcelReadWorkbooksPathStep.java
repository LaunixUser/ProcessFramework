package org.ml.pf.step.impl.excel;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.ml.tools.excel.ExcelTools;
import org.ml.tools.logging.LoggerFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.ml.pf.step.AbstractTransferProcessStep;
import org.ml.pf.step.impl.tool.IFileFilter;

/**
 * Read all the Excel files from a given path into Workbook instances. A file
 * filter can be applied before actually reading the files.
 *
 * @author osboxes
 */
public class ExcelReadWorkbooksPathStep extends AbstractTransferProcessStep<Path, Map<String, Workbook>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelReadWorkbooksPathStep.class.getName());
    private IFileFilter fileFilter = null;

    /**
     * @param id
     */
    public ExcelReadWorkbooksPathStep(String id) {
        super(id);
    }

    /**
     * @param id
     * @param fileFilter
     */
    public ExcelReadWorkbooksPathStep(String id, IFileFilter fileFilter) {
        super(id);
        if (fileFilter == null) {
            throw new NullPointerException("fileFilter may not be null");
        }
        this.fileFilter = fileFilter;
    }

    /**
     *
     */
    public ExcelReadWorkbooksPathStep() {
    }

    /**
     * @param baseDirectory
     * @return
     */
    @Override
    public Map<String, Workbook> convert(Path baseDirectory) {
        if (baseDirectory == null) {
            throw new NullPointerException("baseDirectory may not be null");
        }

        Map<String, Workbook> workbooks = new HashMap<>();

        LOGGER.log(Level.INFO, "Base directory: {0}", baseDirectory);
        try {

            //.... First find all input files in the base directory
            Map<String, Path> allPaths = new HashMap<>();
            for (Path path : Files.walk(baseDirectory, FileVisitOption.FOLLOW_LINKS)
                    .filter(s -> s.toString().matches(".*\\.xls[x]?$"))
                    .collect(Collectors.toList())) {
                //         String i = path.getFileName().toString();
                allPaths.put(path.getFileName().toString(), path);
            }
            Map<String, Path> paths = allPaths;

            //.... Apply a given filter (if any)
            if (fileFilter != null) {
                paths = new HashMap<>();
                for (String fileName : allPaths.keySet()) {
                    if (fileFilter.accept(allPaths.get(fileName))) {
                        paths.put(fileName, allPaths.get(fileName));
                    }
                }
            }

            /**
             * Previous version supporting matcher only
             *
             * for (Path path : Files.walk(baseDirectory) .filter(s ->
             * s.toString().matches(".*\\.xls[x]?$")) .filter(s ->
             * s.getFileName().toString().matches(fileNameFilter))
             * .collect(Collectors.toList())) {
             * paths.put(path.getFileName().toString(), path); }
             */
            //.... Get the workbooks
            for (String fileName : paths.keySet()) {
                LOGGER.log(Level.INFO, "Reading workbook from {0}", paths.get(fileName));
                workbooks.put(fileName, ExcelTools.getWorkbook(paths.get(fileName)));
            }

        } catch (IOException ex) {

            LOGGER.log(Level.SEVERE, "Could not geth paths, returning empty map: {0}", ex.getMessage());

        }

        return workbooks;

    }

}
