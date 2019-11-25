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

import java.util.Collection;
import java.util.Map;

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
