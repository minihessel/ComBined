/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard;

import View.Wizard.WizardPage;
import Controller.MainFXMLController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 *
 * @author Eskil Hesselroth
 */
public class MyWizard {

    Label header = new Label();
    WizardPage[] pages;
    Integer currentPage = 0;
    Stage stage = new Stage();
    MainFXMLController mainFXMLController = new MainFXMLController();
    Boolean finishedOrNot = false;

    public MyWizard(WizardPage... pages) {
        this.pages = pages;

    }

    public Boolean createDialog(Window owner) {

        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        Button nextButton = new Button("Next");
        Button previousButton = new Button("Previous");
        Button cancelButton = new Button("Cancel");
        previousButton.setDisable(true);

        FlowPane pagePane = new FlowPane();
        header.setText(pages[currentPage].getHeader());
        header.setStyle("-fx-font-size:20px;");
        header.setAlignment(Pos.CENTER);
        pagePane.setMaxSize(700, 500);
        FlowPane buttonPane = new FlowPane(nextButton, previousButton, cancelButton);
        FlowPane headerPane = new FlowPane(header);
        buttonPane.setAlignment(Pos.BOTTOM_RIGHT);
        //  buttonPane.setMinSize(333, 333);

        pagePane.getChildren().setAll(pages[currentPage].getPane());

        BorderPane dialogRoot = new BorderPane();
        dialogRoot.setBottom(buttonPane);
        dialogRoot.setCenter(pagePane);
        pagePane.setAlignment(Pos.CENTER);
        headerPane.setAlignment(Pos.CENTER);
        dialogRoot.setTop(headerPane);
        dialogRoot.setStyle("-fx-background-color: derive(lightgrey, 90%) ; -fx-border-color: black;"
                + "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-width: 3px;-fx-font-family:Menlo;");
        final Scene scene = new Scene(dialogRoot, 700, 500,
                Color.TRANSPARENT);
        enableDragging(scene);
        stage.setScene(scene);
        if (currentPage + 1 == pages.length) {
            nextButton.setText("Finish");
        }

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (pages[currentPage].valider()) {
                    if (currentPage + 1 == pages.length) {
                        stage.close();
                        finishedOrNot = true;
                    } else {
                        currentPage++;
                        pagePane.getChildren().setAll(pages[currentPage].getPane());
                        header.setText(pages[currentPage].getHeader());
                        previousButton.setDisable(false);
                        if (currentPage + 1 == pages.length) {
                            nextButton.setText("Finish");

                        }
                    }

                }
            }
        });

        previousButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    pagePane.getChildren().setAll(pages[currentPage].getPane());
                    nextButton.setText("Next");

                    if (currentPage == 0) {
                        previousButton.setDisable(true);
                    }

                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                stage.close();
            }
        });

        stage.showAndWait();
        return finishedOrNot;
    }

    void checkIfOnlyOnePage() {

    }

    private void enableDragging(Scene scene) {
        final ObjectProperty<Point2D> mouseLocation = new SimpleObjectProperty<>();
        scene.setOnMousePressed(event -> mouseLocation.set(new Point2D(event.getScreenX(), event.getScreenY())));
        scene.setOnMouseDragged(event -> {
            double mouseX = event.getScreenX();
            double mouseY = event.getScreenY();
            double deltaX = mouseX - mouseLocation.get().getX();
            double deltaY = mouseY - mouseLocation.get().getY();
            Window window = scene.getWindow();
            window.setX(window.getX() + deltaX);
            window.setY(window.getY() + deltaY);
            mouseLocation.set(new Point2D(mouseX, mouseY));
        });
    }

    public void blurMainWindow(Parent rootPane) {
        Effect previousEffect = rootPane.getEffect();
        final BoxBlur blur = new BoxBlur(0, 0, 5);
        blur.setInput(previousEffect);
        rootPane.setEffect(blur);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),
                new KeyValue(blur.widthProperty(), 10),
                new KeyValue(blur.heightProperty(), 10)
        ));
        timeline.play();

        stage.setOnHidden(t -> rootPane.setEffect(previousEffect));
    }

}
