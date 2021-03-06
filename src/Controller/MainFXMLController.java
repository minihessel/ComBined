package Controller;

import Model.CustomTab;
import View.Wizard.MyWizard;
import View.Wizard.VisualizationWizard.VisualizationPage1;
import View.Wizard.SQLConnectWizard.SQlConnectPage11;
import View.Wizard.SQLConnectWizard.SQlConnectPage1;
import Model.DataInsight.DataIntelligence;
import static Model.ErrorDialog.ErrorDialog;
import View.Visualize;
import Model.Kolonne;
import Model.MyTableColumn;
import Model.Table;
import View.ChartToPng;
import View.SideBar;
import View.Wizard.CombineColumnsWizard.CombineColumnsWizardPage1;
import View.Wizard.InsightWizard.InsightSummaryWizard.InsightSummaryWizardPage1;
import View.Wizard.InsightWizard.InsightWizardPage1;
import View.Wizard.SQLConnectWizard.SQlConnectPage3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.Node;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.dialog.Dialog;

/**
 *
 * @author Eskil Hesselroth
 */
public class MainFXMLController implements Initializable {

    Visualize visualize;
    HelpScreenController helpScreenController;
    ArrayList<Table> tablesList;
    SQL_manager sql_manager = new SQL_manager();
    Parent root;
    List<TableView> listOfTableViews = new ArrayList();

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
    @FXML
    Button pieChartButton;
    @FXML
    Button barChartButton;
    @FXML
    Button lineChartButton;
    @FXML
    Button stackedBarChartButton;
    @FXML
    Button scatterChartButton;
    @FXML
    Button areaChartButton;

    @FXML
    Button normalInsightViewButton;

    @FXML
    Button summaryInsightViewButton;

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
    TabPane tabPaneInsightNormal;

    @FXML
    TabPane tabPaneInsightSummary;
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
    ComboBox<MyTableColumn> comboBox;
    @FXML
    VBox vBoxMenu;

    public static CustomTab whichTabIsSelected;

    @FXML
    private void removeFiltersButton(ActionEvent event) {
        whichTabIsSelected.getTable().removeFilters();

    }

    @FXML
    private void normalInsightViewButton(ActionEvent event) {

        tabPaneInsightNormal.setVisible(true);
        tabPaneInsightSummary.setVisible(false);

    }

    @FXML
    private void summaryInsightViewButton(ActionEvent event) {
        tabPaneInsightNormal.setVisible(false);
        tabPaneInsightSummary.setVisible(true);

    }

