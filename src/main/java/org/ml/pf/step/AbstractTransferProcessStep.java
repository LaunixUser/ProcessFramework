/**
 * This is a step that passes on the source data to the next step and just focuses on creating output via the specified outputs
 * without modifying the data as such
 */
package org.ml.pf.step;

import java.util.logging.Logger;

import org.ml.tools.PropertyManager;
import org.ml.tools.logging.LoggerFactory;

/**
 * Subclasses need to implement process(); outputs take T as format
 *
 * @param <S>
 * @param <T>
 * @author mlaux
 */
public abstract class AbstractTransferProcessStep<S, T> extends AbstractProcessStep<S, T, T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractTransferProcessStep.class.getName());

    /**
     * @param id
     * @param propertyManager
     */
    public AbstractTransferProcessStep(String id, PropertyManager propertyManager) {
        super(id, propertyManager);
    }

    /**
     * @param id
     */
    public AbstractTransferProcessStep(String id) {
        super(id);
    }

    /**
     * @param propertyManager
     */
    public AbstractTransferProcessStep(PropertyManager propertyManager) {
        super(propertyManager);
    }

    /**
     *
     */
    public AbstractTransferProcessStep() {
        super();
    }

    /**
     * Null operation, output data type is the same as target type T
     *
     * @param data
     * @return
     */
    @Override
    public T createOutputData(T data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }
        return data;
    }

}
