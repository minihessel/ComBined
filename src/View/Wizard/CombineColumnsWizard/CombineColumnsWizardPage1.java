/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard.CombineColumnsWizard;

import Controller.SelectKeyComboBoxListener;
import static Model.ErrorDialog.ErrorDialog;
import View.Wizard.WizardPage;
import Model.Kolonne;
import Model.Table;
import View.Wizard.ValidatorRegler;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author Eskil Hesselroth
 */
public class CombineColumnsWizardPage1 implements WizardPage {

    String header = "Combine your columns";

    final private GridPane pane = new GridPane();
    BorderPane splitPane = new BorderPane();
    BorderPane borderPane = new BorderPane();

    GridPane listOfTables = new GridPane();
    GridPane nameYourColumnPane = new GridPane();
    GridPane listOfColumnsPane = new GridPane();
    private boolean fulfilled = false;
    List<Table> tablesList;
    List<ComboBox> choiceBoxList = new ArrayList();
    Button button = new Button("Add column");
    TextField textField = new TextField();
    public Table combinedTable = new Table("combined table");
    ListView<Kolonne> listView = new ListView();

    public CombineColumnsWizardPage1(List<Table> tablesList) {
        listOfTables.setStyle("-fx-border-color: black;-fx-border-width:0.1px;");
        listOfColumnsPane.setStyle("-fx-border-color: black;-fx-border-width:0.1px;");
        nameYourColumnPane.setStyle("-fx-border-color: black;-fx-border-width:0.1px;");

        splitPane.setStyle(".split-pane *.split-pane-divider { -fx-padding: 0 1 0 1;");
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem delete = new MenuItem("Delete");

        contextMenu.getItems().addAll(delete);

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

                if (t.getTarget() instanceof Text) {

                    if (t.getButton() == MouseButton.SECONDARY) {
                        delete.setOnAction(new EventHandler() {
                            public void handle(Event t) {
                                combinedTable.listofColumns.remove(listView.getSelectionModel().getSelectedItem());
                                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());

                            }

                        });
                        Node n = (Node) t.getTarget();
                        contextMenu.show(n, t.getScreenX(), t.getScreenY());
                    }

                }
            }
        });

        listOfTables.setPadding(
                new Insets(20, 20, 20, 20));
        nameYourColumnPane.setPadding(
                new Insets(20, 20, 20, 20));
        listOfColumnsPane.setPadding(
                new Insets(20, 20, 20, 20));
        splitPane.setMaxHeight(
                740);

        this.tablesList = tablesList;

        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e
                    ) {
                        int atLeastTwoTablesSelected = 0;

                        for (ComboBox cb : choiceBoxList) {
                            if (!cb.getSelectionModel().isEmpty()) {
                                atLeastTwoTablesSelected++;
                            }
                        }
                        if (atLeastTwoTablesSelected
                        > 1 && !textField.getText()
                        .isEmpty()) {
                            Kolonne kol = new Kolonne(textField.getText(), combinedTable, true, combinedTable.listofColumns.size() + 1);
                            for (ComboBox cb : choiceBoxList) {
                                if (!cb.getSelectionModel().isEmpty()) {
                                    Kolonne addCol = (Kolonne) cb.getSelectionModel().getSelectedItem();
                                    kol.listOfColumns.add(addCol);
                                    cb.setValue(null);
                                }
                            }

                            combinedTable.listofColumns.add(kol);
                            textField.setText("");

                            listView.setItems(FXCollections.observableArrayList(combinedTable.listofColumns));

                        } else if (textField.getText()
                        .isEmpty()) {
                            ErrorDialog("Your new column need a new name", "Please fill in the name of your new column in the textfield");

                        } else {

                            ErrorDialog("Not enough columns selected", "You haven't selected enough columns. A combined column got to contain at least two columns ");

                        }
                    }
                }
        );

        addElements();
    }

    @Override
    public void addElements() {
        Label combineColumnNameLbl = new Label("2. Name your column");
        combineColumnNameLbl.underlineProperty().set(true);
        combineColumnNameLbl.setPadding(new Insets(0, 0, 50, 0));
        Label listOfTablesLbl = new Label("1. Select columns");
        listOfTablesLbl.setPadding(new Insets(0, 0, 50, 0));
        listOfTablesLbl.underlineProperty().set(true);
        Label listOfColumnsLbl = new Label("Combined columns in your new table");
        listOfColumnsLbl.setPadding(new Insets(0, 0, 50, 0));
        listOfColumnsLbl.underlineProperty().set(true);
        nameYourColumnPane.add(combineColumnNameLbl, 0, 0);
        nameYourColumnPane.add(new Label("Column name: "), 0, 1);
        nameYourColumnPane.add(textField, 1, 1);
        nameYourColumnPane.add(button, 2, 1);

        listOfTables.add(listOfTablesLbl, 0, 0);
        splitPane.setLeft(listOfTables);
        splitPane.setCenter(nameYourColumnPane);
        listOfColumnsPane.add(listOfColumnsLbl, 0, 0);
        listOfColumnsPane.add(listView, 0, 1);

        splitPane.setRight(listOfColumnsPane);

        pane.add(splitPane, 0, 0);

        int rowCounter = 1;
        for (Table table : tablesList) {

            Label lbl = new Label("Table : " + table.NAVN);

            ComboBox<Kolonne> cb = new ComboBox();
            new SelectKeyComboBoxListener(cb);
            choiceBoxList.add(cb);
            for (Kolonne kol : table.listofColumns) {
                cb.getItems().add(kol);
            }

            listOfTables.add(lbl, 0, rowCounter);
            rowCounter++;
            listOfTables.add(cb, 0, rowCounter);
            rowCounter++;

        }

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
        //fulfilled = ValidatorRegler.ikkeTom(transactionIDcolumn);
        //fulfilled = ValidatorRegler.ikkeTom(itemIDcolumn);
        return fulfilled;
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onFinish() {

    }
}
