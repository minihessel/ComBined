package Controller;

import Model.DataInsight;
import static Model.ErrorDialog.ErrorDialog;
import View.Visualize;
import Model.Kolonne;
import Model.Table;
import View.ChartToPng;
import View.SideBar;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.Wizard.LinearFlow;
import org.controlsfx.validation.Validator;

/**
 *
 * @author Eskil Hesselroth
 */
public class MainFXMLController implements Initializable {
    
    Visualize visualize;
    HelpScreenController helpScreenController;
    DataInsight datainsight = new DataInsight();
    ArrayList<Table> tablesList;
    SQL_manager sql_manager = new SQL_manager();
    Parent root;
    List<TableView> listOfTableViews = new ArrayList();
    Map<Tab, TableView> mapOverTabAndTableView = new HashMap<>();
    Map<Tab, Table> mapOverTabAndTable = new HashMap<>();
    static String whichSelectedView;
    public int tabPaneCounter = 0;
    public String whichHelpView;
    Dialog dlg;
    static Stage helpScreenStage;
    List<Kolonne> listOfCombinedColumns = new ArrayList<Kolonne>();
    List<String> listOfColumnNames = new ArrayList<String>();
    TreeItem<String> kombinerteKolonnerRoot = new TreeItem<String>("List of combined columns");
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/helpme.fxml"));
    ChoiceBox choiceBoxTables = new ChoiceBox();
    String whichVisualizationType;
    
    public MainFXMLController() {
        visualize = new Visualize();
        tablesList = new ArrayList<>();
    }

    //////////FXML elements/////////
    //Charts fra FXML view filen
    @FXML
    private LineChart lineChart;
    @FXML
    private BarChart barChart;
    @FXML
    private StackedBarChart stackedBarChart;
    @FXML
    StackedAreaChart areaChart;
    @FXML
    ScatterChart scatterChart;
    @FXML
    PieChart pieChart;
    @FXML
    BubbleChart bubbleChart;
    //////////////////////////////

    //Knapper fra FXML view filen
    @FXML
    private Button btnNewSeries;
    @FXML
    private Button btnInsight;
    @FXML
    private Button btnHelp;
    @FXML
    private Button btnNewChart;
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
    @FXML
    Button btnMenu;
    //////////////////////////////

    //Panes fra FXML view filen
    @FXML
    AnchorPane anchorPaneTables;
    @FXML
    AnchorPane anchorPaneVisualize;
    @FXML
    AnchorPane anchorPaneInsight;
    @FXML
    AnchorPane anchorPaneCombine;
    @FXML
    SplitPane splitPane;
    @FXML
    TabPane tabPane;
    @FXML
    TabPane tabPaneInsight;
    @FXML
    BorderPane borderPane;
    //////////////////////////////

    @FXML
    private Label label;
    @FXML
    Label visualizeLabel;
    final Tooltip tooltip = new Tooltip();
    @FXML
    Separator separator;
    @FXML
    ImageView imageView;
    @FXML
    ComboBox<Column> comboBox;
    @FXML
    VBox vBoxMenu;
    
    @FXML
    private void removeFiltersButton(ActionEvent event) {
        if (anchorPaneTables.visibleProperty().get()) {
            if (tabPane.getSelectionModel().getSelectedItem() != null) {
                mapOverTabAndTable.get(tabPane.getSelectionModel().getSelectedItem()).removeFilters();
            }
        } else if (anchorPaneInsight.visibleProperty().get()) {
            if (!tabPaneInsight.getSelectionModel().isEmpty()) {
                mapOverTabAndTable.get(tabPaneInsight.getSelectionModel().getSelectedItem()).removeFilters();
            }
        }
        
    }
    
    @FXML
    private void showThat(ActionEvent event) {
        
    }
    
    @FXML
    private void barChartButton(ActionEvent event) {
        showHideCharts(false, false, true, false, false, false, "Bar Chart");
        whichVisualizationType = "barChart";
        
    }
    
