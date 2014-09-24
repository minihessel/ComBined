/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.Optional;
import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
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

    private void addDragAndDrop(TreeCell<String> treeCell,MainFXMLController mainFXMLController) {
        //denne metoden legger til mulighet for drag and drop på treeviews.
        //ved å legge til drag and drop kan brukeren dra kolonner for å lage kombinerte kolonner. 
        treeCell.setOnMouseClicked(new EventHandler<MouseEvent>() {

            int clickCount = 0;

            @Override
            public void handle(MouseEvent event) {

                if (clickCount >= 2 && treeCell.getTreeItem().valueProperty().get() == "ColumnTrue") {
                    Optional<String> response = Dialogs.create()
                            .title("Text Input Dialog")
                            .masthead("Look, a Text Input Dialog")
                            .message("Please enter your name:")
                            .showTextInput("walter");

                    treeCell.setText("asd");

// The Java 8 way to get the response value (with lambda expression).
                    response.ifPresent(name
                            -> treeCell.getTreeItem().setValue(name));
                }
                clickCount++;
            }
        });

        treeCell.setOnDragDetected(new EventHandler<MouseEvent>() {
            //brukeren har tatt tak i et treitem og drar det
            @Override
            public void handle(MouseEvent event) {
                System.out.println("setOnDragDetected");

                Dragboard db = treeCell.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();

                content.putString(event.toString());
                db.setContent(content);
                //Først setter vi hvilket item brukeren har tatt tak i
                DRAGGEDSOURCE = treeCell;

                //og hvilken index det har
                DRAGGEDINDEX = (treeCell.getTreeView().getSelectionModel().getSelectedIndex());

                event.consume();

            }
        });

        treeCell.setOnDragOver(new EventHandler<DragEvent>() {
            //brukeren har dragget det over et element
            public void handle(DragEvent event) {

                DRAGGEDTARGET = treeCell.getTreeItem();

                if (event.getGestureSource() != treeCell
                        && event.getDragboard().hasString()) {

                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                }

                event.consume();
            }

        });

        treeCell.setOnDragDropped(new EventHandler<DragEvent>() {
            

            public void handle(DragEvent event) {
                for (Table tbl : mainFXMLController.tablesList) {
                    System.out.println(tbl);
                }
                //brukeren har sluppet elementet og vi må sjekke at elementet brukeren drar over faktisk er et treitem
                //dette for å sørge for at brukeren ikke kan slippe tre itemet hvor som helst
                if (DRAGGEDSOURCE != null && DRAGGEDTARGET != null) {
                    if (DRAGGEDTARGET.getGraphic() != null) {
                        System.out.println("hva med her");

                        DRAGGEDTARGET.getChildren().add(DRAGGEDSOURCE.getTreeItem());

                        int hvilkenTabell = Integer.parseInt(DRAGGEDSOURCE.getTreeView().getUserData().toString());

                        System.out.println(hvilkenTabell);
                        //kaller på den kombinerte kolonnen ved å bruke map og sende inn treitemet
                        //deretter sier vi at vi skal legge til denne kolonnen i den kombinerte kolonnen
                        mainFXMLController.mapOverKolonnerOgTreItems.get(treeCell.getTreeItem()).
                                add(
                                        mainFXMLController.getTablesList().get(hvilkenTabell).listofColumns.get(DRAGGEDINDEX)
                                );
                        //     mapOverKolonnerOgTreItems.get(treeCell.getTreeItem()).add(tablesList.get(hvilkenTabell).listofColumns.get(DRAGGEDINDEX));
                    } 
                    else {
                        Dialogs.create()
                                .title("Information Dialog")
                                .masthead(null)
                                .message("This is not a combined column head")
                                .showInformation();

                        treeCell.setText("asd");
                    }
                } 
                else {
                    Dialogs.create()
                            .title("Information Dialog")
                            .masthead(null)
                            .message("You got to drag the column on to a combined column head")
                            .showInformation();

                }

                boolean success = false;

                /* data dropped */
                event.setDropCompleted(success);

                event.consume();

            }

        });
    }

    public void makeTreeViewDragAble(TreeView treeView,MainFXMLController mainFXMLController) {

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

                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };

                addDragAndDrop(treeCell,mainFXMLController);
                treeView.setEditable(true);
                return treeCell;
            }
        });
    }

}
