/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.control.TableColumn;

/**
 *
 * @author Eskil Hesselroth
 */
public class MyTableColumn extends TableColumn {

    public MyTableColumn(String name) {
        setText(name);
    }

    @Override
    public String toString() {
        return getText();
    }

}
