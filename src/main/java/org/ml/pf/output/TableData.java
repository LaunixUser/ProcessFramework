package org.ml.pf.output;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.ml.tools.PropertyManager;
import org.ml.table.Table;

/**
 * This class holds together one or more actual tables plus a lot of metadata to
 * describe them. The idea is that this is additional input that output /
 * rendering programs can use to process these tables. The tables in here are
 * expected to be processed together, i. e. for example written to one output
 * file
 *
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
