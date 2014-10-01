/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.MainFXMLController.stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Eskil Hesselroth
 */
public class IntroController implements Initializable {

    @FXML
    ImageView imageView;
    @FXML
    Button btnNext;
    int counter = 1;
    List<Image> listofImages = new ArrayList();

    StringProperty whichHelpScreen = new SimpleStringProperty();

    public String getValue() {
        return whichHelpScreen.getValue();
    }

    public void setValue(String whichHelpScreen) {
        this.whichHelpScreen.set(whichHelpScreen);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        whichHelpScreen.addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {

                if (newValue != null && newValue.equals("tableView")) {
                    Image image1 = new Image(
                            getClass().getResourceAsStream("/Icons/1.jpg"));
                    Image image2 = new Image(
                            getClass().getResourceAsStream("/Icons/2.jpg"));

                    Image image3 = new Image(
                            getClass().getResourceAsStream("/Icons/3.jpg"));
                    listofImages.add(image1);
                    listofImages.add(image2);
                    listofImages.add(image3);
                    imageView.setImage(listofImages.get(0));
                    
                    
                } 
                
                
                else if (newValue != null && newValue.equals("combineView")) {
                    Image image1 = new Image(
                            getClass().getResourceAsStream("/Icons/1.jpg"));

                    listofImages.add(image1);
                    listofImages.add(image1);
                    imageView.setImage(listofImages.get(0));
                }

            }
        });

    }

    @FXML
    private void btnNext(ActionEvent event) {

        if (counter < listofImages.size()) {

            imageView.setImage(listofImages.get(counter));
            counter++;
            if (counter == listofImages.size()) {
                Image close = new Image(
                        getClass().getResourceAsStream("/Icons/close.png"));
                btnNext.setGraphic(new ImageView(close));
                btnNext.setText(null);

            }

        } else {

            MainFXMLController.stage.close();
        }

    }

    public void openWindow(String whichScreen) throws IOException {

        try {
            //  whichHelpScreen = "dataSelect";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/helpme.fxml"));
            Parent root = fxmlLoader.load();
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOpacity(1);
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root));

            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(IntroController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
