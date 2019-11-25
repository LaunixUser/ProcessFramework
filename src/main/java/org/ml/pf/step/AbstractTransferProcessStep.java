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
