package org.ml.pf.step;

import java.util.logging.Logger;

import org.ml.tools.PropertyManager;
import org.ml.tools.logging.LoggerFactory;

/**
 * Subclasses need to implement createOutputData() to prepare data for any
 * outputs; the input data is routed through to the targets
 *
 * @param <S>
 * @param <U>
 * @author mlaux
 */
public abstract class AbstractDirectProcessStep<S, U> extends AbstractProcessStep<S, S, U> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractDirectProcessStep.class.getName());

    /**
     * @param id
     * @param propertyManager
     */
    public AbstractDirectProcessStep(String id, PropertyManager propertyManager) {
        super(id, propertyManager);
    }

    /**
     * @param id
     */
    public AbstractDirectProcessStep(String id) {
        super(id);
    }

    /**
     * @param propertyManager
     */
    public AbstractDirectProcessStep(PropertyManager propertyManager) {
        super(propertyManager);
    }

    /**
     *
     */
    public AbstractDirectProcessStep() {
        super();
    }

    /**
     * @param data
     * @return
     */
    @Override
    public S convert(S data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }
        return data;
    }

}
