package org.ml.pf.step.impl.tool;

import org.ml.tools.ConnectionData;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class holds the data required for a query of table names against a database

* @author Dr. Matthias Laux
 */
public class TableQuery {

    private ConnectionData connectionData;
    private Set<String> tableNames;

    /**
     * @param connectionData
     * @param tableNames
     */
    public TableQuery(ConnectionData connectionData, Set<String> tableNames) {
        if (connectionData == null) {
            throw new NullPointerException("connectionData may not be null");
        }
        if (tableNames == null) {
            throw new NullPointerException("tableNames may not be null");
        }
        this.connectionData = connectionData;
        this.tableNames = tableNames;
    }

    /**
     * @param connectionData
     * @param tableName
     */
    public TableQuery(ConnectionData connectionData, String... tableName) {
        if (connectionData == null) {
            throw new NullPointerException("connectionData may not be null");
        }
        if (tableName == null) {
            throw new NullPointerException("tableName may not be null");
        }
        this.connectionData = connectionData;
        this.tableNames = new HashSet<>();
        Collections.addAll(tableNames, tableName);
    }

    /**
     * @return
     */
    public ConnectionData getConnectionData() {
        return connectionData;
    }

    /**
     * @return
     */
    public Set<String> getTableNames() {
        return tableNames;
    }
}
