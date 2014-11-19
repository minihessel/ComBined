/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard.InsightWizard;

import Controller.SelectKeyComboBoxListener;
import View.Wizard.ValidatorRegler;
import View.Wizard.WizardPage;
import Model.Kolonne;
import Model.Table;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Eskil Hesselroth
 */
public class InsightWizardPage1 implements WizardPage {

    String header = "Please select which columns you would like data insight on";
    public ComboBox transactionIDcolumn = new ComboBox();
    public ComboBox<Table> tableColumn = new ComboBox();
    public ComboBox itemIDcolumn = new ComboBox();
    public CheckBox checkBox = new CheckBox();
    final private GridPane pane = new GridPane();
    private boolean fulfilled = false;
    Table table;

    public InsightWizardPage1(List<Table> tablesList) {
        new SelectKeyComboBoxListener(transactionIDcolumn);
        new SelectKeyComboBoxListener(itemIDcolumn);
        for (Table table : tablesList) {
            tableColumn.getItems().add(table);

        }
        tableColumn.valueProperty().addListener(new ChangeListener<Table>() {
            @Override
            public void changed(ObservableValue ov, Table t, Table t1) {
                System.out.println("changed");
                itemIDcolumn.getItems().clear();
                transactionIDcolumn.getItems().clear();
                for (Kolonne kol : t1.listofColumns) {
                    itemIDcolumn.getItems().add(kol.NAVN);
                    transactionIDcolumn.getItems().add(kol.NAVN);

                }
            }
        });
        addElements();
    }

    @Override
    public void addElements() {
        pane.add(new Label(
                "Select which table that contains SalesLine table"), 0, 0);
        pane.add(tableColumn, 1, 0);

        pane.add(new Label("Select a column that represents the transactionID: "), 0, 1);
        pane.add(transactionIDcolumn, 1, 1);
        pane.add(new Label("Select a column that represents the itemID: "), 0, 2);
        pane.add(itemIDcolumn, 1, 2);
        pane.add(new Label(""), 0, 3);
        pane.add(new Label(""), 0, 4);

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
        fulfilled = ValidatorRegler.ikkeTom(transactionIDcolumn);
        fulfilled = ValidatorRegler.ikkeTom(itemIDcolumn);
        return fulfilled;
    }

    @Override
    public void onEnter() {

    }
}

/*Table table = getSelectedTable();
 ComboBox choiceBoxNames = new ComboBox();
 new SelectKeyComboBoxListener(choiceBoxNames);

 for (Kolonne kolonne : table.listofColumns) {
 choiceBoxNames.getItems().add(kolonne.NAVN);
 }

 CheckBox checkBox = new CheckBox("Check here if you want to just count the rows, instead of using their actual value");
 ComboBox choiceBoxValues = new ComboBox();
 new SelectKeyComboBoxListener(choiceBoxValues);

 for (Kolonne kolonne : table.listofColumns) {
 choiceBoxValues.getItems().add(kolonne.NAVN);

 }

 Wizard wizard = new Wizard();

 wizard.setTitle("Connection Wizard");

 // --- page 1
 int row = 0;

 GridPane page1Grid = new GridPane();
 page1Grid.setVgap(10);
 page1Grid.setHgap(30);

 page1Grid.add(new Label("Check which field that represent the categories: "), 0, row);

 wizard.getValidationSupport().registerValidator(choiceBoxNames, Validator.createEmptyValidator("You must select what represents names"));
 page1Grid.add(choiceBoxNames, 1, row++);

 page1Grid.add(new Label("Check which field that represent the values: "), 0, row);

 wizard.getValidationSupport().registerValidator(choiceBoxValues, Validator.createEmptyValidator("You must select what represents values"));
 page1Grid.add(choiceBoxValues, 1, row++);
 page1Grid.add(checkBox, 1, row);

 Wizard.WizardPane page1 = new Wizard.WizardPane();
 page1.setHeaderText("Please select your columns for visualizing");
 page1.setContent(page1Grid);

 wizard.setFlow(new LinearFlow(page1));

 // show wizard and wait for response
 wizard.showAndWait().ifPresent(result -> {
 if (result == ButtonType.FINISH) {
 int en = choiceBoxNames.getSelectionModel().getSelectedIndex();
 int to = choiceBoxValues.getSelectionModel().getSelectedIndex();
 Boolean rowCount = checkBox.selectedProperty().getValue();
 try {
 Tab tab;
 if (whichSelectedView.equals("insightView")) {
 tab = tabPaneInsight.getSelectionModel().getSelectedItem();
 createChart(whichVisualizationType, en, to, tab, newSeries, rowCount);

 } else if (whichSelectedView.equals("tableView")) {
 tab = tabPane.getSelectionModel().getSelectedItem();
 createChart(whichVisualizationType, en, to, tab, newSeries, rowCount);
 }

 } catch (Exception e) {
 ErrorDialog("Invalid columns detected", "The first column has to be a text column, and the second one has to contain numbers");
 System.out.println(e);

 }

 }

 }
 );
 }
 */
