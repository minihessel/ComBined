/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;

/**
 *
 * @author Eskil Hesselroth
 */
public class MyTableRow  extends TableRow{

    ObservableList<String> rowData = FXCollections.observableArrayList();
    public String message = "";

    public MyTableRow() {

    }

    void addData(String cell) {
        rowData.add(cell);
    }

    void setMessage(String message) {
        this.message = message;
    }



}
