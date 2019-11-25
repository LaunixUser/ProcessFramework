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
package org.ml.pf.step;

import org.ml.pf.filter.IFilter;
import org.ml.pf.output.IOutput;
import org.ml.tools.PropertyManager;
import org.ml.tools.logging.LoggerFactory;

import java.util.*;
import java.util.logging.Logger;

/**
 * @param <S>
 * @param <T>
 * @param <U>
 * @author osboxes
 */
public abstract class AbstractProcessStep<S, T, U> implements IProcessStep<S, T, U> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractProcessStep.class.getName());
    private String id;
    protected List<IProcessStep<T, ?, ?>> nextSteps = new ArrayList<>();
    protected T result = null;
    protected PropertyManager propertyManager;
    protected List<IOutput<U>> outputs = new ArrayList<>();
    protected List<IFilter<S>> filters = new ArrayList<>();

    /**
     * @param id
     * @param propertyManager
     */
    public AbstractProcessStep(String id, PropertyManager propertyManager) {
        if (id == null) {
            throw new NullPointerException("id may not be null");
        }
        if (propertyManager == null) {
            throw new NullPointerException("propertyManager may not be null");
        }
        this.id = id;
        this.propertyManager = propertyManager;
    }

    /**
     * @param id
     */
    public AbstractProcessStep(String id) {
        if (id == null) {
            throw new NullPointerException("id may not be null");
        }
        this.id = id;
        propertyManager = new PropertyManager();
    }

    /**
     * @param propertyManager
     */
    public AbstractProcessStep(PropertyManager propertyManager) {
        if (propertyManager == null) {
            throw new NullPointerException("propertyManager may not be null");
        }
        this.id = "ID" + String.valueOf(Math.random()).replace(".", "0");
        this.propertyManager = propertyManager;
    }

    /**
     *
     */
    public AbstractProcessStep() {
        this.id = "ID" + String.valueOf(Math.random()).replace(".", "0");
        this.propertyManager = new PropertyManager();
    }

    /**
     * @return
     */
    @Override
    public String getID() {
        return id;
    }

    /**
     * @param step
     * @return
     */
    @Override
    public IProcessStep<T, ?, ?> addNext(IProcessStep<T, ?, ?> step) {
        if (step == null) {
            throw new NullPointerException("step may not be null");
        }
        nextSteps.add(step);
        return step;
    }

    /**
     * @return
     */
    @Override
    public Collection<IProcessStep<T, ?, ?>> getNext() {
        return nextSteps;
    }

    /**
     * Recursively collects the last step instances for all the steps which
     * follow after the current one
     *
     * @return
     */
    @Override
    public Collection<IProcessStep> getLast() {
        List<IProcessStep> lastSteps = new ArrayList<>();
        for (IProcessStep<T, ?, ?> nextStep : nextSteps) {
            Collection<IProcessStep> lasts = nextStep.getLast();
            if (lasts.isEmpty()) {
                //.... This step doesn't have any next ones, so it is a last step
                lastSteps.add(nextStep);
            } else {
                lastSteps.addAll(lasts);
            }
        }
        return lastSteps;
    }

    /**
     * @return
     */
    @Override
    public T getResult() {
        return result;
    }

    /**
     * Convenience method that runs the processing chain and returns the LAST
     * result(s) as a map of objects (since we don't know of course their
     * types). The return value need(s) to be cast to whatever is returned from
     * the last process step(s). Via the ID the consumer can identify which step
     * delivered which result.
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> run(S data) {
        process(data);
        Map<String, Object> results = new HashMap<>();
        //.... Add results of all child chains - these may be all different kinds of objects
        for (IProcessStep step : getLast()) {
            results.put(step.getID(), step.getResult());
        }
        return results;
    }

    /**
     * @param data
     */
    @Override
    public T process(S data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }

        //.... Run the key process steps
        S filteredData = invokeFilters(data);

        T convertedData = convert(filteredData);

        U outputData = createOutputData(convertedData);

        invokeOutputs(outputData);

        //.... Pass the data on to the next step(s) (if any)
        result = convertedData;
        for (IProcessStep<T, ?, ?> nextStep : nextSteps) {
            nextStep.process(result);
        }
        return convertedData;
    }

    /**
     * @param outputs
     * @return
     */
    @Override
    public IProcessStep<S, T, U> addOutput(IOutput<U>... outputs) {
        if (outputs == null) {
            throw new NullPointerException("outputs may not be null");
        }
        for (IOutput<U> output : outputs) {
            this.outputs.add(output);
        }
        return this;
    }

    /**
     * @param data
     */
    @Override
    public void invokeOutputs(U data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }
        for (IOutput<U> output : outputs) {
            output.put(data);
        }
    }

    /**
     * @param filters
     * @return
     */
    @Override
    public IProcessStep<S, T, U> addFilter(IFilter<S>... filters
    ) {
        if (filters == null) {
            throw new NullPointerException("filters may not be null");
        }
        for (IFilter<S> filter : filters) {
            this.filters.add(filter);
        }
        return this;
    }

    /**
     * @param data
     * @return
     */
    @Override
    public S invokeFilters(S data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }
        S outputData = data;
        for (IFilter<S> filter : filters) {
            outputData = filter.apply(outputData);
        }
        return outputData;
    }

    /**
     * @return
     */
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }
}
