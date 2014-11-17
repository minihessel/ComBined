/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard.SQLConnectWizard;

import Controller.SQL_manager;
import Controller.SelectKeyComboBoxListener;
import View.Wizard.ValidatorRegler;
import View.Wizard.WizardPage;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Eskil Hesselroth
 */
public final class SQlConnectPage11 implements WizardPage {

    String header = "Please select which table you would like to pull data from";

    public ComboBox comboBox = new ComboBox();
    final private GridPane pane = new GridPane();
    private boolean fulfilled = false;

    public SQlConnectPage11() throws SQLException {
        new SelectKeyComboBoxListener(comboBox);
      
        addElements();
    }

    @Override
    public void addElements() {
        pane.add(new Label("Select which table: "), 0, 0);
        pane.add(comboBox, 1, 0);
    }

    @Override
    public GridPane getPane() {
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public boolean valider() {
        fulfilled = ValidatorRegler.ikkeTom(comboBox);
        return fulfilled;
    }
    
    @Override
   public void onEnter() 
    {
        comboBox.getItems().clear();
        try { 
            SQL_manager.getAllTables(comboBox);
        } catch (SQLException ex) {
            Logger.getLogger(SQlConnectPage11.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