    @FXML
    private void stackedBarChartButton(ActionEvent event) {
        showHideCharts(false, false, false, false, false, true, "Stacked Bar Chart");
        whichVisualizationType = "stackedBarChart";
        
    }
    
    @FXML
    private void pieChartButton(ActionEvent event) {
        showHideCharts(false, true, false, false, false, false, "Pie Chart");
        whichVisualizationType = "pieChart";
        
    }
    
    @FXML
    private void areaChartButton(ActionEvent event) {
        showHideCharts(false, false, false, true, false, false, "Area Chart");
        whichVisualizationType = "areaChart";
        
    }
    
    @FXML
    private void lineChartButton(ActionEvent event) {
        showHideCharts(true, false, false, false, false, false, "Line Chart");
        whichVisualizationType = "lineChart";
    }
    
    @FXML
    private void scatterChartButton(ActionEvent event) {
        showHideCharts(false, false, false, false, true, false, "Scatter Chart");
        whichVisualizationType = "scatterChart";
        
    }
    
    @FXML
    private void btnNewChart(ActionEvent event
    ) {
        showVisualizationWizard(Boolean.FALSE);
    }
    
    @FXML
    private void btnNewSeries(ActionEvent event) {
        showVisualizationWizard(Boolean.TRUE);
    }
    
    @FXML
    private void combineButton(ActionEvent event
    ) throws IOException {
        openCombineColumnWizard();
        
    }
    
    @FXML
    public void goToColumn(ActionEvent event) {
        
        if (!comboBox.getSelectionModel().isEmpty()) {
            Column selectedColumn = comboBox.getSelectionModel().getSelectedItem();
            
            FadeTransition ft = new FadeTransition(Duration.millis(300), selectedColumn.getGraphic());
            ft.setFromValue(10.0);
            ft.setToValue(0.0);
            ft.setCycleCount(6);
            ft.setAutoReverse(true);
            
            ft.play();

            // 
            mapOverTabAndTableView.get(tabPane.getSelectionModel().getSelectedItem()).scrollToColumn(selectedColumn);
            
        }
    }
    
    @FXML
    private void newConnectionButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {

        //  openDialogWithSQLConnectionInfo();
        Button button = (Button) event.getSource();
        MyWizard wizard = new MyWizard(new SQlConnectPage1(), new SQlConnectPage11());
          wizard.blurMainWindow(button.getScene().getRoot());
        Boolean finished = wizard.createDialog(button.getScene().getWindow());
      
        if (finished) {
            setVisibleView("tableView");
        }

        // createTabPaneWithTable("transbig");
    }
    
    @FXML
    private void visualizeButton(ActionEvent event) {
        setVisibleView("visualizeView");
        whichHelpView = "visualizeView";
        
    }
    
    @FXML
    private void insightButton(ActionEvent event) throws IOException, UnsupportedEncodingException, FileNotFoundException, ClassNotFoundException, SQLException, InterruptedException, ExecutionException {
        
        setVisibleView("insightView");
        whichHelpView = "insightView";
        
    }
    
    @FXML
    private void handleDataButton(ActionEvent event
    ) {
        setVisibleView("tableView");
        whichHelpView = "tableView";
    }
    
    @FXML
    private void runNewAnalysisButton(ActionEvent event) throws IOException, UnsupportedEncodingException, FileNotFoundException, ClassNotFoundException, SQLException, InterruptedException, ExecutionException {
        
        showInsightWizard();
        
    }
    
    @FXML
    private void btnHelp(ActionEvent event) throws IOException {
        
        loadHelpScreen();
        
    }
    
