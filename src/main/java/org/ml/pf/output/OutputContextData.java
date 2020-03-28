package org.ml.pf.output;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ml.tools.PropertyManager;

/**
 * A helper class to transport data to IOutput instances which can be consumed
 * therein
 *
 * @author Dr. Matthias Laux
 */
public class OutputContextData extends PropertyManager {

    private final Map<String, Set<String>> contextSetData = new HashMap<>();
    private final Map<String, Map<String, Object>> contextMapData = new HashMap<>();

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
    public OutputContextData addContextSetData(String setName, String value) {
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
    public OutputContextData addContextSetData(String setName, Collection<String> values) {
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
    public OutputContextData addContextMapData(String mapName, String key, Object value) {
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
