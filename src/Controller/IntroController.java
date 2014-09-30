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
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
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
    int counter = 0;
    List<Image> listofImages = new ArrayList();
    static String whichHelpScreen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       

      
      
      
      
        

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

    public void openWindow() throws IOException {
whichHelpScreen = "dataSelect";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/helpme.fxml"));
        Parent root = fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root));

        stage.showAndWait();

    }
}
