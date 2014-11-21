/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard.VisualizationWizard;

import Controller.SelectKeyComboBoxListener;
import View.Wizard.ValidatorRegler;
import View.Wizard.WizardPage;
import Model.Kolonne;
import Model.Table;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Eskil Hesselroth
 */
public class VisualizationPage1 implements WizardPage {

    String header = "Please select which columns to visualize";
    public ComboBox nameColumn = new ComboBox();

    public ComboBox valueColumn = new ComboBox();
    public CheckBox checkBox = new CheckBox();
    final private GridPane pane = new GridPane();
    private boolean fulfilled = false;
    Table table;

    public VisualizationPage1(Table table) {
        new SelectKeyComboBoxListener(nameColumn);
        new SelectKeyComboBoxListener(valueColumn);
        this.table = table;
        for (Kolonne kol : table.listofColumns) {
            nameColumn.getItems().add(kol.NAVN);
            valueColumn.getItems().add(kol.NAVN);
        }
        addElements();
    }

    @Override
    public void addElements() {

        pane.add(new Label("You are now visualizing on table: " + table + "\n \n \n \n \n"), 0, 0);
        pane.add(new Label("Select a column that represents the categories: "), 0, 1);
        pane.add(nameColumn, 1, 1);
        pane.add(new Label("Select a column that represents the values: "), 0, 2);
        pane.add(valueColumn, 1, 2);
        pane.add(new Label(""), 0, 3);
        pane.add(new Label(""), 0, 4);
        pane.add(checkBox, 1, 5);
        pane.add(new Label("Select this checkbox if you just want to "), 1, 6);
        pane.add(new Label("count how many rows each category has"), 1, 7);

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
        fulfilled = ValidatorRegler.ikkeTom(nameColumn);
        fulfilled = ValidatorRegler.ikkeTom(valueColumn);
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
