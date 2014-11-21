/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eskil Hesselroth
 */
class QueryBuilder {

    StringBuilder query;
    List<String> columns = new ArrayList();
    String whichTable;

    public QueryBuilder(String whichTable) {
        query = new StringBuilder();
        query.append("select ");
        this.whichTable = whichTable;

    }

    void addColumnNames(List<String> columnNames) {
        columns.addAll(columnNames);
    }

    String getQuery() {

        for (int i = 0; i < columns.size(); i++) {
            System.out.println(columns.size());
            System.out.println(i);
            if (i != 0 && i != columns.size()) {
                query.append(", ");

            }
            query.append(columns.get(i));
        }
        query.append(" from ");
        query.append(whichTable);
        return query.toString();
    }
}
