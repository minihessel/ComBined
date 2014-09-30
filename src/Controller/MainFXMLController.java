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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Eskil Hesselroth
 */
public class MainFXMLController implements Initializable {

    DragAbleTreeView dragAbleTreeView;
    ArrayList<Table> tablesList;

    public MainFXMLController() {
        dragAbleTreeView = new DragAbleTreeView();
        tablesList = new ArrayList<Table>();

    }

    public ArrayList<Table> getTablesList() {
        return this.tablesList;
    }

    Table tbl3 = new Table();

    ArrayList<TreeView> listemedView = new ArrayList<TreeView>();

    TextField txtIP = new TextField();
    TextField txtPort = new TextField();
    TextField txtInstance = new TextField();
    ChoiceBox choiceBoxTables = new ChoiceBox();

    Map<TreeItem, Kolonne> mapOverKolonnerOgTreItems = new HashMap<TreeItem, Kolonne>();

    Dialog dlg;
    public int tabPaneCounter = 0;
    static Stage stage; 
    @FXML
    private Label label;
    
     @FXML
    private Button btnNewConnection;
       @FXML
    private Button btnConnectedTables;
         @FXML
    private Button btnVisualize;
         
           @FXML
    private Button btnCombine;
     
    @FXML
    Button visualizeButton;

    @FXML
    Button newConnectionButton;

    @FXML
    Button combineButton;
    final Tooltip tooltip = new Tooltip();

    @FXML
    AnchorPane anchorPaneTables;

    @FXML
    AnchorPane anchorPaneVisualize;

    @FXML
    AnchorPane anchorPaneCombine;

    @FXML
    SplitPane splitPane;

    @FXML
    TabPane tabPane;

    @FXML
    TreeView treeViewCombinedColumns;

    @FXML
    TabPane hBoxWithTreeViews;

    @FXML
            ImageView imageView;
    
    List<Kolonne> listOfCombinedColumns = new ArrayList<Kolonne>();
    List<String> listOfColumnNames = new ArrayList<String>();

    TreeItem<String> kombinerteKolonnerRoot = new TreeItem<String>("List of combined columns");
    
    String whichView; 
    
    

    @FXML
    private void visualizeButton(ActionEvent event) {
        setVisibleView("visualizeView");

    }

    @FXML
    private void handleDataButton(ActionEvent event) {
        setVisibleView("tableView");
        whichView = "tableView";
    }

    @FXML
    private void combineButton(ActionEvent event) {
        setVisibleView("combineView");
    }

    public void setVisibleView(String whichView) {
        if (whichView == "visualizeView") {
            anchorPaneVisualize.setVisible(true);
            anchorPaneTables.setVisible(false);
            anchorPaneCombine.setVisible(false);
        }

        if (whichView == "tableView") {
            anchorPaneVisualize.setVisible(false);
            anchorPaneTables.setVisible(true);
            anchorPaneCombine.setVisible(false);
        }

        if (whichView == "combineView") {
            anchorPaneVisualize.setVisible(false);
            anchorPaneTables.setVisible(false);
            anchorPaneCombine.setVisible(true);
        }

    }

