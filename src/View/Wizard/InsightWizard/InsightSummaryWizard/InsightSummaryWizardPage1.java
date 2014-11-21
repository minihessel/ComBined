/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard.InsightWizard.InsightSummaryWizard;

import Controller.SelectKeyComboBoxListener;
import Model.Kolonne;
import View.Wizard.ValidatorRegler;
import View.Wizard.WizardPage;
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
public class InsightSummaryWizardPage1 implements WizardPage {

    String header = "cted tables that represents your itemtable\n qweqweqweqwe";
    public ComboBox<Table> tableColumn = new ComboBox();
    public ComboBox itemIDColumn = new ComboBox();
    public ComboBox itemDescriptionColumn = new ComboBox();
    public CheckBox checkBox = new CheckBox();
    final private GridPane pane = new GridPane();
    private boolean fulfilled = false;

    public InsightSummaryWizardPage1(List<Table> tableList) {
        new SelectKeyComboBoxListener(tableColumn);

        for (Table table : tableList) {
            tableColumn.getItems().add(table);

        }
        tableColumn.valueProperty().addListener(new ChangeListener<Table>() {
            @Override
            public void changed(ObservableValue ov, Table t, Table t1) {
                System.out.println("changed");
                itemIDColumn.getItems().clear();
                itemDescriptionColumn.getItems().clear();
                for (Kolonne kol : t1.listofColumns) {
                    itemIDColumn.getItems().add(kol.NAVN);
                    itemDescriptionColumn.getItems().add(kol.NAVN);

                }
            }
        });

        addElements();
    }

    @Override
    public void addElements() {

        pane.add(new Label("Select which table that represents the itemtable "), 0, 0);
        pane.add(tableColumn, 1, 0);

        pane.add(new Label("Select which table that represents the itemID "), 0, 1);
        pane.add(itemIDColumn, 1, 1);
        pane.add(new Label("Select which table that represents the itemDescription "), 0, 2);
        pane.add(itemDescriptionColumn, 1, 2);

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
        fulfilled = ValidatorRegler.ikkeTom(tableColumn);
        return fulfilled;
    }

    @Override
    public void onEnter() {
    }
    
    @Override
    public void onFinish()
    {
        
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
