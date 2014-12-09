/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.MainFXMLController;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

/**
 *
 * @author Eskil Hesselroth
 */
public class CustomTab extends Tab {

    Table table;
    String name;
    TableView tableView;
    List<Table> tablesList;

    public CustomTab(Table table, String name, TableView tableView, Node node, List<Table> tablesList) {
        this.table = table;
        this.name = name;
        this.tableView = tableView;
        this.tablesList = tablesList;

        setText(name);

        node.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    MainFXMLController.whichTabIsSelected = (CustomTab) getTabPane().getSelectionModel().getSelectedItem();
                    System.out.println(CustomTab.this.getText());
                }
            }
        });
        this.setOnSelectionChanged(
                new EventHandler<javafx.event.Event>() {
                    @Override
                    public void handle(javafx.event.Event e
                    ) {
                        MainFXMLController.whichTabIsSelected = CustomTab.this;

                    }
                }
        );

        this.setOnClosed(
                new EventHandler<javafx.event.Event>() {
                    @Override
                    public void handle(javafx.event.Event e
                    ) {
                        if(tablesList.contains(table))
                        { tablesList.remove(table);}
                        TabPane tabPane = getTabPane();
                        tabPane.getTabs().remove(this);

                    }
                }
        );

    }

    public CustomTab(String name) {
        this.name = name;
        setText(name);

    }

    public Table getTable() {
        return table;
    }

    public TableView getTableView() {
        return tableView;
    }

}
