/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import javafx.scene.control.Tab;

/**
 * 
 * @author Eskil Hesselroth
 */
public class CustomTab extends Tab {

    Table table;
    public CustomTab(Table table)
    {
        this.table = table;
    }
    
    public Table getTable()
            
    {
        return table;
    }
}
