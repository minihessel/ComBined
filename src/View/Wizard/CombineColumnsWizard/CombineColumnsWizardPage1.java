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
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author Eskil Hesselroth
 */
public class CombineColumnsWizardPage1 implements WizardPage {

    String header = "Combine your columns";

    final private GridPane pane = new GridPane();
    SplitPane splitPane = new SplitPane();

    VBox vbox = new VBox();
    FlowPane nameYourColumnPane = new FlowPane();
    FlowPane listOfColumnsPane = new FlowPane();
    private boolean fulfilled = false;
    List<Table> tablesList;
    List<ComboBox> choiceBoxList = new ArrayList();
    Button button = new Button("Add column");
    TextField textField = new TextField("Name your new combined column");
    public Table combinedTable = new Table("combined table");
    ListView<Kolonne> listView = new ListView();

    public CombineColumnsWizardPage1(List<Table> tablesList) {
        vbox.setStyle("-fx-border-color: black;-fx-border-width:0.1px;");
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

        vbox.setPadding(
                new Insets(20, 20, 20, 20));
        nameYourColumnPane.setPadding(
                new Insets(20, 20, 20, 20));
        listOfColumnsPane.setPadding(
                new Insets(20, 20, 20, 20));
        splitPane.setMaxHeight(
                740);
        splitPane.setBackground(Background.EMPTY);

        splitPane.setOrientation(Orientation.HORIZONTAL);

        this.tablesList = tablesList;

        nameYourColumnPane.setOrientation(Orientation.VERTICAL);

        listOfColumnsPane.setOrientation(Orientation.VERTICAL);

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
                            textField.setText(null);

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
        Label combineColumnNameLbl = new Label("Please name your new combined column");
        combineColumnNameLbl.underlineProperty().set(true);
        combineColumnNameLbl.setPadding(new Insets(0, 0, 50, 0));
        Label listOfTablesLbl = new Label("Your connected tables");
        listOfTablesLbl.setPadding(new Insets(0, 0, 50, 0));
        listOfTablesLbl.underlineProperty().set(true);
        Label listOfColumnsLbl = new Label("Your created columns");
        listOfColumnsLbl.setPadding(new Insets(0, 0, 50, 0));
        listOfColumnsLbl.underlineProperty().set(true);
        nameYourColumnPane.getChildren().addAll(combineColumnNameLbl, textField);
        nameYourColumnPane.getChildren().add(button);
        vbox.getChildren().add(listOfTablesLbl);
        splitPane.getItems().add(vbox);
        splitPane.getItems().add(nameYourColumnPane);
        listOfColumnsPane.getChildren().add(listOfColumnsLbl);
        listOfColumnsPane.getChildren().add(listView);

        splitPane.getItems().add(listOfColumnsPane);

        pane.add(splitPane, 0, 0);

        for (Table table : tablesList) {
            FlowPane flowPane = new FlowPane();
            Label lbl = new Label("Table : " + table.NAVN);
            flowPane.setOrientation(Orientation.VERTICAL);

            ComboBox<Kolonne> cb = new ComboBox();
            new SelectKeyComboBoxListener(cb);
            choiceBoxList.add(cb);
            for (Kolonne kol : table.listofColumns) {
                cb.getItems().add(kol);
            }
            flowPane.getChildren().addAll(lbl, cb);
            vbox.getChildren().add(flowPane);

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
        // fulfilled = ValidatorRegler.ikkeTom(whichTypeOfInsight);
        //fulfilled = ValidatorRegler.ikkeTom(transactionIDcolumn);
        //fulfilled = ValidatorRegler.ikkeTom(itemIDcolumn);
        return true;
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onFinish() {

    }
}
