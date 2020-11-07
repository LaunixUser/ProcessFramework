package org.ml.pf.output.impl;

import org.ml.pf.output.OutputContextData;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.ml.pf.output.IFileOutput;
import org.ml.pf.output.TableData;
import org.ml.table.render.RenderingContext;
import org.ml.tools.PropertyHolder;
import org.ml.tools.PropertyManager;
import org.ml.tools.logging.LoggerFactory;
import org.ml.tools.velocity.VelocityConfig;

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
import org.ml.table.render.impl.SimpleVelocityRenderer;
import org.ml.tools.FileType;

/**
 * @author osboxes
 */
public class VelocityTableFileOutput extends PropertyHolder implements IFileOutput<TableData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(VelocityTableFileOutput.class.getName());
    private OutputContextData outputContextData;

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
        outputContextData, tableData, date, fileName, rootFileName, renderingContext
    }

    /**
     * @param propertyManager
     */
    public VelocityTableFileOutput(PropertyManager propertyManager) {
        super(propertyManager);
        this.propertyManager.validateAllPropertyNames(RequiredKey.baseDirectory);
        this.outputContextData = new OutputContextData();
    }

    /**
     * @param propertyManager
     * @param pageData
     */
    public VelocityTableFileOutput(PropertyManager propertyManager, OutputContextData pageData) {
        super(propertyManager);
        if (pageData == null) {
            throw new NullPointerException("pageData may not be null");
        }
        this.propertyManager.validateAllPropertyNames(RequiredKey.baseDirectory);
        this.outputContextData = pageData;
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
            outputContextData.setProperties(propertyManager);

            String baseDirectory = propertyManager.getProperty(RequiredKey.baseDirectory);
            String subDirectory = propertyManager.getString(OptionalKey.subDirectory, "");

            BufferedWriter writer;
            for (String fileNameKey : tableDataMap.keySet()) {

                String rootFileName = fileNameKey.replaceAll("\\..+$", "");   // Remove extension (if any)
                File file = new File(baseDirectory + File.separatorChar + subDirectory + File.separatorChar + rootFileName  + FileType.HTML.getExtension());

                VelocityContext context = new VelocityContext();
                TableData tableData = tableDataMap.get(fileNameKey);

                //.... Add a Velocity renderer to the tables in the tableData
                //     If we want to allow for other renderers, we need to add some generic coding here
                for (String tableKey : tableData.getTables().keySet()) {
                    if (!tableData.getTable(tableKey).hasRenderer(RenderingContext.VELOCITY)) {
                        tableData.getTable(tableKey).addRenderer(RenderingContext.VELOCITY, new SimpleVelocityRenderer());
                    }
                }

                //.... Add some standard data to the OutputContextData instance
                outputContextData.setProperty(VelocityContextKey.date, new Date().toString());
                outputContextData.setProperty(VelocityContextKey.fileName, Paths.get(file.getAbsolutePath()).getFileName().toString());
                outputContextData.setProperty(VelocityContextKey.rootFileName, rootFileName);

                //.... Load the VelocityContext
                context.put(VelocityContextKey.tableData.toString(), tableData);
                context.put(VelocityContextKey.outputContextData.toString(), outputContextData);
                context.put(VelocityContextKey.renderingContext.toString(), RenderingContext.VELOCITY);

                //.... Place TableData properties into the VELOCITY context 
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
