/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.MainFXMLController;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

/**
 *
 * @author Eskil Hesselroth
 */
public class CustomTab extends Tab {

    Table table;
    String name;
    TableView tableView;

    public CustomTab(Table table, String name, TableView tableView) {
        this.table = table;
        this.name = name;
        this.tableView = tableView;
        setText(name);
        this.setOnSelectionChanged(
                new EventHandler<javafx.event.Event>() {
                    @Override
                    public void handle(javafx.event.Event e
                    ) {
                        MainFXMLController.whichTabIsSelected = CustomTab.this;
                    }
                }
        );
    }

    public Table getTable() {
        return table;
    }

    public TableView getTableView() {
        return tableView;
    }

}
