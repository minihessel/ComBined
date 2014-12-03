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
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.validation.ValidationResult;

/**
 *
 * @author Eskil Hesselroth
 */
public final class SQlConnectPage3 implements WizardPage {

    String header = "Pleasse select your columns";

    public ComboBox comboBox = new ComboBox();
    final private GridPane pane = new GridPane();
    public ListSelectionView listView = new ListSelectionView();

    private boolean fulfilled = false;

    public SQlConnectPage3() throws SQLException {
        new SelectKeyComboBoxListener(comboBox);

        addElements();
    }

    @Override
    public void addElements() {
        pane.add(new Label("Select which columns: "), 0, 0);
        pane.add(listView, 1, 0);

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
        fulfilled = ValidatorRegler.ikkeTom(listView);
        return fulfilled;
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onEnter() {
        listView.getSourceItems().clear();
        listView.getTargetItems().clear();
        try {

            for (int i = 1; i <= SQL_manager.rs.getMetaData().getColumnCount(); i++) {
                listView.getSourceItems().add(SQL_manager.rs.getMetaData().getColumnName(i));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQlConnectPage3.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