    @FXML
    private void newConnectionButton(ActionEvent event) throws IOException, SQLException {
        setVisibleView("tableView");
      
 
        
        //  openDialogWithSQLConnectionInfo();
        createTabPaneWithTable("test");
        treeViewCombinedColumns.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));


    }

    @FXML
    private void btnMakeTreeViewWithCombined(ActionEvent event) throws IOException {

        TableView tableViewCombined = new TableView();
        Tab tab = new Tab();
        tab.setContent(tableViewCombined);
        tabPane.getTabs().add(tab);
     tableViewCombined = tbl3.fillTableView(tableViewCombined, tbl3);
    }
    
        @FXML
    private void btnHelp(ActionEvent event) throws IOException {
       
IntroController introController = new IntroController();

introController.openWindow();
        
        
        
    }

    @FXML
    private void barChartButton(ActionEvent event) {
    }

    @FXML
    private void pieChartButton(ActionEvent event) {

    }

    @FXML
    private void lineChartButton(ActionEvent event) {

    }
    
  
    
      @FXML
    private void btnNextUserInterFaceIntro(ActionEvent event) {

    }

    @FXML
    private void createNewCombinedColumnButton(ActionEvent event) {
        createNewCombinedColumn(tbl3);
    }

    private void createNewCombinedColumn(Table tbl) {
        Image nodeImage = new Image(
                getClass().getResourceAsStream("/Icons/root.png"));
        //metode for å lage en ny kombinert kolonne
        //først lager vi trenoden i treeviewen
        TreeItem treItem = new TreeItem(" ", new ImageView(nodeImage));
        System.out.println(treItem.getGraphic());

        //vi puncher ut en melding og spør brukeren hva han vil kalle den kombinerte kolonnen
        Optional<String> response = Dialogs.create()
                .title("Text Input Dialog")
                .masthead("Look, a Text Input Dialog")
                .message("Please enter your name:")
                .showTextInput("Column name");

        //den kombinerte kolonnen skal hete:
        response.ifPresent(name
                -> treItem.setValue(name));

        if (response.isPresent() == false) {
            treItem.setValue("unnamed");

        }

        //noden i treeviewet skal være utvidet
        treItem.expandedProperty().set(true);

        //vi lager en liste(som tilsvarer en kombinert kolonne). Listen skal inneholde kolonner(altså den kombinerte kolonnen skal inneholde hvilke kolonner den skal være)   
        //  List<Kolonne> combinedColumn = new ArrayList<Kolonne>();
        Kolonne kol = new Kolonne(treItem.getValue().toString(), tbl, true);

        //vi legger til den nye combinedColumn(den nye kombinerte kolonnen i combinedColumn over kombinerte kolonner
 
        tbl.listofColumns.add(kol);
        listOfColumnNames.add(treItem.getValue().toString());
        //deretter mapper vi den nye kombinerte kolonnen opp mot treitemet, sånn at vi senere kan hente ut den kombinerte kolonnen
        mapOverKolonnerOgTreItems.put(treItem, kol);
        //Deretter putter vi kolonnen i treeviewet for kombinerte kolonner
        kombinerteKolonnerRoot.getChildren().add(treItem);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {



        
        treeViewCombinedColumns.setRoot(kombinerteKolonnerRoot);
        treeViewCombinedColumns.setShowRoot(false);
        

        
        btnConnectedTables.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
btnCombine.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnVisualize.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        Image noTables = new Image(
                getClass().getResourceAsStream("/Icons/no_tables.png"));
         imageView.setImage(noTables);
        imageView.visibleProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        
        


    }

    final Action actionListenerSQLConnect = new AbstractAction("Login") {
        // This method is called when the  button is clicked ...
        public void handle(ActionEvent ae) {

            Dialog dlg = (Dialog) ae.getSource();

            SQL_manager sql_manager = new SQL_manager();

            try {
                Connection conn = sql_manager.getConnection(txtIP.getText(), Integer.parseInt(txtPort.getText()), txtInstance.getText());
            } catch (SQLException ex) {
                Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                sql_manager.getAllTables(choiceBoxTables);
            } catch (SQLException ex) {
                Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (Object k : choiceBoxTables.getItems().toArray()) {
                System.out.println(k);
            }
            openDialogWithWhichTableInfo();
            dlg.hide();

        }

    };

    final Action actionListenerWhichTable = new AbstractAction("OK") {
        // This method is called when the button is clicked ...
        public void handle(ActionEvent ae) {
            Dialog d = (Dialog) ae.getSource();
            d.hide();
            try {
                createTabPaneWithTable(choiceBoxTables.getSelectionModel().getSelectedItem().toString());
            } catch (SQLException ex) {
                Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    };

    private void openDialogWithSQLConnectionInfo() {

        // Create the custom dialog.
        dlg = new Dialog(null, "Login Dialog", false, DialogStyle.UNDECORATED);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        grid.add(new Label("IP:"), 0, 0);
        grid.add(txtIP, 1, 0);
        grid.add(new Label("Port:"), 0, 1);
        grid.add(txtPort, 1, 1);

        grid.add(new Label("Instance:"), 0, 2);
        grid.add(txtInstance, 1, 2);

        ButtonBar.setType(actionListenerSQLConnect, ButtonBar.ButtonType.OK_DONE);

        // Do some validation (using the Java 8 lambda syntax).
        dlg.setMasthead("Please enter your connection information to your database");
        dlg.setContent(grid);
        dlg.getActions().addAll(actionListenerSQLConnect, Dialog.Actions.CANCEL);

        dlg.show();

    }

    private void openDialogWithWhichTableInfo() {

        // Create the custom dialog.
        dlg = new Dialog(null, "OK", false, DialogStyle.UNDECORATED);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.add(new Label("Table:"), 0, 0);
        grid.add(choiceBoxTables, 1, 0);
        ButtonBar.setType(actionListenerWhichTable, ButtonBar.ButtonType.OK_DONE);
        // Do some validation (using the Java 8 lambda syntax).
        dlg.setMasthead("Great, now which table would you like to get data from?");
        dlg.setContent(grid);
        dlg.getActions().addAll(actionListenerWhichTable, Dialog.Actions.CANCEL);

        dlg.show();

    }

    public void createTabPaneWithTable(String whichTable) throws SQLException {
        //Hver gang brukeren kobler til en ny tabell, lager vi en ny tabpane
        //dette for å kunne organisere tabeller og vite hvilken rekkefølge de er i
        VBox vBox = new VBox();

        Table tabellen = new Table();
        //  String query = textField.getText();
        SQL_manager sql_manager = new SQL_manager();
        //her skjer oppkoblingen
        sql_manager.getConnection("localhost", 8889, "eskildb");

        //laster inn dataen med en query
        tabellen.loadData("select * from " + whichTable + "  ", sql_manager, tabellen, tabPaneCounter);

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
            Image nodeImage = new Image(
                getClass().getResourceAsStream("/Icons/root.png"));
            TreeItem treItem = new TreeItem();
            treItem.setGraphic(new ImageView(nodeImage));
            treItem.setValue(kol.NAVN);
            treeView2Root.getChildren().add(treItem);
        }
        treeView.setRoot(treeView2Root);
        treeView.setShowRoot(false);

        dragAbleTreeView.makeTreeViewDragAble(treeView, this);
        listemedView.add(treeView);

        Tab tab = new Tab();

        Optional<String> response = Dialogs.create()
                .title("Your table name")
               
                .message("What would you like to name your table in the tabview?")
                .showTextInput(" ");

        //spør brukeren hva denne tabpanen skal hete
        response.ifPresent(name
                -> tab.setText(name));

        if (response.isPresent() == false) {
            tab.setText("Unnamed");
        }
        treeView2Root.setValue(tab.getText());
        tabPane.getTabs().add(tab);

        tab.setContent(vBox);
        tab.setId("" + tabPaneCounter);

        VBox pane = new VBox();

        treeView.setUserData(tabPaneCounter);

        pane.getChildren().add(treeView);
        Tab treeViewTab = new Tab();
        treeViewTab.setText(tab.getText());
        treeViewTab.setContent(pane);

        hBoxWithTreeViews.getTabs().add(treeViewTab);

        tabPaneCounter++;

        tabPane.getSelectionModel().select(tabPaneCounter);

        dragAbleTreeView.makeTreeViewDragAble(treeViewCombinedColumns, this);

        for (Table tbl : tablesList) {
            System.out.println(tbl);
        }
    }

    public void addColumnToCombinedColumn(TreeCell treeCell, int hvilkenTabell, int DRAGGEDINDEX) {
        Kolonne targetCombinedColumn = mapOverKolonnerOgTreItems.get(treeCell.getTreeItem());
        Kolonne sourceDraggedColumn = getTablesList().get(hvilkenTabell).listofColumns.get(DRAGGEDINDEX);

        targetCombinedColumn.listOfColumns.add(sourceDraggedColumn);

    }

    private void makeTableViewWithCombinedColumns(Table tbl, TableView tableView) {
        tbl.listofColumns.clear();
        tableView.getItems().clear();
        tableView.getColumns().clear();

        //Først looper vi igjennom combinedColumn av lister med kolonner(med andre ord er en liste i denne combinedColumn en kombinert kolonne)
   

        //deretter lager vi tableviewet med alle de kombinerte kolonnene. 
        tableView = tbl.fillTableView(tableView, tbl);

    }

}
