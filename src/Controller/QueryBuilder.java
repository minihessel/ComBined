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
public class QueryBuilder {

    StringBuilder query;
    List<String> columns = new ArrayList();
    String whichTable;

    public QueryBuilder(String whichTable, String databaseType) {
        query = new StringBuilder();
        query.append("select ");

        switch (databaseType) {
            case "Microsoft SQL Server":
                this.whichTable = "[" + whichTable + "]";
                break;
            case "MySQL":
                this.whichTable = "`" + whichTable + "";
                break;
            default:
                this.whichTable = whichTable;
                break;
        }

    }

    void addColumnNames(List<String> columnNames) {
        columns.addAll(columnNames);
    }

  public String getQuery() {

        for (int i = 0; i < columns.size(); i++) {
            System.out.println(columns.size());
            System.out.println(i);
            if (i != 0 && i != columns.size()) {
                query.append(", ");

            }
            query.append(columns.get(i));
        }

        if (columns.isEmpty()) {
            query.append("*");
        }
        query.append(" from ");
        query.append(whichTable);
        return query.toString();
    }
}
