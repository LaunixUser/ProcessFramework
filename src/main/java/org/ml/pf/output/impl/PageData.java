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
package org.ml.pf.output.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ml.tools.PropertyManager;

/**
 * @author Dr. Matthias Laux
 */
public class PageData extends PropertyManager {

    private Map<String, Set<String>> contextSetData = new HashMap<>();
    private Map<String, Map<String, Object>> contextMapData = new HashMap<>();

    /**
     * @param setName
     * @return
     */
    public Set<String> getContextSetData(String setName) {
        if (setName == null) {
            throw new NullPointerException("setName may not be null");
        }
        return contextSetData.get(setName);
    }

    /**
     * @param mapName
     * @return
     */
    public Map<String, Object> getContextMapData(String mapName) {
        if (mapName == null) {
            throw new NullPointerException("mapName may not be null");
        }
        return contextMapData.get(mapName);
    }

    /**
     * @param setName
     * @param value
     * @return
     */
    public PageData addContextSetData(String setName, String value) {
        if (setName == null) {
            throw new NullPointerException("setName may not be null");
        }
        if (value == null) {
            throw new NullPointerException("value may not be null");
        }
        if (!contextSetData.containsKey(setName)) {
            contextSetData.put(setName, new TreeSet<>());
        }
        contextSetData.get(setName).add(value);
        return this;
    }

    /**
     * @param setName
     * @param values
     * @return
     */
    public PageData addContextSetData(String setName, Collection<String> values) {
        if (setName == null) {
            throw new NullPointerException("setName may not be null");
        }
        if (values == null) {
            throw new NullPointerException("values may not be null");
        }
        if (!contextSetData.containsKey(setName)) {
            contextSetData.put(setName, new TreeSet<>());
        }
        for (String value : values) {
            contextSetData.get(setName).add(value);
        }
        return this;
    }

    /**
     * @param mapName
     * @param key
     * @param value
     * @return
     */
    public PageData addContextMapData(String mapName, String key, Object value) {
        if (mapName == null) {
            throw new NullPointerException("mapName may not be null");
        }
        if (key == null) {
            throw new NullPointerException("key may not be null");
        }
        if (value == null) {
            throw new NullPointerException("value may not be null");
        }
        if (!contextMapData.containsKey(mapName)) {
            contextMapData.put(mapName, new HashMap<>());
        }
        contextMapData.get(mapName).put(key, value);
        return this;
    }

}
