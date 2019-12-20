package org.ml.pf.step;

import org.ml.pf.filter.IFilter;
import org.ml.pf.output.IOutput;

import java.util.Collection;
import java.util.Map;
import org.ml.tools.PropertyManager;

/**
 * @param <S>
 * @param <T>
 * @param <U>
 * @author osboxes
 */
public interface IProcessStep<S, T, U> {

    /**
     * @return
     */
    String getID();

    /**
     *
     * @return
     */
    public PropertyManager getPropertyManager();

    /**
     * @param data
     * @return The result of this step's work
     */
    T process(S data);

    /**
     * @param step
     * @return
     */
//    public <V> IStep<T, V> setNext(IStep<T, V> step);
    IProcessStep<T, ?, ?> addNext(IProcessStep<T, ?, ?> step);

    /**
     * @return
     */
    Collection<IProcessStep<T, ?, ?>> getNext();

    /**
     * @return
     */
    Collection<IProcessStep> getLast();

    /**
     * @return
     */
    T getResult();

    /**
     * @param data
     * @return
     */
    Map<String, Object> run(S data);

    /**
     * @param filters
     * @return
     */
    IProcessStep<S, T, U> addFilter(IFilter<S>... filters);

    /**
     * @param data
     * @return
     */
    S invokeFilters(S data);

    /**
     * @param data
     * @return
     */
    T convert(S data);

    /**
     * @param outputs
     * @return
     */
    IProcessStep<S, T, U> addOutput(IOutput<U>... outputs);

    /**
     * @param data
     * @return
     */
    U createOutputData(T data);

    /**
     * @param data
     */
    void invokeOutputs(U data);

}
