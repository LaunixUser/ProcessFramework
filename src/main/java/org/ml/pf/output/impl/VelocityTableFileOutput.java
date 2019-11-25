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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.ml.pf.output.IFileOutput;
import org.ml.pf.output.TableData;
import org.ml.pf.render.SimpleVelocityRenderer;
import org.ml.table.render.RenderingContext;
import org.ml.tools.PropertyHolder;
import org.ml.tools.PropertyManager;
import org.ml.tools.logging.LoggerFactory;
import org.ml.tools.velocity.VelocityConfig;
import org.ml.tools.velocity.VelocityFileType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author osboxes
 */
public class VelocityTableFileOutput extends PropertyHolder implements IFileOutput<TableData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(VelocityTableFileOutput.class.getName());
    private PageData pageData;

    /**
     *
     */
    public enum RequiredKey {
        baseDirectory, templateName
    }

    /**
     *
     */
    public enum OptionalKey {
        templateDirectory, inputEncoding, subDirectory
    }

    /**
     *
     */
    public enum VelocityContextKey {
        pageData, tableData, date, fileName, rootFileName, renderingContext
    }

    /**
     * @param propertyManager
     */
    public VelocityTableFileOutput(PropertyManager propertyManager) {
        super(propertyManager);
        this.propertyManager.validateAllPropertyNames(RequiredKey.baseDirectory);
        pageData = new PageData();
    }

    /**
     * @param propertyManager
     * @param pageData
     */
    public VelocityTableFileOutput(PropertyManager propertyManager, PageData pageData) {
        super(propertyManager);
        if (pageData == null) {
            throw new NullPointerException("pageData may not be null");
        }
        this.propertyManager.validateAllPropertyNames(RequiredKey.baseDirectory);
        this.pageData = pageData;
    }

    /**
     * @param tableDataMap
     */
    @Override
    public void put(Map<String, TableData> tableDataMap) {
        if (tableDataMap == null) {
            throw new NullPointerException("tables may not be null");
        }

        try {

            //.... Get the template instance
            PropertyManager pm = new PropertyManager();
            pm.setProperty(VelocityConfig.RequiredKey.templateName, propertyManager.getProperty(RequiredKey.templateName));
            if (propertyManager.containsProperty(OptionalKey.templateDirectory)) {
                pm.setProperty(VelocityConfig.OptionalKey.templateDirectory, propertyManager.getProperty(OptionalKey.templateDirectory));
            }
            if (propertyManager.containsProperty(OptionalKey.inputEncoding)) {
                pm.setProperty(VelocityConfig.OptionalKey.inputEncoding, propertyManager.getProperty(OptionalKey.inputEncoding));
            }
            VelocityConfig velocityConfig = new VelocityConfig(pm);
            Template template = velocityConfig.getTemplate();

            //.... Copy properties into PageData instance
            pageData.setProperties(propertyManager);

            String baseDirectory = propertyManager.getProperty(RequiredKey.baseDirectory);
            String subDirectory = propertyManager.getString(OptionalKey.subDirectory, "");

            BufferedWriter writer;
            for (String fileNameKey : tableDataMap.keySet()) {

                String rootFileName = fileNameKey.replaceAll("\\..+$", "");   // Remove extension (if any)
                File file = new File(baseDirectory + File.separatorChar + subDirectory + File.separatorChar + rootFileName + "." + VelocityFileType.html.toString());

                VelocityContext context = new VelocityContext();
                TableData tableData = tableDataMap.get(fileNameKey);

                //.... Add a Velocity renderer to the tables in the tableData
                //     If we want to allow for other renderers, we need to add some generic coding here
                for (String tableKey : tableData.getTables().keySet()) {
                    if (!tableData.getTable(tableKey).hasRenderer(RenderingContext.velocity)) {
                        tableData.getTable(tableKey).addRenderer(RenderingContext.velocity, new SimpleVelocityRenderer());
                    }
                }

                //                String fullRootFileName = Paths.get(file.getAbsolutePath()).getFileName().toString();
                // This removes a file extension ... not perfectly sure what the duplicate [] mean here in the RegEx though
                //              String rootFileName = fullRootFileName.replaceFirst("[.][^.]+$", "");
                //.... Add some standard data to the PageData instance
                pageData.setProperty(VelocityContextKey.date, new Date().toString());
                pageData.setProperty(VelocityContextKey.fileName, Paths.get(file.getAbsolutePath()).getFileName().toString());
                pageData.setProperty(VelocityContextKey.rootFileName, rootFileName);

                //.... Load the VelocityContext
                context.put(VelocityContextKey.tableData.toString(), tableData);
                context.put(VelocityContextKey.pageData.toString(), pageData);
                context.put(VelocityContextKey.renderingContext.toString(), RenderingContext.velocity);

                //.... Place TableData properties into the velocity context 
                for (String key : tableData.getProperties().keySet()) {
                    context.put(key, tableData.getProperty(key));
                }

                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }

                LOGGER.log(Level.INFO, "Writing output file {0}", file);

                if (file.exists()) {
                    file.delete();
                }
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
//                writer = Files.newBufferedWriter(Paths.get(file.getPath()), StandardCharsets.UTF_8);
//                writer = new BufferedWriter(new FileWriter(file));
                template.merge(context, writer);
                writer.flush();
                writer.close();
            }

        } catch (Exception ex) {

            LOGGER.log(Level.SEVERE, "{0}:{1}", new Object[]{ex.getClass(), ex.getMessage()});

        }

    }
}