    @FXML
    private void exportTableToCSVButton(ActionEvent event) throws IOException {
        Table selectedTable = whichTabIsSelected.getTable();
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showSaveDialog(stage);
        Writer writer = null;
        if (file != null) {
            try {

                writer = new BufferedWriter(new FileWriter(file));
                int counter = 0;

                for (Kolonne kol : selectedTable.listofColumns) {
                    if (kol == selectedTable.listofColumns.get(selectedTable.listofColumns.size() - 1)) {
                        String text = kol.NAVN + "\n";
                        writer.write(text);
                    } else {
                        String text = kol.NAVN + ",";
                        writer.write(text);
                    }
                }

                for (List<String> strings : selectedTable.sortedData) {

                    for (int i = 0; i < strings.size(); i++) {
                        if (i + 1 == strings.size()) {
                            String text = strings.get(i);
                            writer.write(text);
                        } else {
                            String text = strings.get(i) + ",";
                            writer.write(text);
                        }

                    }
                    String text = "\n";
                    writer.write(text);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {

                writer.flush();
                writer.close();

            }

        }

    }

    @FXML
    private void showThat(ActionEvent event
    ) //THIS SHOULD BE REMOVED; ONLY FOR TESTING
    {

        try {
            try {
                setVisibleView("tableView");
                createTabPaneWithTable("select * from G_Items", "ITEMS");
                createTabPaneWithTable("select * from G_Salesline3", "G_salesline");

            } catch (SQLException ex) {
                Logger.getLogger(MainFXMLController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

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
    }

    @FXML
    private void barChartButton(ActionEvent event
    ) {
        showHideCharts(false, false, true, false, false, false, "Bar Chart");
        whichVisualizationType = "barChart";

    }

    @FXML
    private void stackedBarChartButton(ActionEvent event
    ) {
        showHideCharts(false, false, false, false, false, true, "Stacked Bar Chart");
        whichVisualizationType = "stackedBarChart";

    }

    @FXML
    private void pieChartButton(ActionEvent event
    ) {
        showHideCharts(false, true, false, false, false, false, "Pie Chart");
        whichVisualizationType = "pieChart";

    }

    @FXML
    private void areaChartButton(ActionEvent event
    ) {
        showHideCharts(false, false, false, true, false, false, "Area Chart");
        whichVisualizationType = "areaChart";

    }

    @FXML
    private void lineChartButton(ActionEvent event
    ) {
        showHideCharts(true, false, false, false, false, false, "Line Chart");
        whichVisualizationType = "lineChart";
    }

    @FXML
    private void scatterChartButton(ActionEvent event
    ) {
        showHideCharts(false, false, false, false, true, false, "Scatter Chart");
        whichVisualizationType = "scatterChart";

    }

    @FXML
    private void btnNewChart(ActionEvent event
    ) {
        showVisualizationWizard(Boolean.FALSE);
    }

    @FXML
    private void btnNewSeries(ActionEvent event
    ) {
        showVisualizationWizard(Boolean.TRUE);
    }

    @FXML
    private void combineButton(ActionEvent event
    ) throws IOException {
        openCombineColumnWizard();

    }

    @FXML
    public void goToColumn(ActionEvent event
    ) {

        if (!comboBox.getSelectionModel().isEmpty()) {
            MyTableColumn selectedColumn = comboBox.getSelectionModel().getSelectedItem();

            FadeTransition ft = new FadeTransition(Duration.millis(300), selectedColumn.getGraphic());
            ft.setFromValue(10.0);
            ft.setToValue(0.0);
            ft.setCycleCount(6);
            ft.setAutoReverse(true);

            ft.play();

            // 
            whichTabIsSelected.getTableView().scrollToColumn(selectedColumn);

        }
    }

    @FXML
    private void newConnectionButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {

        showNewSQLConnectionWizard(event);

    }

    @FXML
    private void visualizeButton(ActionEvent event
    ) {
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
    private void dataInsightCreateSummaryButton(ActionEvent event) {

        showInsightSummaryWizard();
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
    public void initialize(URL url, ResourceBundle rb
    ) {

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

            sql_manager.getConnection("localhost", "8889", "test", "root", "root", "MySQL");
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
        markRedWhenSelected(pieChartButton, pieChart);
        markRedWhenSelected(barChartButton, barChart);
        markRedWhenSelected(lineChartButton, lineChart);
        markRedWhenSelected(stackedBarChartButton, stackedBarChart);
        markRedWhenSelected(scatterChartButton, scatterChart);
        markRedWhenSelected(areaChartButton, areaChart);
        markRedWhenSelected(btnConnectedTables, anchorPaneTables);
        markRedWhenSelected(btnVisualize, anchorPaneVisualize);
        markRedWhenSelected(btnInsight, anchorPaneInsight);
        markRedWhenSelected(normalInsightViewButton, tabPaneInsightNormal);
        markRedWhenSelected(summaryInsightViewButton, tabPaneInsightSummary);

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

    private void showVisualizationWizard(Boolean newSeries) {

        if (whichTabIsSelected.getTable() != null) {

            VisualizationPage1 visualizationPage1 = new VisualizationPage1(whichTabIsSelected.getTable());
            //  openDialogWithSQLConnectionInfo();

            MyWizard wizard = new MyWizard(visualizationPage1);
            wizard.blurMainWindow(pieChart.getScene().getRoot());
            Boolean finished = wizard.createDialog(pieChart.getScene().getWindow(), 700, 500);

            if (finished) {

                int nameColumn = visualizationPage1.nameColumn.getSelectionModel().getSelectedIndex();
                int valueColumn = visualizationPage1.valueColumn.getSelectionModel().getSelectedIndex();
                Boolean countRowsOrNot = visualizationPage1.checkBox.selectedProperty().get();

                createChart(whichVisualizationType, nameColumn, valueColumn, whichTabIsSelected.getTable(), newSeries, countRowsOrNot);

            }

        }
    }

    private void showNewSQLConnectionWizard(ActionEvent event) throws InterruptedException, ExecutionException, ClassNotFoundException, SQLException {
        SQlConnectPage1 sQlConnectPage1 = new SQlConnectPage1();
        SQlConnectPage11 SQLConnectPage2 = new SQlConnectPage11();
        SQlConnectPage3 SQLConnectPage3 = new SQlConnectPage3();

        //  openDialogWithSQLConnectionInfo();
        Button button = (Button) event.getSource();
        MyWizard wizard = new MyWizard(sQlConnectPage1, SQLConnectPage2, SQLConnectPage3);
        wizard.blurMainWindow(button.getScene().getRoot());
        Boolean finished = wizard.createDialog(button.getScene().getWindow(), 700, 500);

        if (finished) {
            String tableSelected = SQLConnectPage2.comboBox.getSelectionModel().getSelectedItem().toString();
            QueryBuilder queryBuilder = new QueryBuilder(tableSelected, SQL_manager.md.getDatabaseProductName());
            List<String> selectedColumns = SQLConnectPage3.listView.getTargetItems();
            setVisibleView("tableView");

            queryBuilder.addColumnNames(selectedColumns);
            createTabPaneWithTable(queryBuilder.getQuery(), tableSelected);

        }
    }

    private void showInsightWizard() throws UnsupportedEncodingException, IOException, FileNotFoundException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        if (tabPane.getSelectionModel().getSelectedItem() != null) {

            InsightWizardPage1 insightWizardPage1 = new InsightWizardPage1(tablesList);

            //  openDialogWithSQLConnectionInfo();
            MyWizard wizard = new MyWizard(insightWizardPage1);
            wizard.blurMainWindow(pieChart.getScene().getRoot());
            Boolean finished = wizard.createDialog(pieChart.getScene().getWindow(), 700, 500);

            if (finished) {
                int transactionIDcolumn = insightWizardPage1.transactionIDcolumn.getSelectionModel().getSelectedIndex();
                int itemIDcolumn = insightWizardPage1.itemIDcolumn.getSelectionModel().getSelectedIndex();
                String whichTypeOfInsight = insightWizardPage1.whichTypeOfInsight.getSelectionModel().getSelectedItem().toString();
                DataIntelligence dataInsight;
                if (whichTypeOfInsight.equals("Rare item sets")) {
                    dataInsight = new DataIntelligence("rare");
                    dataInsight.getRareItemSets(insightWizardPage1.tableColumn.getSelectionModel().getSelectedItem(), transactionIDcolumn, itemIDcolumn);
                } else {
                    dataInsight = new DataIntelligence("frequent");
                    dataInsight.getFrequentItemSets(insightWizardPage1.tableColumn.getSelectionModel().getSelectedItem(), transactionIDcolumn, itemIDcolumn);
                }

                Table itemSetsTable = dataInsight.getItemSets(dataInsight.result);
                Table associationRulesTable = dataInsight.getAssociationRules();
                itemSetsTable.setDataInsight(dataInsight);
                associationRulesTable.setDataInsight(dataInsight);
                CustomTab itemSetsTab = createaTabWithDataInsight("Item Sets", itemSetsTable, whichTypeOfInsight, dataInsight.getItemSetsStats());
                CustomTab associationRulesTab = createaTabWithDataInsight("Association Rules", associationRulesTable, "Association Rules", dataInsight.getAssociationRulesStats());
                CustomTab combinedTab = new CustomTab(insightWizardPage1.tableColumn.getSelectionModel().getSelectedItem() + " analysis");
                TabPane combinedTabPane = new TabPane();
                combinedTabPane.getTabs().addAll(itemSetsTab, associationRulesTab);
                combinedTab.setContent(combinedTabPane);
                tabPaneInsightNormal.getTabs().add(combinedTab);
                tabPaneInsightNormal.setVisible(true);
                tabPaneInsightSummary.setVisible(false);
                tabPaneInsightNormal.getSelectionModel().select(combinedTab);

            }

        } else {
            System.out.println("qq");
        }

    }

    private void showInsightSummaryWizard() {
        Table dataInsightTable = whichTabIsSelected.getTable();
        InsightSummaryWizardPage1 insightPage1 = new InsightSummaryWizardPage1(tablesList);
        //  openDialogWithSQLConnectionInfo();
        if (whichTabIsSelected.getTable().getDataInsight() == null) {
            ErrorDialog("Run analysis first", "Please run an analysis before trying to create a summary");
        } else {

            MyWizard wizard = new MyWizard(insightPage1);
            wizard.blurMainWindow(pieChart.getScene().getRoot());
            Boolean finished = wizard.createDialog(pieChart.getScene().getWindow(), 700, 500);
            if (finished) {

                TabPane summaryTabPane = new TabPane();

                CustomTab summaryTab = new CustomTab("Summary for " + tabPaneInsightNormal.getSelectionModel().getSelectedItem().getText());
                tabPaneInsightSummary.getTabs().add(summaryTab);
                tabPaneInsightSummary.getSelectionModel().select(summaryTab);
                List<Table> tables = dataInsightTable.getDataInsight().createUnderstandableSummary(insightPage1.tableColumn.getSelectionModel().getSelectedItem(), insightPage1.itemIDColumn.getSelectionModel().getSelectedIndex(), insightPage1.itemDescriptionColumn.getSelectionModel().getSelectedIndex());

                for (Table table : tables) {

                    TableView tableView = new TableView();

                    CustomTab tab = new CustomTab(table, table.NAVN, tableView, tabPaneInsightSummary, tablesList);

                    tableView = table.fillTableView(tableView, table);
                    tableView.setMinHeight(760);
                    tab.setContent(tableView);
                    summaryTabPane.getTabs().add(tab);

                    summaryTab.setContent(summaryTabPane);
                    tabPaneInsightNormal.setVisible(false);
                    tabPaneInsightSummary.setVisible(true);

                }

            }
        }
    }

    private void createChart(String whichVisualizationType, int en, int to, Table table, Boolean newSeries, Boolean rowCount) {
        if ("barChart".equals(whichVisualizationType)) {

            visualize.getBarChartData(en, to, table, barChart, newSeries, rowCount);

        } else if ("pieChart".equals(whichVisualizationType)) {

            visualize.getPieChartData(en, to, table, pieChart, label, newSeries, rowCount);
        } else if ("lineChart".equals(whichVisualizationType)) {

            visualize.getLineChartData(en, to, table, lineChart, newSeries, rowCount);
        } else if ("areaChart".equals(whichVisualizationType)) {

            visualize.getAreaChartData(en, to, table, areaChart, newSeries, rowCount);
        } else if ("scatterChart".equals(whichVisualizationType)) {

            visualize.getScatterChartData(en, to, table, scatterChart, newSeries, rowCount);
        } else if ("stackedBarChart".equals(whichVisualizationType)) {

            visualize.getStackedBarChartData(en, to, table, stackedBarChart, newSeries, rowCount);
        }
    }

    public CustomTab createaTabWithDataInsight(String tabName, Table tabellen, String whichInsightType, String stats) throws UnsupportedEncodingException, IOException, FileNotFoundException, ClassNotFoundException, SQLException, InterruptedException, ExecutionException {

        VBox vBox = new VBox();
        TableView tableViewet = new TableView();

        Label lbl = new Label(stats);
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
        CustomTab tab = new CustomTab(tabellen, tabName, tableViewet, tabPaneInsightNormal, tablesList);

        tab.setContent(vBox);
        tableViewet.setMinHeight(760);
        return tab;
    }

    public void createTabPaneWithTable(String sql, String tableName) throws SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Hver gang brukeren kobler til en ny tabell, lager vi en ny tabpane
        //dette for å kunne organisere tabeller og vite hvilken rekkefølge de er i
        VBox vBox = new VBox();

        Table tabellen = new Table(tableName + "@" + SQL_manager.instanceName);

        //  String query = textField.getText();
        //her skjer oppkoblingen
        //laster inn dataen med en query
        System.out.println(sql);
        tabellen.loadData(sql, tabellen, tablesList.size() + 1);

        //legger til den nye tilkoblede itemSetsTable i listen over tilkoblede tabeller
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

        CustomTab tab = new CustomTab(tabellen, (tableName
                + "@" + sql_manager.instanceName), tableViewet, anchorPaneTables, tablesList);

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

        tab.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov, Boolean old_val, Boolean new_val) {

                if (new_val.equals(Boolean.TRUE)) {

                    comboBox.getItems().clear();
                    comboBox.getItems().addAll(whichTabIsSelected.getTableView().getColumns());
                    comboBox.setValue(null);
                    System.out.println("true");

                }

            }
        });

        tabPane.getSelectionModel()
                .select(tab);

    }

    void showHideCharts(Boolean lineChartVisibility, Boolean pieChartVisibility, Boolean barChartVisiblity, Boolean areaChartVisibility, Boolean scatterChartVisibility, Boolean stackedBarChartVisibility, String visualizeLabelText) {
        btnNewChart.setVisible(true);
        btnNewSeries.setVisible(true);
        visualizeLabel.setText(visualizeLabelText);
        lineChart.setVisible(lineChartVisibility);
        pieChart.setVisible(pieChartVisibility);
        barChart.setVisible(barChartVisiblity);
        areaChart.setVisible(areaChartVisibility);
        scatterChart.setVisible(scatterChartVisibility);
        stackedBarChart.setVisible(stackedBarChartVisibility);

    }

    private void markRedWhenSelected(Button button, Node control) {
        control.visibleProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println(newValue);
                if (newValue) {
                    button.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.50), 10, 0.8, 0, 0);");
                } else {
                    button.setStyle("");

                }
            }
        });
    }

    private void openCombineColumnWizard() throws IOException {

        CombineColumnsWizardPage1 combineColumnsWizardPage1 = new CombineColumnsWizardPage1(tablesList);
        //  openDialogWithSQLConnectionInfo();

        MyWizard wizard = new MyWizard(combineColumnsWizardPage1);
        wizard.blurMainWindow(pieChart.getScene().getRoot());
        Boolean finished = wizard.createDialog(pieChart.getScene().getWindow(), 1200, 800);

        if (finished) {

            if (!combineColumnsWizardPage1.combinedTable.listofColumns.isEmpty()) {

                TableView tableViewCombined = new TableView();
                VBox tabPaneVBox = new VBox();
                tableViewCombined = combineColumnsWizardPage1.combinedTable.fillTableView(tableViewCombined, combineColumnsWizardPage1.combinedTable);
                CustomTab tab = new CustomTab(combineColumnsWizardPage1.combinedTable, "combined table", tableViewCombined, anchorPaneTables, tablesList);
                tab.setOnClosed(new EventHandler<javafx.event.Event>() {
                    @Override
                    public void handle(javafx.event.Event e) {
                        tablesList.remove(combineColumnsWizardPage1.combinedTable);
                        tabPane.getTabs().remove(tab);
                    }
                });
                combineColumnsWizardPage1.combinedTable.numberofRows = combineColumnsWizardPage1.combinedTable.listofColumns.get(0).allFields().size();
                Label lbl = new Label("Number of rows : " + combineColumnsWizardPage1.combinedTable.numberofRows);
                AnchorPane anchorPane = new AnchorPane(lbl);

                anchorPane.setRightAnchor(lbl,
                        5.0);
                //legger til tableviewet i tabben
                tabPaneVBox.getChildren()
                        .addAll(tableViewCombined, anchorPane);
                tab.setContent(tabPaneVBox);
                tablesList.add(combineColumnsWizardPage1.combinedTable);
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
                // myTabPane.getSelectionModel().selectLast();
                tab.setId("" + tabPane.getTabs().size() + 1);

            } else {
                ErrorDialog("No columns created", "There are no columns created, can not continue. Please create atleast one combined column");
            }

        }

        /*
         FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(getClass().getResource("/View/CombineColumns.fxml"));
         Parent thisRoot = fxmlLoader.load();
         CombineColumnsController c = (CombineColumnsController) fxmlLoader.getController();

         Stage stagen = new Stage();

         Effect previousEffect = pieChart.getScene().getRoot().getEffect();
         final BoxBlur blur = new BoxBlur(0, 0, 5);
         blur.setInput(previousEffect);
         pieChart.getScene().getRoot().setEffect(blur);
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),
         new KeyValue(blur.widthProperty(), 10),
         new KeyValue(blur.heightProperty(), 10)
         ));
         timeline.play();

         pieChart.getScene().getWindow().setOnHidden(t -> pieChart.getScene().getRoot().setEffect(previousEffect));

         ((CombineColumnsController) fxmlLoader.getController()).setContext(tablesList, anchorPaneTables);
         c.myTabPane = tabPane;

         stagen.setOpacity(1);
         stagen.setScene(new Scene(thisRoot));
         stagen.show();
         */
    }

}
