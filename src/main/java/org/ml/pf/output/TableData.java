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
package org.ml.pf.output;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.ml.tools.PropertyManager;
import org.ml.table.Table;

/**
 * @author Dr. Matthias Laux
 */
public class TableData extends PropertyManager {

    private Map<String, Table> tables = new TreeMap<>();
    private Map<String, String> subHeaders = new HashMap<>();
    private Map<String, String> subFooters = new HashMap<>();
    private String tableHeader = "";
    private String tableFooter = "";
    private String description = "";

    /**
     *
     */
    public TableData() {

    }

    /**
     * @param propertyManager
     */
    public TableData(PropertyManager propertyManager) {
        super(propertyManager);
    }

    /**
     * @param tableKey
     * @param table
     */
    public void addTable(String tableKey, Table table) {
        if (tableKey == null) {
            throw new NullPointerException("tableKey may not be null");
        }
        if (table == null) {
            throw new NullPointerException("table may not be null");
        }
        tables.put(tableKey, table);
    }

    /**
     * @return
     */
    public Map<String, Table> getTables() {
        return tables;
    }

    /**
     * @param tableKey
     * @return
     */
    public Table getTable(String tableKey) {
        if (tableKey == null) {
            throw new NullPointerException("tableKey may not be null");
        }
        return tables.get(tableKey);
    }

    /**
     * @return the tableHeader
     */
    public String getTableHeader() {
        return tableHeader;
    }

    /**
     * @param tableHeader the tableHeader to set
     */
    public void setTableHeader(String tableHeader) {
        if (tableHeader == null) {
            throw new NullPointerException("tableHeader may not be null");
        }
        this.tableHeader = tableHeader;
    }

    /**
     * @param tableKey
     * @return
     */
    public String getTableSubHeader(String tableKey) {
        if (tableKey == null) {
            throw new NullPointerException("tableKey may not be null");
        }
        return subHeaders.get(tableKey);
    }

    /**
     * @param tableKey
     * @param tableHeader
     */
    public void setTableSubHeader(String tableKey, String tableHeader) {
        if (tableKey == null) {
            throw new NullPointerException("tableKey may not be null");
        }
        if (tableHeader == null) {
            throw new NullPointerException("tableHeader may not be null");
        }
        subHeaders.put(tableKey, tableHeader);
    }

    /**
     * @param tableKey
     * @return
     */
    public String getTableSubFooter(String tableKey) {
        if (tableKey == null) {
            throw new NullPointerException("tableKey may not be null");
        }
        return subFooters.get(tableKey);
    }

    /**
     * @param tableKey
     * @param tableFooter
     */
    public void setTableSubFooter(String tableKey, String tableFooter) {
        if (tableKey == null) {
            throw new NullPointerException("tableKey may not be null");
        }
        if (tableFooter == null) {
            throw new NullPointerException("tableFooter may not be null");
        }
        subFooters.put(tableKey, tableFooter);
    }

    /**
     * @return the tableFooter
     */
    public String getTableFooter() {
        return tableFooter;
    }

    /**
     * @param tableFooter the tableFooter to set
     */
    public void setTableFooter(String tableFooter) {
        if (tableFooter == null) {
            throw new NullPointerException("tableFooter may not be null");
        }
        this.tableFooter = tableFooter;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        if (description == null) {
            throw new NullPointerException("description may not be null");
        }
        this.description = description;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }
}
