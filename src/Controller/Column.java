/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;

/**
 *
 * @author Eskil Hesselroth
 */
public class Column extends TableColumn {

    String NAVN;

    public Column(String navn) {
        NAVN = navn;
        setText(navn);

    }

    @Override
    public String toString() {
        return NAVN;

    }

    public Column() {
    }

}
