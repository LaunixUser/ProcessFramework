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
    private U outputDataCache;

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

        outputDataCache = createOutputData(convertedData);

        invokeOutputs(outputDataCache);

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
        this.outputs.addAll(Arrays.asList(outputs));
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
        outputs.forEach((output) -> {
            output.put(data);
        });
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
        this.filters.addAll(Arrays.asList(filters));
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
    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    /**
     * The outputData generated in the process() method is stored here such that
     * it can be accessed for other use. This is more of a convenience feature
     *
     * @return
     */
    public U getOutputDataCache() {
        return outputDataCache;
    }
}
