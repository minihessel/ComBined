/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Eskil Hesselroth
 */
public class MainFXMLController implements Initializable {
    
    private ArrayList<TreeView> listemedView = new ArrayList<TreeView>();

    

    Map<TreeItem, List> mapOverKolonnerOgTreItems = new HashMap<TreeItem, List>();
    
    public int tabPaneCounter = 0;
    @FXML
    private Label label;
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
    HBox hBoxWithTreeViews;
    
    boolean wizardIsOpen = false;
    WizardController wizard = new WizardController();
    
     TreeItem<String> kombinerteKolonnerRoot = new TreeItem<String>("List of combined columns");
    
    @FXML
    private void visualizeButton(ActionEvent event) {
        setVisibleView("visualizeView");
        
    }
    
    @FXML
    private void handleDataButton(ActionEvent event) {
        setVisibleView("tableView");
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
    private void newConnectionButton(ActionEvent event) throws IOException {
        setVisibleView("tableView");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/WizardFXML.fxml"));
        WizardController controller = new WizardController();
        loader.setController(controller);
        Parent root = (Parent) loader.load();
        WizardController c = (WizardController) loader.getController();
        c.openStage(root);
        c.tabPane = tabPane;
        c.hBoxWithTreeViews = hBoxWithTreeViews;
        wizardIsOpen = true;
        
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
    private void createNewCombinedColumnButton(ActionEvent event) {
        createNewCombinedColumn();
    }
    
     private void createNewCombinedColumn() {
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
        List<Kolonne> combinedColumn = new ArrayList<Kolonne>();
        //vi legger til den nye combinedColumn(den nye kombinerte kolonnen i combinedColumn over kombinerte kolonner
       // listOfCombinedColumns.add(combinedColumn);
      //  listOfColumnNames.add(treItem.getValue().toString());
        //deretter mapper vi den nye kombinerte kolonnen opp mot treitemet, sånn at vi senere kan hente ut den kombinerte kolonnen
      //  mapOverKolonnerOgTreItems.put(treItem, listOfCombinedColumns.get(listOfCombinedColumns.size() - 1));
        //Deretter putter vi kolonnen i treeviewet for kombinerte kolonner
       kombinerteKolonnerRoot.getChildren().add(treItem);
    }

    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           treeViewCombinedColumns.setRoot(kombinerteKolonnerRoot);
        treeViewCombinedColumns.setShowRoot(false);
        AddDragAndDropFunctionality addDragAndDropFunctionality = new AddDragAndDropFunctionality();
           addDragAndDropFunctionality.makeTreeViewDragAble(treeViewCombinedColumns);
    }
    
}
