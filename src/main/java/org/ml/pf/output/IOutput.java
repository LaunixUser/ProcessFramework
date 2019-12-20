package org.ml.pf.output;

/**
 * @param <U>
 * @author osboxes
 */
public interface IOutput<U> {

    /**
     * @param data
     */
    void put(U data);
}
