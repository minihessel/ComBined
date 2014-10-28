/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eskil Hesselroth
 */
public class CombineColumnsController implements Initializable {

    @FXML
    TextField textField;

    TabPane myTabPane;
    @FXML
    ListView<Kolonne> listView;
    Table combinedTable = new Table("combined table");
    final ObservableList<Kolonne> data = FXCollections.observableArrayList();
    List<Table> myList;
    List<ChoiceBox> choiceBoxList = new ArrayList();

    @FXML
    VBox vBox;

    @FXML
    Button btnFinish;

    /**
     * Initializes the controller class.
     */
    @FXML
    private void btnAdd(ActionEvent event
    ) {
        Kolonne kol = new Kolonne(textField.getText(), combinedTable, true, combinedTable.listofColumns.size() + 1);
        for (ChoiceBox cb : choiceBoxList) {

            Kolonne addCol = myList.get(Integer.parseInt(cb.getUserData().toString())).listofColumns.get(cb.getSelectionModel().getSelectedIndex());
            kol.listOfColumns.add(addCol);
        }

        combinedTable.listofColumns.add(kol);

        listView.setItems(FXCollections.observableArrayList(combinedTable.listofColumns));

    }

    @FXML
    private void btnFinish(ActionEvent event
    ) {
        TableView tableViewCombined = new TableView();
        tableViewCombined = combinedTable.fillTableView(tableViewCombined, combinedTable);
        Tab tab = new Tab("combined table");
        tab.setContent(tableViewCombined);
        myTabPane.getTabs().add(tab);
        Stage stage = (Stage) btnFinish.getScene().getWindow();
        // do what you have to do
        stage.close();

        //to select the last tab that has been selected
    }

    public void setContext(List<Table> tablesList) {
        this.myList = tablesList;

        for (Table tbl : myList) {
            FlowPane pane = new FlowPane();
ImageView imageView = new ImageView(new Image(
                getClass().getResourceAsStream("/Icons/arrow_down.png")));
            // pane.setPrefSize(20, 40);
            pane.setStyle("-fx-border-color: black;-fx-border-width:0.1px;");
            Label lbl = new Label("Table : " + tbl.NAVN);

            ChoiceBox cb = new ChoiceBox();
            cb.setUserData(tbl.tableNumber);
            choiceBoxList.add(cb);
            for (Kolonne kol : tbl.listofColumns) {
                cb.getItems().add(kol.NAVN);
            }

            pane.getChildren().addAll(lbl, cb,imageView);

            vBox.getChildren().add(pane);
        }

        // initialize country dependent data here rather then in initialize()
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
    }

}
