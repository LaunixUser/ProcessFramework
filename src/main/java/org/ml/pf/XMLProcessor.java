package org.ml.pf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.ml.pf.step.IProcessStep;
import org.ml.tools.PropertyManager;

/**
 *
 * @author Dr. Matthias Laux
 */
public class XMLProcessor {

    /**
     *
     */
    public enum XML {
        id, className, step, outputs, properties
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {

        try {

            XMLProcessor processor = new XMLProcessor();
            File file = new File("/home/osboxes/development/Java/netbeans/ProcessFramework/src/test/java/org/ml/pf/test/config/steps.xml");
            SAXBuilder builder = new SAXBuilder();

            Document doc = builder.build(new BufferedReader(new FileReader(file)));
            Map<String, IProcessStep> steps = processor.process(doc.getRootElement());
            for (String id : steps.keySet()) {
                System.out.println("ID = " + id + " - " + steps.get(id).getClass().getCanonicalName());
            }

        } catch (IOException | JDOMException ex) {
            throw new UnsupportedOperationException(ex.getMessage());
        }

    }

    /**
     *
     * @param rootElement
     * @return
     */
    public Map<String, IProcessStep> process(Element rootElement) {
        if (rootElement == null) {
            throw new NullPointerException("rootElement may not be null");
        }

        Map<String, IProcessStep> steps = new HashMap<>();

        for (Element stepElement : rootElement.getChildren(XML.step.toString())) {

            String id = getRequiredAttribute(stepElement, XML.id);
            String className = getRequiredAttribute(stepElement, XML.className);

            //.... Handle properties, if any
            PropertyManager stepPropertyManager = null;
            if (stepElement.getAttribute(XML.properties.toString()) != null) {
                stepPropertyManager = new PropertyManager(stepElement);
            }
            //.... Handle output elements, if any
            if (stepElement.getAttribute(XML.outputs.toString()) != null) {

            }

            //.... Now construct the step instance
            Class<?> baseClass;
            try {

                baseClass = Class.forName(className);
                IProcessStep processStep;
                if (stepPropertyManager != null) {
                    Constructor<?> constructor = baseClass.getConstructor(String.class, PropertyManager.class);
                    processStep = (IProcessStep) constructor.newInstance(id, stepPropertyManager);
                } else {
                    Constructor<?> constructor = baseClass.getConstructor(String.class);
                    processStep = (IProcessStep) constructor.newInstance(id);
                }
                steps.put(id, processStep);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                throw new UnsupportedOperationException(ex.getMessage());
            }

        }

        return steps;
    }

    /**
     *
     * @param element
     * @param attributeName
     * @return
     */
    private String getRequiredAttribute(Element element, XML attributeName) {
        if (element == null) {
            throw new NullPointerException("element may not be null");
        }
        if (attributeName == null) {
            throw new NullPointerException("attributeName may not be null");
        }
        if (element.getAttribute(attributeName.toString()) != null) {
            return element.getAttribute(attributeName.toString()).getValue();
        } else {
            throw new UnsupportedOperationException("Missing attribute: " + attributeName.toString());
        }

    }
}
