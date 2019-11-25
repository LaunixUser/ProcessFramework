package org.ml.pf.step.impl.excel;

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
 * Read all the Excel files from a given path into Workbook instances.
 * A file filter can be applied before actually reading the files.
 */


import java.io.IOException;
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

        try {

            //.... First find all input files in the base directory
            Map<String, Path> allPaths = new HashMap<>();
            for (Path path : Files.walk(baseDirectory)
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
