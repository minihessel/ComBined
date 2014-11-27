/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.MainFXMLController;
import Model.DataInsight.AlgoFPGrowth;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Eskil Hesselroth
 */
public class CustomTab extends Tab {

    Table table;
    String name;
    TableView tableView;
    AlgoFPGrowth algoFPGrowth;

    public CustomTab(Table table, String name, TableView tableView, AnchorPane anchorPane) {
        this.table = table;
        this.name = name;
        this.tableView = tableView;

        setText(name);

        anchorPane.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    MainFXMLController.whichTabIsSelected = (CustomTab) getTabPane().getSelectionModel().getSelectedItem();
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

    }

    public CustomTab(String name) {
        this.name = name;
        setText(name);

    }
    
    public void mapFpGrowthToTab(AlgoFPGrowth algoFPGrowth)
    {
        this.algoFPGrowth = algoFPGrowth;
    }

    public Table getTable() {
        return table;
    }

    public TableView getTableView() {
        return tableView;
    }

}
