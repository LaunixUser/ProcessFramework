/**
 * This is a step that passes on the source data to the next step and just focuses on creating output via the specified outputs
 * without modifying the data as such
 */
package org.ml.pf.step.impl;

import java.util.logging.Logger;

import org.ml.tools.logging.LoggerFactory;
import org.ml.pf.step.AbstractDirectProcessStep;

/**
 * Simple case where S, T, and U are of the same type: only outputs are added
 * here which are then fed with the input data. The input data is also routed
 * through to the target(s) directly without conversion. Combines the features of
 * AbstractDirectProcessStep and AbstractTransferProcessStep
 *
 * @param <S>
 * @author mlaux
 */
public class SimpleProcessStep<S> extends AbstractDirectProcessStep<S, S> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleProcessStep.class.getName());

    /**
     * @param id
     */
    public SimpleProcessStep(String id) {
        super(id);
    }

    /**
     *
     */
    public SimpleProcessStep() {
        super();
    }

    /**
     * @param data
     * @return
     */
    @Override
    public S createOutputData(S data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }
        return data;
    }

}
