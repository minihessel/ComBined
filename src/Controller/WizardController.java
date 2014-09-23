/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import combined.SQL_manager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Eskil Hesselroth
 */
public class WizardController implements Initializable {

    ObservableList<Node> lister = FXCollections.observableArrayList();
    @FXML
    private TextField txtIPadress;
    @FXML
    private TextField txtPortNumber;
    @FXML
    private TextField txtSQLinstance;
    @FXML
    private ChoiceBox choiceBoxTable;
    
        ArrayList<Table> tablesList = new ArrayList<Table>();
    
        public int tabPaneCounter = 0;

    @FXML
    private Group groupConnectionInfo;
    @FXML
    private Group groupTableSelecter;

    String selectedTable;
    public TabPane tabPane;

    static Stage dialogStage = new Stage();
    HBox hBoxWithTreeViews;

    public void openStage(Parent root) {
        if (!dialogStage.isShowing()) {

            dialogStage.setAlwaysOnTop(true);

            dialogStage.setScene(new Scene(root));
            dialogStage.show();

        }

    }

    @FXML
    private void handleConnectToDatabaseButton(ActionEvent event) throws SQLException, IOException {
        SQL_manager sql_manager = new SQL_manager();
        Connection conn = sql_manager.getConnection(txtIPadress.getText(), Integer.parseInt(txtPortNumber.getText()), txtSQLinstance.getText());
        sql_manager.getAllTables(choiceBoxTable);
        groupConnectionInfo.setVisible(false);
        groupTableSelecter.setVisible(true);
        choiceBoxTable.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                selectedTable = t1;

            }
        });

    }

   private void createTableViewWithData(Tab tab) throws SQLException {
        VBox vBox = new VBox();

        Table tabellen = new Table();
        //    String query = textField.getText();
        SQL_manager sql_manager = new SQL_manager();
        //her skjer oppkoblingen
        sql_manager.getConnection("localhost", 8889, "eskildb");

        //laster inn dataen med en query
        tabellen.loadData("select * from test", sql_manager, tabellen, 1);

        //legger til den nye tilkoblede tabellen i listen over tilkoblede tabeller
          tablesList.add(tabPaneCounter, tabellen);
        TableView tableViewet = new TableView();
        //legger til tableviewet i tabben
        vBox.getChildren().add(tableViewet);
        tableViewet = tabellen.fillTableView(tableViewet, tabellen);
        vBox.setId("" + tabPaneCounter);

        //lager en ny treeview med en liste over alle kolonnene i tabellen
        TreeView treeView = new TreeView();

        TreeItem<String> treeView2Root = new TreeItem<String>("MYSQL");

        for (Kolonne kol : tabellen.listofColumns) {
            
            treeView2Root.getChildren().add(new TreeItem<>(kol.NAVN));
        }
        treeView.setRoot(treeView2Root);
        treeView.setUserData(tabPaneCounter);
        treeView.setShowRoot(false);

    
        //  listemedView.add(treeView);
        Optional<String> responsen = Dialogs.create()
                .title("Text Input Dialog")
                .masthead("Look, a Text Input Dialog")
                .message("Please enter your name:")
                .showTextInput("walter");

        //spør brukeren hva denne tabpanen skal hete
        responsen.ifPresent(name
                -> tab.setText(name));

        if (responsen.isPresent() == false) {
            tab.setText("Unnamed");
        }
        treeView2Root.setValue(tab.getText());

        tab.setContent(vBox);
        tab.setId("" + tabPaneCounter);

         tabPaneCounter++;
          VBox pane = new VBox();
        Label lbl = new Label("TABELL OG SERVER 3:");
     
        pane.getChildren().add(lbl);
        pane.getChildren().add(treeView);
        
        AddDragAndDropFunctionality addDragAndDropFunctionality = new AddDragAndDropFunctionality();
        addDragAndDropFunctionality.makeTreeViewDragAble(treeView);
 
    

        hBoxWithTreeViews.getChildren().add(pane);

    }

    @FXML
    private void handleConnectToTable(ActionEvent event) throws SQLException {
        Tab tab = new Tab();

        dialogStage.close();

        createTableViewWithData(tab);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
