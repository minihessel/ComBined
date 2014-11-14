/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 *
 * @author Eskil Hesselroth
 */
public class ValidatorRegler {

    public static boolean ikkeTom(TextField txtField) {

        if (txtField.getText().length() != 0) {
            return true;
        } else {
            blink(txtField);
            return false;
        }

    }
    
        public static boolean ikkeTom(ComboBox comboBox) {

        if (comboBox.getSelectionModel().getSelectedItem()!=null) {
            return true;
        } else {
            blink(comboBox);
            return false;
        }

    }

    public static boolean connected(Label lbl, Button testConnection) {

        if (lbl.getText().equals("Success, logged in")) {
            return true;
        } else {
            lbl.setText("Please test your connection first");
            blink(testConnection);

            return false;
        }
    }

    static void blink(Node graphic) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), graphic);
        ft.setFromValue(10.0);
        ft.setToValue(0.0);
        ft.setCycleCount(6);
        ft.setAutoReverse(true);
        ft.play();
    }
}
