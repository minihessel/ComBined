/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *///
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.Wizard.LinearFlow;
import org.controlsfx.validation.Validator;

/**
 *
 * @author Eskil Hesselroth
 */
public class MainFXMLController implements Initializable {

    Visualize visualize;
    ArrayList<Table> tablesList;
    SQL_manager sql_manager = new SQL_manager();
    Parent root;

    public MainFXMLController() {

        visualize = new Visualize();
        tablesList = new ArrayList<Table>();

    }

    public ArrayList<Table> getTablesList() {
        return this.tablesList;
    }

    TextField txtIP = new TextField();
    TextField txtPort = new TextField();
    TextField txtInstance = new TextField();
    ChoiceBox choiceBoxTables = new ChoiceBox();
    int userSelectedTableInTabPane;

    String whichHelpView;
    Dialog dlg;
    public int tabPaneCounter = 0;
    static Stage stage;
    @FXML
    private Label label;
    @FXML
    private Button btnNewSeries;
    @FXML
    private Button btnNewChart;

    @FXML
    private LineChart lineChart;

    @FXML
    private BarChart barChart;
    @FXML
    private Button btnNewConnection;
    @FXML
    private Button btnConnectedTables;
    @FXML
    private Button btnVisualize;

    @FXML
    private Button btnCombine;

    @FXML
    PieChart pieChart;

    @FXML
    StackedAreaChart areaChart;
    @FXML
    ScatterChart scatterChart;

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
    Separator separator;
    @FXML
    ImageView imageView;

    List<Kolonne> listOfCombinedColumns = new ArrayList<Kolonne>();
    List<String> listOfColumnNames = new ArrayList<String>();

    TreeItem<String> kombinerteKolonnerRoot = new TreeItem<String>("List of combined columns");

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/helpme.fxml"));

    HelpScreenController helpScreenController;

    @FXML
    BorderPane borderPane;
    @FXML
    VBox vBoxMenu;
    @FXML
    Button btnMenu;

    @FXML
    private void btnRemoveFilters(ActionEvent event
    ) {

        if (tabPane.getSelectionModel().getSelectedItem() != null) {
            tablesList.get(tabPane.getSelectionModel().getSelectedIndex()).removeFilters();
        }

    }

    @FXML
    private void visualizeButton(ActionEvent event) {
        setVisibleView("visualizeView");
        whichHelpView = "visualizeView";

    }

    @FXML
    private void btnNewChart(ActionEvent event
    ) {
        if (pieChart.visibleProperty().get()) {
            showLinearWizard("pieChart", false);
        }
        if (lineChart.visibleProperty().get()) {
            showLinearWizard("lineChart", false);
        }
        if (barChart.visibleProperty().get()) {
            showLinearWizard("barChart", false);
        }
        if (areaChart.visibleProperty().get()) {
            showLinearWizard("areaChart", false);
        }
        if (scatterChart.visibleProperty().get()) {
            showLinearWizard("scatterChart", false);
        }

    }

    @FXML
    private void btnNewSeries(ActionEvent event
    ) {
        if (pieChart.visibleProperty().get()) {
            showLinearWizard("pieChart", true);
        }
        if (lineChart.visibleProperty().get()) {
            showLinearWizard("lineChart", true);
        }
        if (barChart.visibleProperty().get()) {
            showLinearWizard("barChart", true);
        }
        if (areaChart.visibleProperty().get()) {
            showLinearWizard("areaChart", true);
        }
        if (scatterChart.visibleProperty().get()) {
            showLinearWizard("scatterChart", true);
        }

    }

    @FXML
    private void handleDataButton(ActionEvent event
    ) {
        setVisibleView("tableView");
        whichHelpView = "tableView";
    }

    @FXML
    private void combineButton(ActionEvent event
    ) throws IOException {
        openNew();

    }

    public void setVisibleView(String whichView) {
        if (whichView == "visualizeView") {
            anchorPaneVisualize.setVisible(true);
            anchorPaneTables.setVisible(false);

        }

        if (whichView == "tableView") {
            anchorPaneVisualize.setVisible(false);
            anchorPaneTables.setVisible(true);

        }

    }

