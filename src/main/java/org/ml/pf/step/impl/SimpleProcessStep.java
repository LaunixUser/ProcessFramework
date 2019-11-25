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