    @FXML
    private void btnExportChartToPNG(ActionEvent event) throws IOException {
        if (barChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = barChart.snapshot(new SnapshotParameters(), null);
            
            chartToPNG.saveChartToPNG(image, helpScreenStage);
            
        }
        if (pieChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = pieChart.snapshot(new SnapshotParameters(), null);
            
            chartToPNG.saveChartToPNG(image, helpScreenStage);
            
        }
        
        if (lineChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
            
            chartToPNG.saveChartToPNG(image, helpScreenStage);
            
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SelectKeyComboBoxListener(comboBox);
        
        try {
            root = fxmlLoader.load();
            helpScreenStage = new Stage();
            helpScreenStage.initStyle(StageStyle.UNDECORATED);
            helpScreenStage.initModality(Modality.APPLICATION_MODAL);
            helpScreenStage.setOpacity(1);
            helpScreenStage.setTitle("My New Stage Title");
            helpScreenStage.setScene(new Scene(root));
            
        } catch (IOException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //TEMPORARY, FOR Å SLIPPE Å SKRIVE INN TILKOBLING HVER GANG
        try {
            
            sql_manager.getConnection("localhost", "8889", "test");
            //      createTabPaneWithTable("transactions");
            //   createTabPaneWithTable("G_salesline3");

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
        SideBar sidebar = new SideBar(btnMenu, 90, vBoxMenu);
        
        borderPane.setLeft(sidebar);
        
        btnConnectedTables.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnCombine.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnVisualize.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        
        btnInsight.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnHelp.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        Image noTables = new Image(
                getClass().getResourceAsStream("/Icons/no_tables.png"));
        imageView.setImage(noTables);
        imageView.visibleProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        
    }
    
    public void setVisibleView(String whichView) {
        
        if ("visualizeView".equals(whichView)) {
            
            anchorPaneVisualize.setVisible(true);
            anchorPaneTables.setVisible(false);
            anchorPaneInsight.setVisible(false);
            
        }
        
        if ("tableView".equals(whichView)) {
            whichHelpView = "tableView";
            whichSelectedView = whichView;
            anchorPaneVisualize.setVisible(false);
            anchorPaneTables.setVisible(true);
            anchorPaneInsight.setVisible(false);
            
        }
        
        if ("insightView".equals(whichView)) {
            whichSelectedView = whichView;
            anchorPaneVisualize.setVisible(false);
            anchorPaneTables.setVisible(false);
            anchorPaneInsight.setVisible(true);
            
        }
        
    }
    
    void loadHelpScreen() throws IOException {
        
        helpScreenController = fxmlLoader.getController();
        System.out.println(whichHelpView);
        helpScreenController.setValue(whichHelpView);
        System.out.println(whichHelpView);
        helpScreenStage.showAndWait();
        
    }
    
    public Table getSelectedTable() {
        if (whichSelectedView.equals("insightView")) {
            Table table = mapOverTabAndTable.get(tabPaneInsight.getSelectionModel().getSelectedItem());
            return table;
        }
        if (whichSelectedView.equals("tableView")) {
            Table table = mapOverTabAndTable.get(tabPane.getSelectionModel().getSelectedItem());
            System.out.println(table.listofColumns.size());
            return table;
        } else {
            return null;
        }
        
    }
    
    private void showVisualizationWizard(Boolean newSeries) {
        
        if (getSelectedTable() != null) {
            
            Table table = getSelectedTable();
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
    }
    
    private void showInsightWizard() {
        
        Table table = mapOverTabAndTable.get(tabPane.getSelectionModel().getSelectedItem());
        
        ComboBox choiceBoxTransaction = new ComboBox();
        new SelectKeyComboBoxListener(choiceBoxTransaction);
        
        for (Kolonne kolonne : table.listofColumns) {
            choiceBoxTransaction.getItems().add(kolonne.NAVN);
        }
        
        ComboBox choiceBoxItem = new ComboBox();
        new SelectKeyComboBoxListener(choiceBoxItem);
        
        for (Kolonne kolonne : table.listofColumns) {
            choiceBoxItem.getItems().add(kolonne.NAVN);
            
        }
        
        Wizard wizard = new Wizard();
        
        wizard.setTitle("Connection Wizard");

        // --- page 1
        int row = 0;
        
        GridPane page1Grid = new GridPane();
        page1Grid.setVgap(10);
        page1Grid.setHgap(30);
        
        page1Grid.add(new Label("Check which field that represent the transactionID: "), 0, row);
        
        wizard.getValidationSupport().registerValidator(choiceBoxTransaction, Validator.createEmptyValidator("You must select what represents transactionID"));
        page1Grid.add(choiceBoxTransaction, 1, row++);
        
        page1Grid.add(new Label("Check which field that represent the itemID: "), 0, row);
        
        wizard.getValidationSupport().registerValidator(choiceBoxItem, Validator.createEmptyValidator("You must select what represents itemID"));
        page1Grid.add(choiceBoxItem, 1, row++);
        
        Wizard.WizardPane page1 = new Wizard.WizardPane();
        page1.setHeaderText("Please select your columns for visualizing");
        page1.setContent(page1Grid);
        
        wizard.setFlow(new LinearFlow(page1));

        // show wizard and wait for response
        wizard.showAndWait().ifPresent(result -> {
            if (result == ButtonType.FINISH) {
                int en = choiceBoxTransaction.getSelectionModel().getSelectedIndex();
                int to = choiceBoxItem.getSelectionModel().getSelectedIndex();
                
                try {
                    
                    Table tabellen = datainsight.getInsight(0, 1, tabPane, mapOverTabAndTable, en, to);
                    createTabPaneWithDataInsight(tabellen);
                    
                } catch (Exception e) {
                    ErrorDialog("Invalid columns detected", "The first column has to be a text column, and the second one has to contain numbers");
                    System.out.println(e);
                    
                }
                
            }
            
        }
        );
        
    }
    
    private void createChart(String whichVisualizationType, int en, int to, Tab tab, Boolean newSeries, Boolean rowCount) {
        if ("barChart".equals(whichVisualizationType)) {
            
            visualize.getBarChartData(en, to, tab, mapOverTabAndTable, barChart, newSeries, rowCount);
            
        } else if ("pieChart".equals(whichVisualizationType)) {
            
            visualize.getPieChartData(en, to, tab, mapOverTabAndTable, pieChart, label, newSeries, rowCount);
        } else if ("lineChart".equals(whichVisualizationType)) {
            
            visualize.getLineChartData(en, to, tab, mapOverTabAndTable, lineChart, newSeries, rowCount);
        } else if ("areaChart".equals(whichVisualizationType)) {
            
            visualize.getAreaChartData(en, to, tab, mapOverTabAndTable, areaChart, newSeries, rowCount);
        } else if ("scatterChart".equals(whichVisualizationType)) {
            
            visualize.getScatterChartData(en, to, tab, mapOverTabAndTable, scatterChart, newSeries, rowCount);
        } else if ("stackedBarChart".equals(whichVisualizationType)) {
            
            visualize.getStackedBarChartData(en, to, tab, mapOverTabAndTable, stackedBarChart, newSeries, rowCount);
        }
    }
    
    public void createTabPaneWithDataInsight(Table tabellen) throws UnsupportedEncodingException, IOException, FileNotFoundException, ClassNotFoundException, SQLException, InterruptedException, ExecutionException {
        VBox vBox = new VBox();
        TableView tableViewet = new TableView();
        
        Label lbl = new Label(datainsight.getStats());
        Label lbl2 = new Label("Stats:");
        lbl2.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        SplitPane split_pane = new SplitPane();
        FlowPane anchorPane = new FlowPane(lbl2, lbl);
        split_pane.setDividerPositions(9.0);
        anchorPane.setOrientation(Orientation.VERTICAL);
        split_pane.getItems().addAll(tableViewet, anchorPane);
        vBox.getChildren()
                .addAll(split_pane);
        tableViewet = tabellen.fillTableView(tableViewet, tabellen);
        vBox.setId(
                "" + tabPaneCounter);
        Tab tab = new Tab("Data Insight based on - " + (tabPane.getSelectionModel().getSelectedItem().getText())
        );
        tab.setContent(vBox);
        tabPaneInsight.getTabs()
                .add(tab);
        mapOverTabAndTable.put(tab, tabellen);
        tabPaneInsight.getSelectionModel()
                .select(tab);
        
    }
    
    public void createTabPaneWithTable(String whichTable) throws SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Hver gang brukeren kobler til en ny tabell, lager vi en ny tabpane
        //dette for å kunne organisere tabeller og vite hvilken rekkefølge de er i
        VBox vBox = new VBox();
        
        Table tabellen = new Table(whichTable + "@" + sql_manager.instanceName);

        //  String query = textField.getText();
        //her skjer oppkoblingen
        //laster inn dataen med en query
        tabellen.loadData("select * from " + whichTable + "  ", tabellen, tablesList.size() + 1);

        //legger til den nye tilkoblede tabellen i listen over tilkoblede tabeller
        tablesList.add(tabellen);
        
        TableView tableViewet = new TableView();
        listOfTableViews.add(tableViewet);
        
        Label lbl = new Label("Number of rows : " + tabellen.numberofRows);
        AnchorPane anchorPane = new AnchorPane(lbl);
        
        anchorPane.setRightAnchor(lbl,
                5.0);
        //legger til tableviewet i tabben
        vBox.getChildren()
                .addAll(tableViewet, anchorPane);
        tableViewet = tabellen.fillTableView(tableViewet, tabellen);
        
        vBox.setId(
                "" + tabPaneCounter);
        
        Tab tab = new Tab(whichTable
                + "@" + sql_manager.instanceName);
        
        tab.setOnClosed(new EventHandler<javafx.event.Event>() {
            @Override
            public void handle(javafx.event.Event e) {
                tablesList.remove(tabellen);
                tabPane.getTabs().remove(tab);
            }
        });
        
        tab.setContent(vBox);
        
        tabPane.getTabs()
                .add(tab);
        mapOverTabAndTableView.put(tab, tableViewet);
        mapOverTabAndTable.put(tab, tabellen);
        
        tab.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov, Boolean old_val, Boolean new_val) {
                
                if (new_val.equals(Boolean.TRUE)) {
                    
                    comboBox.getItems().clear();
                    comboBox.getItems().addAll(mapOverTabAndTableView.get(tabPane.getSelectionModel().getSelectedItem()).getColumns());
                    comboBox.setValue(null);
                    System.out.println("true");
                    
                }
                
            }
        });
        
        tabPane.getSelectionModel()
                .select(tab);
        comboBox.getItems().addAll(mapOverTabAndTableView.get(tabPane.getSelectionModel().getSelectedItem()).getColumns());
        
    }
    
    void showHideCharts(Boolean lineChartVisibility, Boolean pieChartVisibility, Boolean barChartVisiblity, Boolean areaChartVisibility, Boolean scatterChartVisibility, Boolean stackedBarChartVisibility, String visualizeLabelText) {
        visualizeLabel.setText(visualizeLabelText);
        lineChart.setVisible(lineChartVisibility);
        pieChart.setVisible(pieChartVisibility);
        barChart.setVisible(barChartVisiblity);
        areaChart.setVisible(areaChartVisibility);
        scatterChart.setVisible(scatterChartVisibility);
        stackedBarChart.setVisible(stackedBarChartVisibility);
        
    }
    
    private void openCombineColumnWizard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/View/CombineColumns.fxml"));
        Parent root = fxmlLoader.load();
        CombineColumnsController c = (CombineColumnsController) fxmlLoader.getController();
        Stage stagen = new Stage();
        ((CombineColumnsController) fxmlLoader.getController()).setContext(tablesList, mapOverTabAndTableView, mapOverTabAndTable);
        c.myTabPane = tabPane;
        stagen.setOpacity(1);
        stagen.setScene(new Scene(root));
        stagen.show();
        
    }
    
}