    @FXML
    private void newConnectionButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        setVisibleView("tableView");
        //  openDialogWithSQLConnectionInfo();
        // createTabPaneWithTable("transbig");
        createTabPaneWithTable("transactions");

    }

    @FXML
    private void btnHelp(ActionEvent event) throws IOException {

        loadHelpScreen();

    }

    void loadHelpScreen() throws IOException {

        helpScreenController = fxmlLoader.getController();
        helpScreenController.setValue(whichHelpView);

        stage.showAndWait();

    }

    private void showLinearWizard(String whichVisualizationType, Boolean newSeries) {
        Table table = tablesList.get(Integer.parseInt(tabPane.getSelectionModel().getSelectedItem().getId()));

        ChoiceBox choiceBoxNames = new ChoiceBox();

        for (Kolonne kolonne : table.listofColumns) {
            choiceBoxNames.getItems().add(kolonne.NAVN);
        }

        ChoiceBox choiceBoxValues = new ChoiceBox();

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
        page1Grid.add(choiceBoxValues, 1, row);

        Wizard.WizardPane page1 = new Wizard.WizardPane();
        page1.setHeaderText("Please select your columns for visualizing");
        page1.setContent(page1Grid);

        wizard.setFlow(new LinearFlow(page1));

        // show wizard and wait for response
        wizard.showAndWait().ifPresent(result -> {
            if (result == ButtonType.FINISH) {
                int en = choiceBoxNames.getSelectionModel().getSelectedIndex();
                int to = choiceBoxValues.getSelectionModel().getSelectedIndex();
                try {

                    if (whichVisualizationType == "barChart") {

                        visualize.getBarChartData(en, to, tabPane, tablesList, barChart, newSeries);

                        System.out.println("true");

                    } else if (whichVisualizationType == "pieChart") {

                        visualize.getPieChartData(en, to, tabPane, tablesList, pieChart, label, newSeries);
                    } else if (whichVisualizationType == "lineChart") {

                        visualize.getLineChartData(en, to, tabPane, tablesList, lineChart, newSeries);
                    } else if (whichVisualizationType == "areaChart") {

                        visualize.getAreaChartData(en, to, tabPane, tablesList, areaChart, newSeries);
                    } else if (whichVisualizationType == "scatterChart") {

                        visualize.getScatterChartData(en, to, tabPane, tablesList, scatterChart, newSeries);
                    }

                } catch (Exception e) {
                    Dialogs.create()
                            .owner(stage)
                            .title("Invalid columns detected")
                            .message("The first column has to be a text column, and the second one has to contain numbers")
                            .showError();
                }

            }

        }
        );

    }

    @FXML
    private void btnExportChartToPNG(ActionEvent event) throws IOException {
        if (barChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = barChart.snapshot(new SnapshotParameters(), null);

            chartToPNG.saveChartToPNG(image);

        }
        if (pieChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = pieChart.snapshot(new SnapshotParameters(), null);

            chartToPNG.saveChartToPNG(image);

        }

        if (lineChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);

            chartToPNG.saveChartToPNG(image);

        }

    }

    @FXML
    private void barChartButton(ActionEvent event) {
        lineChart.setVisible(false);
        pieChart.setVisible(false);
        barChart.setVisible(true);
        areaChart.setVisible(false);
        scatterChart.setVisible(false);

    }

    @FXML
    private void pieChartButton(ActionEvent event) {

        lineChart.setVisible(false);
        pieChart.setVisible(true);
        barChart.setVisible(false);
        areaChart.setVisible(false);
        scatterChart.setVisible(false);

    }

    @FXML
    private void areaChartButton(ActionEvent event) {

        lineChart.setVisible(false);
        pieChart.setVisible(false);
        barChart.setVisible(false);
        areaChart.setVisible(true);
        scatterChart.setVisible(false);

    }

    @FXML
    private void lineChartButton(ActionEvent event) {
        lineChart.setVisible(true);
        pieChart.setVisible(false);
        barChart.setVisible(false);
        areaChart.setVisible(false);
        scatterChart.setVisible(false);

    }

    @FXML
    private void scatterChartButton(ActionEvent event) {
        lineChart.setVisible(false);
        pieChart.setVisible(false);
        barChart.setVisible(false);
        areaChart.setVisible(false);
        scatterChart.setVisible(true);

    }

    @FXML
    private void btnNextUserInterFaceIntro(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            root = fxmlLoader.load();
            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOpacity(1);
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root));

        } catch (IOException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //TEMPORARY, FOR Å SLIPPE Å SKRIVE INN TILKOBLING HVER GANG
        try {
            sql_manager.getConnection("eskil-server-pc", "8889", "advaniatestdata");

        } catch (SQLException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //TEMPORARY, FOR Å SLIPPE Å SKRIVE INN TILKOBLING HVER GANG
        btnNewSeries.setVisible(true);
        btnNewChart.setVisible(true);

        SideBar sidebar = new SideBar(btnMenu, 90, vBoxMenu);

        borderPane.setLeft(sidebar);

        btnConnectedTables.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnCombine.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnVisualize.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        Image noTables = new Image(
                getClass().getResourceAsStream("/Icons/no_tables.png"));
        imageView.setImage(noTables);
        imageView.visibleProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));

    }

    public void createTabPaneWithTable(String whichTable) throws SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Hver gang brukeren kobler til en ny tabell, lager vi en ny tabpane
        //dette for å kunne organisere tabeller og vite hvilken rekkefølge de er i
        VBox vBox = new VBox();

        Table tabellen = new Table(whichTable + "@" + sql_manager.instanceName);

        //  String query = textField.getText();
        //her skjer oppkoblingen
        //laster inn dataen med en query
        tabellen.loadData("select * from " + whichTable + "  ", tabellen, tabPaneCounter);

        //legger til den nye tilkoblede tabellen i listen over tilkoblede tabeller
        tablesList.add(tabPaneCounter, tabellen);

        TableView tableViewet = new TableView();

        Label lbl = new Label("Number of rows : " + tabellen.numberofRows);
        AnchorPane anchorPane = new AnchorPane(lbl);
        anchorPane.setRightAnchor(lbl, 5.0);
        //legger til tableviewet i tabben
        vBox.getChildren().addAll(tableViewet, anchorPane);
        tableViewet = tabellen.fillTableView(tableViewet, tabellen);

        vBox.setId("" + tabPaneCounter);

        Tab tab = new Tab();

        tab.setText(whichTable + "@" + sql_manager.instanceName);

        tabPane.getTabs().add(tab);

        tab.setContent(vBox);
        tab.setId("" + tabPaneCounter);
        System.out.println(tabPaneCounter);

        tabPaneCounter++;

        tabPane.getSelectionModel().select(tabPaneCounter);

        for (Table tbl : tablesList) {
            System.out.println(tbl);
        }
    }

    private void openNew() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CombineColumns.fxml"));
        Parent root = fxmlLoader.load();
        CombineColumnsController c = (CombineColumnsController) fxmlLoader.getController();
        Stage stage = new Stage();
        ((CombineColumnsController) fxmlLoader.getController()).setContext(tablesList);
        c.myTabPane = tabPane;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setScene(new Scene(root));
        stage.show();

    }

}
