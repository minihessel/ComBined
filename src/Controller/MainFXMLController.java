/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import combined.SQL_manager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialog;

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
    String whichHelpView;
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
    TextField filterField;

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
    Separator separator;
    @FXML
    ImageView imageView;

    List<Kolonne> listOfCombinedColumns = new ArrayList<Kolonne>();
    List<String> listOfColumnNames = new ArrayList<String>();

    TreeItem<String> kombinerteKolonnerRoot = new TreeItem<String>("List of combined columns");

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/helpme.fxml"));

    IntroController introController;

    @FXML
    BorderPane borderPane;
    @FXML
    VBox vBoxMenu;
    @FXML
    Button btnMenu;

    @FXML
    private void visualizeButton(ActionEvent event) {
        setVisibleView("visualizeView");

    }

    @FXML
    private void handleDataButton(ActionEvent event) {
        setVisibleView("tableView");
        whichHelpView = "tableView";
    }

    @FXML
    private void combineButton(ActionEvent event) {
        setVisibleView("combineView");
        whichHelpView = "combineView";
        System.out.println("BEFORE + " + treeViewCombinedColumns.getStyleClass());
        for (String k : treeViewCombinedColumns.getStyleClass()) {
            System.out.println(k);
        }
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
        createTabPaneWithTable("employees");

    }

    @FXML
    private void btnMakeTreeViewWithCombined(ActionEvent event) throws IOException {

        TableView tableViewCombined = new TableView();

        Tab tab = new Tab();

        tab.setContent(tableViewCombined);
        tabPane.getTabs().add(tab);
        tableViewCombined = tbl3.fillTableView(tableViewCombined, tbl3,filterField);
    }

    @FXML
    private void btnHelp(ActionEvent event) throws IOException {

        loadHelpScreen();

    }

    void loadHelpScreen() throws IOException {
        Parent root = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root));
        introController = fxmlLoader.getController();
        introController.setValue(whichHelpView);
        stage.showAndWait();

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
                getClass().getResourceAsStream("/Icons/combined_column_icon.png"));
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
        SideBar sidebar = new SideBar(btnMenu, 90, vBoxMenu);

        borderPane.setLeft(sidebar);

        treeViewCombinedColumns.setRoot(kombinerteKolonnerRoot);
        treeViewCombinedColumns.setShowRoot(false);
        treeViewCombinedColumns.setUserData("combinedColumnTree");

        btnConnectedTables.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnCombine.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnVisualize.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        Image noTables = new Image(
                getClass().getResourceAsStream("/Icons/no_tables.png"));
        imageView.setImage(noTables);
        imageView.visibleProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        System.out.println("BEFORE + " + treeViewCombinedColumns.getStyleClass());
    }

    public void createTabPaneWithTable(String whichTable) throws SQLException {
        //Hver gang brukeren kobler til en ny tabell, lager vi en ny tabpane
        //dette for å kunne organisere tabeller og vite hvilken rekkefølge de er i
        VBox vBox = new VBox();

        Table tabellen = new Table();
        //  String query = textField.getText();
        SQL_manager sql_manager = new SQL_manager();
        //her skjer oppkoblingen
        sql_manager.getConnection("localhost", 8889, "employees");

        //laster inn dataen med en query
        tabellen.loadData("select * from " + whichTable + "  ", sql_manager, tabellen, tabPaneCounter);

        //legger til den nye tilkoblede tabellen i listen over tilkoblede tabeller
        tablesList.add(tabPaneCounter, tabellen);

        TableView tableViewet = new TableView();
        System.out.println("vBOX HEIGHT : " + vBox.getHeight());
        System.out.println("TABPANE HEIGHT : " + tabPane.getHeight());
        System.out.println("TABLEVIEW HEIGHT : " + tableViewet.getHeight());

        //legger til tableviewet i tabben
        vBox.getChildren().add(tableViewet);
        tableViewet = tabellen.fillTableView(tableViewet, tabellen,filterField);
        vBox.setId("" + tabPaneCounter);
        vBox.setMinHeight(100000000);
        vBox.setPrefHeight(10000000);
        vBox.setMaxHeight(100000000);

        //lager en ny treeview med en liste over alle kolonnene i tabellen
        TreeView treeView = new TreeView();

        TreeItem<String> treeView2Root = new TreeItem<String>("MYSQL");

        for (Kolonne kol : tabellen.listofColumns) {

            TreeItem treItem = new TreeItem();
            treItem.setGraphic(new ImageView(new Image(
                    getClass().getResourceAsStream("/Icons/column_icon.png"))));
            treItem.setValue(kol.NAVN);
            mapOverKolonnerOgTreItems.put(treItem, kol);
            treeView2Root.getChildren().add(treItem);
        }
        treeView.setRoot(treeView2Root);
        treeView.setShowRoot(false);

        dragAbleTreeView.makeTreeViewDragAble(treeView, this);
        listemedView.add(treeView);

        Tab tab = new Tab();

        tab.setText(whichTable + "@" + sql_manager.instanceName);
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

    public void removeColumnToCombinedColumn(TreeCell treeCell) {
        Kolonne parentColumn = mapOverKolonnerOgTreItems.get(treeCell.getTreeItem().getParent());
        // Kolonne targetCombinedColumn = mapOverKolonnerOgTreItems.get(treeCell.getTreeItem());
        Kolonne targetForDelete = mapOverKolonnerOgTreItems.get(treeCell.getTreeItem());
        //    System.out.println(parentColumn);

        parentColumn.listOfColumns.remove(targetForDelete);
    }

    private void makeTableViewWithCombinedColumns(Table tbl, TableView tableView) {
        tbl.listofColumns.clear();
        tableView.getItems().clear();
        tableView.getColumns().clear();

        //Først looper vi igjennom combinedColumn av lister med kolonner(med andre ord er en liste i denne combinedColumn en kombinert kolonne)
        //deretter lager vi tableviewet med alle de kombinerte kolonnene. 
        tableView = tbl.fillTableView(tableView, tbl,filterField);

    }

}
