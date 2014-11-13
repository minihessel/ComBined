/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Eskil Hesselroth
 */
public final class SQlConnectPage11 implements WizardPage {

    public Label header = new Label("Please select which table you would like to pull data from");
    TextField txtField = new TextField("");
    TextField txtPort = new TextField("");
    TextField txtInstance = new TextField("");
    final private GridPane pane = new GridPane();
    private boolean fulfilled = false;

    public SQlConnectPage11() {

        addElements();
    }

    @Override
    public void addElements() {
        pane.getChildren().addAll(txtField);
    }

    @Override
    public GridPane getPane() {
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    @Override
    public Label getHeader() {
        return header;
    }

    @Override
    public boolean valider() {
        fulfilled = ValidatorRegler.ikkeTom(txtField);
        return fulfilled;
    }
}
