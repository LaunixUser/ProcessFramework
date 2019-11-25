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
/**
 * Class holds the data required for a query of table names against a database
 */
package org.ml.pf.step.impl.tool;

import org.ml.tools.ConnectionData;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
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
