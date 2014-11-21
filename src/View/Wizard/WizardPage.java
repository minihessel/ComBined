/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Eskil Hesselroth
 */
public interface WizardPage {

    public String getHeader();

    public GridPane getPane();

    public void addElements();

    public void onEnter();

    public boolean valider();

    public void onFinish();
}
