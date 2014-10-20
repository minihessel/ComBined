/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Eskil Hesselroth
 */
public class DragAbleTreeView {

    private static TreeCell DRAGGEDSOURCE;
    private static TreeItem DRAGGEDTARGET;
    private static int DRAGGEDINDEX;

    Map<TreeCell, TreeCell> source_and_target_treeitem = new HashMap<TreeCell, TreeCell>();

    private void addDragAndDrop(TreeCell<String> treeCell, MainFXMLController mainFXMLController) {
        //denne metoden legger til mulighet for drag and drop på treeviews.
        //ved å legge til drag and drop kan brukeren dra kolonner for å lage kombinerte kolonner. 

        treeCell.setOnDragDetected(new EventHandler<MouseEvent>() {
            //brukeren har tatt tak i et treitem og drar det
            @Override
            public void handle(MouseEvent event) {
                System.out.println("setOnDragDetected");

                if (treeCell.getTreeItem() != null) {
                    Dragboard db = treeCell.startDragAndDrop(TransferMode.ANY);

                    ClipboardContent content = new ClipboardContent();
                    content.putString(event.toString());
                    db.setContent(content);
                    //Først setter vi hvilket item brukeren har tatt tak i
                    DRAGGEDSOURCE = treeCell;

                    //og hvilken index det har
                    DRAGGEDINDEX = (treeCell.getTreeView().getSelectionModel().getSelectedIndex());

                }

            }
        });

        treeCell.setOnDragOver(new EventHandler<DragEvent>() {
            //brukeren har dragget det over et element
            public void handle(DragEvent event) {

                DRAGGEDTARGET = treeCell.getTreeItem();

                if (event.getGestureSource() != treeCell
                        && event.getDragboard().hasString() && DRAGGEDTARGET != null) {

                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                }

            }

        });

        treeCell.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                //brukeren har sluppet elementet og vi må sjekke at elementet brukeren drar over faktisk er et treitem
                //dette for å sørge for at brukeren ikke kan slippe tre itemet hvor som helst
                if (DRAGGEDSOURCE != null && DRAGGEDTARGET != null) {
                    TreeView treeView = treeCell.getTreeView();
                    if (treeView.getUserData() == "combinedColumnTree") {
                        System.out.println("hva med her");

                        DRAGGEDTARGET.getChildren().add(DRAGGEDSOURCE.getTreeItem());

                        int hvilkenTabell = Integer.parseInt(DRAGGEDSOURCE.getTreeView().getUserData().toString());
                        //kaller på den kombinerte kolonnen ved å bruke map og sende inn treitemet
                        //deretter sier vi at vi skal legge til denne kolonnen i den kombinerte kolonnen
                        mainFXMLController.addColumnToCombinedColumn(treeCell, hvilkenTabell, DRAGGEDINDEX);

                        /* data dropped */
                        DRAGGEDSOURCE.setGraphic(new ImageView(new Image(
                                getClass().getResourceAsStream("/Icons/check.png"))));
                    
                                System.out.println(treeCell);

                    } else {
                        Dialogs.create()
                                .title("Information Dialog")
                                .masthead(null)
                                .message("This is not a combined column head")
                                .showInformation();

                    }
                } else {
                    Dialogs.create()
                            .title("Information Dialog")
                            .masthead(null)
                            .message("You got to drag the column on to a combined column head")
                            .showInformation();

                }

                boolean success = false;

            }

        });

    }

    public void makeTreeViewDragAble(TreeView treeView, MainFXMLController mainFXMLController) {

        treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override

            public TreeCell<String> call(TreeView<String> stringTreeView) {
                TreeCell<String> treeCell = new TreeCell<String>() {
                    @Override

                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            setText(item);

                            setGraphic(getTreeItem().getGraphic());
                            if (treeView.getUserData() == "combinedColumnTree") {

                                addContextMenu(this, mainFXMLController);
                            }

                        } else {
                            setText(null);
                            setGraphic(null);

                        }
                    }

                };

                addDragAndDrop(treeCell, mainFXMLController);
                treeView.setEditable(true);
                return treeCell;
            }

        });

    }

    private void addContextMenu(TreeCell treeCell, MainFXMLController mainFXMLController) {

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem item2 = new MenuItem("Delete");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                System.out.println("qq + " + source_and_target_treeitem.get(treeCell));
               treeCell.getTreeItem()
                        .setGraphic(new ImageView(new Image(
                                                getClass().getResourceAsStream("/Icons/column_icon.png"))));
                mainFXMLController.removeColumnToCombinedColumn(treeCell);
                treeCell.getTreeItem().getParent().getChildren().remove(treeCell.getTreeItem());

            }
        });
        contextMenu.getItems().addAll(item2);

        treeCell.setContextMenu(contextMenu);

    }

}
