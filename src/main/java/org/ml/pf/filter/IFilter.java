package org.ml.pf.filter;

/**
 * @param <F>
 * @author osboxes
 */
public interface IFilter<F> {

    /**
     * @param data
     * @return
     */
    F apply(F data);

}
